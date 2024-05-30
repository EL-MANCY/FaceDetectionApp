package com.example.facedetectionapp.presentation.inputDialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.facedetectionapp.databinding.FragmentInputDialogBinding
import java.io.File

class InputDialogFragment : DialogFragment() {
    private var _binding: FragmentInputDialogBinding? = null
    private val binding get() = _binding!!

    private var listener: InputDialogHandler? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            _binding = FragmentInputDialogBinding.inflate(it.layoutInflater)
            setImage()
            saveButton()
            cancelButton()
            builder.setView(binding.root)
                .setTitle("Enter Details")
                .create()
        } ?: throw IllegalStateException()
    }

    private fun setImage() {
        binding.ImageView.setImageBitmap(
            BitmapFactory.decodeFile(
                arguments?.getString(
                    BITMAP_PATH
                )
            )
        )
    }

    private fun saveButton() {
        binding.btSave.setOnClickListener {
          //  deleteBitmapFile()
            val id = binding.etId.text.toString()
            val name = binding.etName.text.toString()
            if (id.isNotBlank() && name.isNotBlank()) {
                listener?.onSave(id, name)
                dismiss()
            } else {
                Toast.makeText(
                    context,
                    "Enter Input Fields",
                    Toast.LENGTH_SHORT
                ).show()

                listener?.onCancel()
            }
        }
    }

    private fun cancelButton() {
        binding.btCancel.setOnClickListener {
            deleteBitmapFile()
            dismiss()
            listener?.onCancel()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
       // deleteBitmapFile()
        _binding = null
    }

    fun setListener(listener: InputDialogHandler) {
        this.listener = listener
    }

    private fun deleteBitmapFile() {
        arguments?.getString(BITMAP_PATH)?.let {
            val tempFile = File(it)
            if (tempFile.exists()) {
                tempFile.delete()
            }
        }
    }

    companion object {
        const val BITMAP_PATH = "bitmap_path"
    }
}
