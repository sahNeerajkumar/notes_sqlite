package com.example.notessqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notessqlite.databinding.ActivityUpdateBinding


class UpdateButton : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: DataBaseHelper
    private var noteId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DataBaseHelper(this)
        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }
        val note = db.getNoteByID(noteId)
        binding.updateName.setText(note.title)
        binding.updateSubTitle.setText(note.content)
        binding.updateButton.setOnClickListener {
            val newTitle = binding.updateName.text.toString()
            val newContent = binding.updateSubTitle.text.toString()
            val updateNote = Note(noteId, newTitle, newContent)
            db.updateNote(updateNote)
            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }
    }
}