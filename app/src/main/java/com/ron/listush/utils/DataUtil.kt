package com.ron.listush.utils

import android.app.Activity
import com.ron.listush.R

object DataUtil {

    //view holder types

    val ITEM_TYPE = 0
    val TITLE_TYPE = 1

    //categories name

    val categoriesName = listOf(
        "Fruits",
        "Vegetables",
        "Drinks"
    )

    val categoriesResourceMap = hashMapOf<String,Int>(
        "Fruits" to R.mipmap.ic_fruits,
        "Vegetables" to R.mipmap.ic_vegetables,
        "Drinks" to R.mipmap.ic_drinks,
    )


    const val ADD_ITEM_RESULT_OK = Activity.RESULT_FIRST_USER
    const val EDIT_ITEM_RESULT_OK = Activity.RESULT_FIRST_USER + 1

    const val defaultName = "Listush"





}