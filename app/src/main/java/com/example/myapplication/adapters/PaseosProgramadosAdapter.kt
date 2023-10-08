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

class PaseosProgramadosAdapter(var paseos : MutableList<PaseoProgramado>,
                    var onClick : (Int) -> Unit
) : RecyclerView.Adapter<PaseosProgramadosAdapter.UserHolder>() {

       class UserHolder(v: View): RecyclerView.ViewHolder(v)
    {
        private var view : View
        init {
            this.view = v
        }

        val user = UserSession.user

        fun setDetails (detailsUser : String){
            val textDetail : TextView = view.findViewById(R.id.textDetail)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.paseoprogramado_item,parent,false)
        return (UserHolder(view))
    }

    override fun getItemCount(): Int {
        return paseos.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val paseo = paseos[position]
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