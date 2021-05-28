package com.example.weekseven.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weekseven.model.Result
import com.example.weekseven.databinding.PokeListItemBinding

/**
 * The adapter binds the views to the data class content fetched from the API, as well as the images
 * fetched using the matching id from the url provided.
 *
 * Glide is used to load the images from the API source into the App
 * **/

class PokeListAdapter(var context: Context, var pokeSpecies: ArrayList<Result>, val listener: OnItemCLickListener)
    : RecyclerView.Adapter<PokeListAdapter.PokeViewHolder>() {

    inner class PokeViewHolder(binding: PokeListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var pokeImages = binding.pokeImage
        var pokeName = binding.pokeName

        /**
         * Handles the onClick event, returning the position of the clicked item
         * **/

        init {
            binding.root.setOnClickListener {
                var position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeViewHolder {
        var binding = PokeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokeViewHolder(binding)
    }

    override fun getItemCount() = pokeSpecies.size

    override fun onBindViewHolder(holder: PokeViewHolder, position: Int) {
        val pokeItem = pokeSpecies[position]
        holder.pokeName.text = pokeItem.name.toUpperCase()
        val id = getPokeId(pokeItem.url)
        Glide.with(context).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png")
                .into(holder.pokeImages)
    }

    interface OnItemCLickListener {
        fun onItemClick(position: Int)
    }

}