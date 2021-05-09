package com.ron.listush.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM item_table")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM item_table WHERE category = :category")
    fun getItemsByCategory(category: String): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)


}