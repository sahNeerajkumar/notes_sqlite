package com.example.notessqlite

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.notessqlite.databinding.ActivityAddNotesBinding
import java.io.ByteArrayOutputStream

class add_notes : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    private lateinit var db :DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DataBaseHelper(this)


        binding.doneIconAddData.setOnClickListener {
            val name = binding.editName.text.toString()
            val subtitle = binding.SubTitle.text.toString()
            val  note = Note(0,name,subtitle )
            db.insertNote(note)
            finish()
            Toast.makeText(this, "Note Save Data", Toast.LENGTH_SHORT).show()
        }


       binding.openCamera.setOnClickListener { addImage() }
    }
    @SuppressLint("MissingInflatedId")
    private fun addImage() {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.add_open_camera, null)
        val openCamera = view.findViewById<LinearLayout>(R.id.linerLayoutCamera)
        val openGallery = view.findViewById<LinearLayout>(R.id.linerLayoutGallery)

        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(view)
        val dialog = addDialog.create()
        dialog.show()

        openCamera.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog before opening the camera
            openCameraAndTakeImage()
        }
        openGallery.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog before opening the gallery
            openGalleryAndTakeImage()
        }
    }
    private fun openCameraAndTakeImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 10)
    }

    private fun openGalleryAndTakeImage() {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(galleryIntent, ""), 200
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == RESULT_OK) {
            val intent = data?.extras?.get("data") as Bitmap
            binding.imageSet.setImageBitmap(intent)
            val imageStore = bitmapToString(intent)
            storeImageAsBitmap(imageStore)
        }
        if (requestCode == 200 && resultCode == RESULT_OK){
            if (data != null && data.data != null){
                val imageUri = data.data
                val uriToString = imageUri.toString()
                try {
                    val imageData = contentResolver.openInputStream(imageUri!!)
                    val gallery = BitmapFactory.decodeStream(imageData)
                    binding.imageSet.setImageURI(imageUri)

                    val imageToString = bitmapToString(gallery)
                    storeImageAsBitmap(imageToString)

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }
    private fun bitmapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val arr = baos.toByteArray()
        return android.util.Base64.encodeToString(arr, android.util.Base64.DEFAULT)
    }

    private fun storeImageAsBitmap(image: String?) {
        val preImageView = getSharedPreferences("images", MODE_PRIVATE)
        val edit = preImageView.edit()
        edit.putString("gallery", image)
        edit.apply()
    }


}