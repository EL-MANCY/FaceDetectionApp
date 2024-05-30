package com.example.facedetectionapp.presentation.inputDialog

interface InputDialogHandler {
    fun onSave(id: String, name: String)
    fun onCancel()
}