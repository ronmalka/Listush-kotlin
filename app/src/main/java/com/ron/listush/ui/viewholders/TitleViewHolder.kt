package com.ron.listush.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.ron.listush.databinding.CategoryTitleBinding

class TitleViewHolder(private val binding: CategoryTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(name: String) {
        binding.apply {
            title.text = name
        }
    }
}