package com.example.facedetectionapp.presentation.faceDetection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facedetectionapp.data.local.entities.PersonEntity
import com.example.facedetectionapp.domain.repositories.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceDetectorViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {


    fun saveNewFace(personEntity: PersonEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                personRepository.savePerson(personEntity)
            } catch (e: Exception) {

            }
        }
    }
}