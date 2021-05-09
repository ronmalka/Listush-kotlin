package com.ron.listush.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.ron.listush.OnItemClickListener
import com.ron.listush.data.Item
import com.ron.listush.databinding.ItemBinding


class ItemViewHolder(private val binding: ItemBinding, private val listener: OnItemClickListener) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item) {
        binding.apply {
            checkBox.isChecked = item.completed
            title.text = item.name
            quantity.text = item.quantity

            root.setOnClickListener {
                listener.onItemClick(item)
            }

            checkBox.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onCheckboxClick(item, checkBox.isChecked)
                }
            }
        }


    }
}