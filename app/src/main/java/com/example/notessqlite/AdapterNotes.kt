package com.example.notessqlite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AdapterNotes(private var notes: List<Note>, context: Context) :RecyclerView.Adapter<AdapterNotes.NotesViewHolder>() {
    private val db: DataBaseHelper = DataBaseHelper(context)
    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.title_textView)
        val subtitleTextView: TextView = itemView.findViewById(R.id.Subtitle_textView)
        val updateButton: ImageView = itemView.findViewById(R.id.edit_text)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_Icon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.subtitleTextView.text = note.content

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateButton::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener {
            showDeleteAlertDialog(holder.itemView.context, note)
        }


    }

    private fun showDeleteAlertDialog(context: Context, note: Note) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete Date")
        alertDialogBuilder.setMessage("Are you sure you want to delete this data")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            db.deleteData(note.id)
            refreshData(db.getAllNotes())
            Toast.makeText(context, "Delete Data ", Toast.LENGTH_SHORT).show()
        }
        alertDialogBuilder.setNegativeButton("No") { _, _ ->

        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.setCancelable(false)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()

    }

}








