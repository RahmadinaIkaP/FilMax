package com.rahmadina.ika.movieapp_kotlin.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.trending.ResultTrendingMovies
import com.rahmadina.ika.movieapp_kotlin.databinding.ItemFilmsBinding

class TrendingMovieAdapter( private val onClick : TrendingMoviesInterface) : RecyclerView.Adapter<TrendingMovieAdapter.ViewHolder>() {

    private val limit = 20

    private val differCallback = object : DiffUtil.ItemCallback<ResultTrendingMovies>(){
        override fun areItemsTheSame(
            oldItem: ResultTrendingMovies,
            newItem: ResultTrendingMovies
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultTrendingMovies,
            newItem: ResultTrendingMovies
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(private val binding: ItemFilmsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resultTrendingMovies: ResultTrendingMovies){
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w400${resultTrendingMovies.posterPath}")
                .into(binding.ivMovie)

            itemView.setOnClickListener {
                onClick.onItemClick(resultTrendingMovies)
            }
        }
    }

    interface TrendingMoviesInterface {
        fun onItemClick(resultTrendingMovies: ResultTrendingMovies)
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

    fun setData(data : List<ResultTrendingMovies>){
        differ.submitList(data)
    }
}