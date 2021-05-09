package com.ron.listush.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ron.listush.data.Category
import com.ron.listush.databinding.CategoryBinding
import com.ron.listush.ui.viewmodels.CategoryViewModel

class CategoryViewHolder(private val binding: CategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(category: Category, viewModel: CategoryViewModel) {
        binding.apply {
            Glide.with(itemView)
                .load(category.iconPath)
                .into(categoryButton)

            categoryButton.setOnClickListener {
                viewModel.NavigateToCategoryScreen(category.name)
            }
        }
    }
}