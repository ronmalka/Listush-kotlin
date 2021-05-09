package com.ron.listush

import com.ron.listush.data.Item

interface OnItemClickListener {
    fun onItemClick(item: Item)
    fun onCheckboxClick(item: Item, isChecked: Boolean)
}