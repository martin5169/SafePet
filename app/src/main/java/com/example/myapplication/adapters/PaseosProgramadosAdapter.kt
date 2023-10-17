package com.example.myapplication.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entities.Paseador
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.UserSession
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    fun isPaseoAnterior(paseo: PaseoProgramado): Boolean {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val paseoDate = dateFormat.parse(paseo.fecha)
        return paseoDate != null && paseoDate.before(currentDate)
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

       // val cardLayout = holder.getCard().getChildAt(position) as View

       // val colorPaseoAnterior = ContextCompat.getColor(holder.getCard().context, R.color.colorPaseoAnterior)
       // val colorPaseoNormal = ContextCompat.getColor(holder.getCard().context, R.color.colorPaseoNormal)

        if (isPaseoAnterior(paseo)) {
            //cardLayout.setBackgroundColor(ContextCompat.getColor(holder.getCard().context, R.color.colorPaseoAnterior))
            holder.getCard().isEnabled = false
        } else {
         //   holder.getCard().setCardBackgroundColor(colorPaseoNormal)
            holder.getCard().isEnabled = true
        }

        if (UserSession.user is Paseador) {
            holder.setDetails("Dueño: ${paseo.user.lastName}")
            holder.setFechaPaseo(paseo.fecha)
        } else {
            holder.setFechaPaseo(paseo.fecha)
            holder.setDetails("Paseador: ${paseo.paseador.lastName}")
        }

        holder.getCard().setOnClickListener {
            if (holder.getCard().isEnabled) {
                onClick(position)
            }
        }
    }

}


