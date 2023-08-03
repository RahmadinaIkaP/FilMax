package com.rahmadina.ika.movieapp_kotlin.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.Genre
import com.rahmadina.ika.movieapp_kotlin.databinding.ItemGenreBinding

class GenreAdapter(private val list : List<Genre>) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    class ViewHolder(private val binding : ItemGenreBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(genre : Genre){
                binding.tvGenre.text = genre.name
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemGenreBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}