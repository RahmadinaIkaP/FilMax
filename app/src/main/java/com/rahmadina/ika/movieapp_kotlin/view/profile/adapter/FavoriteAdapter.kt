package com.rahmadina.ika.movieapp_kotlin.view.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahmadina.ika.movieapp_kotlin.data.model.Favorite
import com.rahmadina.ika.movieapp_kotlin.databinding.ItemSearchBinding

class FavoriteAdapter(private val onClick : FavoriteInterface) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Favorite>(){
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(private val binding : ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(favorite: Favorite){
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w400${favorite.img}")
                    .into(binding.ivMovie)

                binding.tvName.text = favorite.name

                itemView.setOnClickListener {
                    onClick.onItemClick(favorite)
                }
            }
    }

    interface FavoriteInterface {
        fun onItemClick(favorite: Favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun setData(data : List<Favorite>){
        differ.submitList(data)
    }

}