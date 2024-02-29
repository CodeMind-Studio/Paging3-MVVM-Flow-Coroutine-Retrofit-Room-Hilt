package com.example.pagingrickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pagingrickandmorty.data.Result
import com.example.pagingrickandmorty.databinding.EachItemLayoutBinding
import javax.inject.Inject

class PersonAdapter @Inject constructor() :
    PagingDataAdapter<Result, PersonAdapter.ViewHolder>(Diff) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        if (current != null) {
            holder.bind(current)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            EachItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class ViewHolder(private val binding: EachItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: Result) {
            binding.apply {
                actionImagePerson.load(list.image)
                textName.text = list.name
            }
        }
    }


    object Diff : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem == newItem

    }
}