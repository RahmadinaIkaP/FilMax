package com.rahmadina.ika.movieapp_kotlin.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahmadina.ika.movieapp_kotlin.data.model.tvshows.ResultPopularTvShows
import com.rahmadina.ika.movieapp_kotlin.databinding.ItemFilmsBinding

class PopularTvShowsAdapter(private val onClick : PopularTvInterface) : RecyclerView.Adapter<PopularTvShowsAdapter.ViewHolder>(){

    private val limit = 20

    private val differCallback = object : DiffUtil.ItemCallback<ResultPopularTvShows>(){
        override fun areItemsTheSame(
            oldItem: ResultPopularTvShows,
            newItem: ResultPopularTvShows
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultPopularTvShows,
            newItem: ResultPopularTvShows
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder (private val binding: ItemFilmsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resultPopularTvShows: ResultPopularTvShows){
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w400${resultPopularTvShows.posterPath}")
                .into(binding.ivMovie)

            itemView.setOnClickListener {
                onClick.onItemClick(resultPopularTvShows)
            }
        }
    }

    interface PopularTvInterface {
        fun onItemClick(resultPopularTvShows: ResultPopularTvShows)
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

    fun setData(data : List<ResultPopularTvShows>){
        differ.submitList(data)
    }
}