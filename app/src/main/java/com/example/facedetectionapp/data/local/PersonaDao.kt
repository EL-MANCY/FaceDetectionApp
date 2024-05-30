package com.example.facedetectionapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.facedetectionapp.data.local.entities.PersonEntity

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(persona: PersonEntity): Long

    @Query("SELECT * FROM person WHERE id = :id")
    fun getPerson(id: String): PersonEntity

    @Query("SELECT id FROM person")
    fun getPersonIds(): List<String>
}