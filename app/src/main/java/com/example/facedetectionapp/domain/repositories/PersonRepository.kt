package com.example.facedetectionapp.domain.repositories

import com.example.facedetectionapp.data.local.entities.PersonEntity

interface PersonRepository {
    suspend fun savePerson(personEntity: PersonEntity): Boolean
}