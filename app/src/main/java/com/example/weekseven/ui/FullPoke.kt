package com.example.weekseven.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weekseven.R
import com.example.weekseven.api.myApi
import com.example.weekseven.databinding.FragmentFullPokeBinding
import com.example.weekseven.util.PokeViewModel
import kotlinx.android.synthetic.main.fragment_full_poke.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


/**
 *Shows all the details of the pokemon item selected from the item's id passed as an argument
 */

class FullPoke : Fragment() {

    var fragmentBinding: FragmentFullPokeBinding? = null
    lateinit var binding: FragmentFullPokeBinding
    lateinit var pokeModel: PokeViewModel
    lateinit var id: String
    private val pokeArgs: FullPokeArgs by navArgs()
    var statis = arrayListOf<Pair<String, String>>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var rootie = inflater.inflate(R.layout.fragment_full_poke, container, false)

        binding = FragmentFullPokeBinding.bind(rootie)

        fragmentBinding = binding

        pokeModel = ViewModelProvider(this).get(PokeViewModel::class.java)

        id = pokeArgs.id
        binding.pokeName.text = pokeArgs.name.toUpperCase()

        binding.pokeid.text = id.toString()

        makePokeApiRequest()

        Glide.with(this).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png")
                .into(binding.pokePicOne)

        Glide.with(this).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/$id.png")
                .into(binding.pokePicTwo)

        Glide.with(this).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/$id.png")
                .into(binding.pokePicThree)

        return binding.root
    }

    /**
     *Fetches the other images of the pokemon character
     * */

    override fun onDestroyView() {
        super.onDestroyView()

        fragmentBinding = null
    }

    /**
     * Request using the API link created and populate the viewmodel items with the details fetched
     * In a case where the detail is not fetched, the catch block is logged providing the thrown exception
     * */

    fun makePokeApiRequest() {
        GlobalScope.launch(Dispatchers.IO) {

            try {
                val response = myApi.getPokeDetails(id)

                for (abs in response.abilities) {
                    pokeModel.addAbility(abs.ability)
                    Log.i("Poke", abs.ability.name)
                }

                for (mov in response.moves) {
                    pokeModel.addMove(mov.move)
                    Log.i("Poke", mov.move.name)
                }

                for (sta in response.stats) {
                    var statname = sta.stat.name
                    var statrate = sta.base_stat.toString()

                    pokeModel.addStat(Pair(statname, statrate))
                    Log.i("Poke", sta.stat.name)
                    Log.i("Poke", "${sta.base_stat}")
                }

                for (typ in response.types) {
                    pokeModel.addType(typ.type)
                    Log.i("Poke", typ.type.name)
                }

                response.weight.toString()
                Log.i("Poke", response.weight.toString())

                /**
                 *The details fetched are passed into the main thread and displayed on the UI
                 * */

                withContext(Dispatchers.Main) {
                    var abt = ""
                    var mov = ""
                    var typ = ""
                    for (i in pokeModel.pokeAbilities.indices) {
                        abt += if (i == pokeModel.pokeAbilities.lastIndex) {
                            pokeModel.pokeAbilities[i].name
                        } else {
                            "${pokeModel.pokeAbilities[i].name}, "
                        }
                    }
                    binding.abilities.text = abt.toUpperCase()

                    binding.pokeWeight.text = "Weight: ${response.weight}"

                    for (i in pokeModel.pokeMoves.indices) {
                        mov += if (i == pokeModel.pokeMoves.lastIndex) {
                            pokeModel.pokeMoves[i].name
                        } else {
                            "${pokeModel.pokeMoves[i].name}, "
                        }
                    }
                    binding.move.text = mov.toUpperCase()

                    for (i in pokeModel.pokeStatistics.indices) {
                        statis.add(pokeModel.pokeStatistics[i])
                    }
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_activated_1, statis)
                    binding.statContainer.adapter = adapter
                    for (i in pokeModel.pokeType.indices) {
                        typ += if (i == pokeModel.pokeType.lastIndex) {
                            pokeModel.pokeType[i].name
                        } else {
                            "${pokeModel.pokeType[i].name}, "
                        }
                    }
                    binding.pokeTypes.text = typ.toUpperCase()
                }
            } catch (ex: Exception) {
                Log.e("MainActivity", ex.toString())
            }
        }
    }
}