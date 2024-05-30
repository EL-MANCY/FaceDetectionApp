package com.example.facedetectionapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.facedetectionapp.data.local.entities.PersonEntity

@Database(entities = [PersonEntity::class], version = 1, exportSchema = false)
@TypeConverters(BitmapConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun personDao(): PersonDao

}