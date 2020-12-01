package ru.zolax.tasklist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Item::class], version = 1)
abstract class ItemDatabase() : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object{
        @Volatile
        private var INSTANCE: ItemDatabase? = null


        fun getDatabase(context: Context, scope: CoroutineScope) : ItemDatabase{
            return INSTANCE ?: synchronized(this){
                val instance: ItemDatabase = Room.databaseBuilder(
                    context.applicationContext,
                ItemDatabase::class.java,
                "item_database"
                ).fallbackToDestructiveMigration().addCallback(ItemDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }

        fun populateDatabase(itemDao: ItemDao){
            itemDao.insert(Item(title = "Title 1", description = "Description 1", priority = true))
        }


        private class  ItemDatabaseCallback(private val scope: CoroutineScope) :RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {database->
                    scope.launch(Dispatchers.IO){
                        populateDatabase(database.itemDao())
                    }
                }
            }
        }
    }
}