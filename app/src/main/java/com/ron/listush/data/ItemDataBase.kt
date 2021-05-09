package com.ron.listush.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ron.listush.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Item::class], version = 1)
abstract class ItemDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    class Callback @Inject constructor(
        private val database: Provider<ItemDataBase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().itemDao()

            applicationScope.launch {
                dao.insert(Item("Fruits", "", "Fruits", 1))
                dao.insert( Item("Apple","1Kg","Fruits",0))
                dao.insert(Item("Vegetables","","Vegetables",1))
                dao.insert( Item("Cucumber","1Kg","Vegetables",0))
                dao.insert(Item("Drinks","","Drinks",1))
                dao.insert( Item("Water","1 Bottle","Drinks",0))
            }
        }
    }

}