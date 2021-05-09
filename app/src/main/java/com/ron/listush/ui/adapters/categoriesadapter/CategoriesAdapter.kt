package com.ron.listush.ui.adapters.categoriesadapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ron.listush.data.Category
import com.ron.listush.databinding.CategoryBinding
import com.ron.listush.ui.viewholders.CategoryViewHolder
import com.ron.listush.ui.viewmodels.CategoryViewModel

class CategoriesAdapter(val viewModel: CategoryViewModel) :
    ListAdapter<Category, CategoryViewHolder>(DiffCallbackCategory()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }


}

class DiffCallbackCategory : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category) =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem

}