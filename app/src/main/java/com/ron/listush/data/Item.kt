package com.ron.listush.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "item_table")
@Parcelize
data class Item(
    var name: String,
    var quantity: String,
    @ColumnInfo(name = "category") val category: String,
    val type: Int,
    var completed: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {

}