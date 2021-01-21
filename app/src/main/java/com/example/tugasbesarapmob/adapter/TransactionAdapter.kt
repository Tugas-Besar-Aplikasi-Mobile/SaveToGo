
package com.example.tugasbesarapmob.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesarapmob.CustomOnItemClickListener
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.UI.TransactionAddUpdate
import com.example.tugasbesarapmob.entity.Transaction
import kotlinx.android.synthetic.main.transaction_adapter.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionAdapter(private val activity: Activity) : RecyclerView.Adapter<TransactionAdapter.TransViewHolder>() {

    var listNotes = ArrayList<Transaction>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNotes)

            notifyDataSetChanged()
        }

    fun addItem(note: Transaction) {
        this.listNotes.add(note)
        notifyItemInserted(this.listNotes.size - 1)
    }

    fun updateItem(position: Int, note: Transaction) {
        this.listNotes[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNotes.size)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapter.TransViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_adapter, parent, false)
        return TransViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int = this.listNotes.size

    inner class TransViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Transaction) {
            with(itemView){

                txtNamaTransaction.text = note.keterangan.toString()
                txtTanggalTransaction.text = note.date

                if (note.jenis.toString() == "Pemasukkan"){

                    ImageJenisTransaction.setImageResource(R.drawable.down)

                    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                    var masuk = format.format(note.pemasukkan)
                    txtSaldoTransaction.text = masuk.toString()


                }
                else {

                    ImageJenisTransaction.setImageResource(R.drawable.up)
                    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                    var saldo = format.format(note.pengeluaran)
                    txtSaldoTransaction.text = saldo.toString()
                }

                cv_transaction.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, TransactionAddUpdate::class.java)
                        intent.putExtra(TransactionAddUpdate.EXTRA_POSITION, position)
                        intent.putExtra(TransactionAddUpdate.EXTRA_NOTE, note)
                        activity.startActivityForResult(intent, TransactionAddUpdate.REQUEST_UPDATE)
                    }
                }))
            }
        }
    }
}

