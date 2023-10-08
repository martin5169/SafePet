package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entities.Paseador
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.UserSession

class PaseosProgamadosAdapter(var paseosProgramados : MutableList<PaseoProgramado>,
                              var onClick : (Int) -> Unit
) : RecyclerView.Adapter<PaseosProgamadosAdapter.PaseoHolder>() {

    class PaseoHolder(v: View) : RecyclerView.ViewHolder(v)
    {
        private var view: View

        init {
            this.view = v
        }

    fun setDetails (detailsUser : String){
        val textDetail : TextView = view.findViewById(R.id.txtDetalle)
        textDetail.text = detailsUser
    }

    fun setFechaPaseo (fecha : String){
        val fechaPaseo : TextView = view.findViewById(R.id.txtFecha)
        fechaPaseo.text = fecha
    }

    fun getCard() : CardView {
        return view.findViewById(R.id.cardPaseoProgramado)
    }
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaseoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.paseoprogramado_item,parent,false)
        return (PaseoHolder(view))
    }

    override fun getItemCount(): Int {
        return paseosProgramados.size
    }

    override fun onBindViewHolder(holder: PaseoHolder, position: Int) {
        val paseo = paseosProgramados[position]

        if (UserSession.user is Paseador) {
            holder.setDetails("Dueño: ${paseo.user.lastName}")
            holder.setFechaPaseo(paseo.fecha)
        } else {
            // Mostrar solo la fecha si el usuario es un User
            holder.setFechaPaseo(paseo.fecha)
            holder.setDetails("Paseador: ${paseo.paseador.lastName}")
        }

        holder.getCard().setOnClickListener{
            onClick(position)
        }
    }
}