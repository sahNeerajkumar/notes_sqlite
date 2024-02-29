package com.example.notessqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notessqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DataBaseHelper
    private lateinit var adapterNotes: AdapterNotes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DataBaseHelper(this)
        adapterNotes = AdapterNotes(db.getAllNotes(), this)

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapterNotes

        binding.floatingButton.setOnClickListener {
            val intent = Intent(this, add_notes::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        adapterNotes.refreshData(db.getAllNotes())
    }
}
