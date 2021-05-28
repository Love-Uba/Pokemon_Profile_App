package com.example.weekseven.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weekseven.R
import com.example.weekseven.api.myApi
import com.example.weekseven.databinding.FragmentPokeListBinding
import com.example.weekseven.model.Result
import com.example.weekseven.util.PokeListAdapter
import com.example.weekseven.util.PokeViewModel
import com.example.weekseven.util.getPokeId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *Displays the list items and attach to the recycler populating the list
 *
 * */

class PokeList : Fragment(), PokeListAdapter.OnItemCLickListener {

    var fragmentBinding: FragmentPokeListBinding? = null
    lateinit var binding: FragmentPokeListBinding
    lateinit var pokeModel: PokeViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val rootie = inflater.inflate(R.layout.fragment_poke_list, container, false)
        binding = FragmentPokeListBinding.bind(rootie)
        fragmentBinding = binding
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pokeModel = ViewModelProvider(this).get(PokeViewModel::class.java)

        makePokeAPIRequest()

        binding.uploadButton.setOnClickListener {
            uploadImageFragment()
        }

    }

    fun makePokeAPIRequest() {

        GlobalScope.launch(Dispatchers.IO) {

            try {
                val pokeResponse = myApi.getPokeBasic()
                for (item in pokeResponse.results) {
                    pokeModel.addPokeToList(item)
                    Log.i("Main", item.name)
                }
                withContext(Dispatchers.Main) {
                    setUpPokeList()
                }

            } catch (ex: Exception) {
                Log.e("Main", ex.toString())
            }
        }
    }

    fun setUpPokeList() {
        pokeModel.pokeListLive.observe(requireActivity(), Observer {
            binding.pokeRecycler.adapter =
                    PokeListAdapter(requireActivity(), it as ArrayList<Result>, this)
            binding.pokeRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        })
    }

    fun uploadImageFragment() {
        val action = PokeListDirections.actionPokeListToUploadImage()
        findNavController().navigate(action)
    }

    override fun onItemClick(position: Int) {
        var id = getPokeId(pokeModel.pokeItemList[position].url)
        var name = pokeModel.pokeItemList[position].name
        val action = PokeListDirections.actionPokeListToFullPoke(id, name)
        findNavController().navigate(action)
        Log.i("Main", id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}