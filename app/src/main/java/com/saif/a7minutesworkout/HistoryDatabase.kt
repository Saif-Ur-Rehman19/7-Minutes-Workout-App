package com.saif.a7minutesworkout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryDatabase : RoomDatabase(){

    abstract fun historyDao(): HistoryDao

    companion object{
        // companion abject is equivalent to static keyword in java. Means it belong to
        // the type not to the instance. So we don't need to create instance in order to run
        // code inside companion object.
        @Volatile// volatile keyword is used to force changes in a variable to be immediately
        // visible to other threads
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext, HistoryDatabase::class.java,
                        "history_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}