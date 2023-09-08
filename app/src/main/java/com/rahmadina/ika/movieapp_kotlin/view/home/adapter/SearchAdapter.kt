package com.rahmadina.ika.movieapp_kotlin.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahmadina.ika.movieapp_kotlin.data.model.search.ResultSearch
import com.rahmadina.ika.movieapp_kotlin.databinding.ItemSearchBinding

class SearchAdapter(private val onClick : SearchResultInterface) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ResultSearch>(){
        override fun areItemsTheSame(oldItem: ResultSearch, newItem: ResultSearch): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultSearch, newItem: ResultSearch): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(resultSearch: ResultSearch){
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w400${resultSearch.posterPath}")
                    .into(binding.ivMovie)

                binding.tvName.text = resultSearch.title

                itemView.setOnClickListener {
                    onClick.onItemClick(resultSearch)
                }
            }
    }

    interface SearchResultInterface {
        fun onItemClick(resultSearch: ResultSearch)
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

    fun setData(data : List<ResultSearch>){
        differ.submitList(data)
    }
}