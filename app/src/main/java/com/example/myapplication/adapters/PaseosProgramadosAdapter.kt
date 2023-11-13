package com.example.myapplication.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entities.EstadoEnum
import com.example.myapplication.entities.Paseador
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.UserSession
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class PaseosProgramadosAdapter(var paseos : MutableList<PaseoProgramado>,
                    var onClick : (Int) -> Unit
) : RecyclerView.Adapter<PaseosProgramadosAdapter.UserHolder>() {

       class UserHolder(v: View): RecyclerView.ViewHolder(v)
    {
        private var view : View
        init {
            this.view = v
        }

        val imagenPaseo : ImageView = view.findViewById(R.id.imagenPaseoPendiente)
        val user = UserSession.user

        fun setImage (estadoPaseo : EstadoEnum){
            if(estadoPaseo != EstadoEnum.FINALIZADO){
                imagenPaseo.visibility = View.VISIBLE
            }
            else{
                imagenPaseo.visibility = View.GONE
            }
        }

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
        Log.d("FECHA HOY ADAPTER", dateFormat.format(currentDate))
        val paseoDate = dateFormat.parse(paseo.fecha)
        return ((currentDate.time - 10800000) - paseoDate.time).absoluteValue >= 300000
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

        holder.setImage(paseo.estado)
        holder.getCard().isEnabled = true

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


