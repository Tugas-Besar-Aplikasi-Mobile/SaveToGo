package com.example.tugasbesarapmob.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesarapmob.CustomOnItemClickListener
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.UI.SavingAddUpdate
import com.example.tugasbesarapmob.UI.SavingAddUpdate.Companion.EXTRA_NOTE
import com.example.tugasbesarapmob.UI.SavingAddUpdate.Companion.EXTRA_POSITION
import com.example.tugasbesarapmob.UI.SavingAddUpdate.Companion.REQUEST_UPDATE
import com.example.tugasbesarapmob.entity.Saving
import kotlinx.android.synthetic.main.nabung_adapter.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class TabunganAdapter(private val activity: Activity) : RecyclerView.Adapter<TabunganAdapter.TransViewHolder>() {

    var listNotes = ArrayList<Saving>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNotes)

            notifyDataSetChanged()
        }

    fun addItem(note: Saving) {
        this.listNotes.add(note)
        notifyItemInserted(this.listNotes.size - 1)
    }

    fun updateItem(position: Int, note: Saving) {
        this.listNotes[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNotes.size)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabunganAdapter.TransViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nabung_adapter, parent, false)
        return TransViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int = this.listNotes.size

    inner class TransViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Saving) {
            with(itemView){

                txtNamaSaving.text = note.keterangan.toString()
                txtTanggalSaving.text = note.date

                val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                var saldo = format.format(note.pemasukkan)
                txtSaldoSaving.text = saldo.toString()

                cv_savings.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, SavingAddUpdate::class.java)
                        intent.putExtra(EXTRA_POSITION, position)
                        intent.putExtra(EXTRA_NOTE, note)
                        activity.startActivityForResult(intent, REQUEST_UPDATE)
                    }
                }))
            }
        }
    }
}
