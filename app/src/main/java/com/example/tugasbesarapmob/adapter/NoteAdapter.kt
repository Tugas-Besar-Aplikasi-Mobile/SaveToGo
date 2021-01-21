package com.example.tugasbesarapmob.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesarapmob.CustomOnItemClickListener
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.UI.NoteAddUpdate
import com.example.tugasbesarapmob.entity.Notes
import kotlinx.android.synthetic.main.note_adapter.view.*

class NoteAdapter(private val activity: Activity) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var listNotes = ArrayList<Notes>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNotes)
            notifyDataSetChanged()
        }

    fun addItem(note: Notes) {
        this.listNotes.add(note)
        notifyItemInserted(this.listNotes.size - 1)
    }

    fun updateItem(position: Int, note: Notes) {
        this.listNotes[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNotes.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_adapter, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int = this.listNotes.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Notes) {
            with(itemView){

                judulNotes.text = note.judul
                tanggalNotes.text = note.date
                contentNotes.text = note.content

                cv_notes.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, NoteAddUpdate::class.java)
                        intent.putExtra(NoteAddUpdate.EXTRA_POSITION, position)
                        intent.putExtra(NoteAddUpdate.EXTRA_NOTE, note)
                        activity.startActivityForResult(intent,NoteAddUpdate.REQUEST_UPDATE)
                    }
                }))
            }
        }
    }
}