package com.example.tugasbesarapmob.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesarapmob.CustomOnItemClickListener
import com.example.tugasbesarapmob.UI.PerhitunganAddUpdate
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.entity.Perhitungan
import kotlinx.android.synthetic.main.perhitungan_adapter.view.*
import kotlinx.android.synthetic.main.saving_adapter.view.txtNamaTransaksi
import kotlinx.android.synthetic.main.saving_adapter.view.txtTanggalTransaksi
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class PerhitunganAdapter(private val activity: Activity) : RecyclerView.Adapter<PerhitunganAdapter.NoteViewHolder>()  {

    var listNotes = ArrayList<Perhitungan>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNotes)

            notifyDataSetChanged()
        }

    fun addItem(note: Perhitungan) {
        this.listNotes.add(note)
        notifyItemInserted(this.listNotes.size - 1)
    }

    fun updateItem(position: Int, note: Perhitungan) {
        this.listNotes[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNotes.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.perhitungan_adapter, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int = this.listNotes.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Perhitungan) {
            with(itemView){
                txtNamaTransaksi.text = note.nama
                txtTanggalTransaksi.text = note.date

                var hari = note.hari.toString()
                lamaHari.text = "$hari Hari"

                val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                var saldo = format.format(note.saldo)

                txtTotal.text = saldo.toString()

                var total = format.format(note.total).toString()

                txtperkiraan.text = "Perkiraan Pendapatan : $total"

                cv_item_note.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, PerhitunganAddUpdate::class.java)
                        intent.putExtra(PerhitunganAddUpdate.EXTRA_POSITION, position)
                        intent.putExtra(PerhitunganAddUpdate.EXTRA_NOTE, note)
                        activity.startActivityForResult(intent, PerhitunganAddUpdate.REQUEST_UPDATE)
                    }
                }))
            }
        }
    }
}