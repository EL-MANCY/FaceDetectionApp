package com.example.facedetectionapp.data.repositoriesImpl

import android.util.Log
import com.example.facedetectionapp.data.local.PersonDao
import com.example.facedetectionapp.data.local.entities.PersonEntity
import com.example.facedetectionapp.domain.repositories.PersonRepository
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val personDao: PersonDao,
) : PersonRepository {

    override suspend fun savePerson(personEntity: PersonEntity): Boolean {
        return try {
            val person = PersonEntity(
                id = personEntity.id,
                name = personEntity.name,
                personEntity.bitmapImage
            )
            personDao.insertPerson(person)
            true
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            false
        }
    }

    companion object {
        const val TAG = "ModelAnalyzerRepositoryImpl"
    }
}