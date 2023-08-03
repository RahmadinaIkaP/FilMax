package com.rahmadina.ika.movieapp_kotlin.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.ResultPopularMovie
import com.rahmadina.ika.movieapp_kotlin.databinding.ItemFilmsBinding

class PopularMovieAdapter( private val onClick : PopularMoviesInterface) : RecyclerView.Adapter<PopularMovieAdapter.ViewHolder>() {

    private val limit = 20

    private val differCallback = object : DiffUtil.ItemCallback<ResultPopularMovie>(){
        override fun areItemsTheSame(
            oldItem: ResultPopularMovie,
            newItem: ResultPopularMovie
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultPopularMovie,
            newItem: ResultPopularMovie
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(private val binding: ItemFilmsBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(resultPopularMovie: ResultPopularMovie){
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w400${resultPopularMovie.posterPath}")
                    .into(binding.ivMovie)

                itemView.setOnClickListener {
                    onClick.onItemClick(resultPopularMovie)
                }
            }
    }

    interface PopularMoviesInterface {
        fun onItemClick(resultPopularMovie: ResultPopularMovie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemFilmsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return if (differ.currentList.size > limit){
            limit
        }else{
            differ.currentList.size
        }
    }

    fun setData(data : List<ResultPopularMovie>){
        differ.submitList(data)
    }
}