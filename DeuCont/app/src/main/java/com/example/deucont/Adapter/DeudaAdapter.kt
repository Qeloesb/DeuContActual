package com.example.deucont.Adapter

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deucont.R
import com.example.deucont.model.Deuda
import com.google.android.material.button.MaterialButton

class DeudaAdapter (private val deudas: ArrayList<Deuda>):
RecyclerView.Adapter<DeudaAdapter.ViewHolder>() {
    private var onDeleteClickListener: OnDeleteClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nombre_deuda : TextView = itemView.findViewById(R.id.tvNombreDeuda)
        val valor_deuda : TextView = itemView.findViewById(R.id.tvValorDeuda)
        val valor_mensual : TextView = itemView.findViewById(R.id.tvValorMensual)
        val cantidad_cuotas : TextView = itemView.findViewById(R.id.tvValorCuota)
        val fecha_pago : TextView = itemView.findViewById(R.id.tvFechaPago)
        val delete_deuda: MaterialButton = itemView.findViewById(R.id.btnDeleteItem)
        val edit_deuda: MaterialButton = itemView.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_deuda, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  deudas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deuda = deudas[position]
        holder.nombre_deuda.text = "Nombre de la Deuda: ${deuda.nombreDeuda}"
        holder.valor_deuda.text = "Valor Deuda: ${deuda.valorDeuda}"
        holder.valor_mensual.text = "Valor Mensual: ${deuda.valorMensual}"
        holder.cantidad_cuotas.text = "Cantidad Cuotas: ${deuda.cantidadCuotas}"
        holder.fecha_pago.text = "Fecha Pago: ${deuda.fechaPago}"
        holder.edit_deuda.setOnClickListener {
            onDeleteClickListener?.onEditClick(deuda)
        }
        holder.delete_deuda.setOnClickListener {
            Log.d("DeudaAdapter", "Esta clicleando en la posision: ${holder.adapterPosition}")
            onDeleteClickListener?.onDeleteClick(holder.adapterPosition)
        }
    }
    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
        fun onDeleteClick(deudaId: String)
        fun onEditClick(deuda: Deuda)
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }
}
