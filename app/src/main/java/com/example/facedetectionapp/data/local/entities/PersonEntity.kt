package com.example.facedetectionapp.data.local.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person")
data class PersonEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "bitmapImage") val bitmapImage: Bitmap
)
