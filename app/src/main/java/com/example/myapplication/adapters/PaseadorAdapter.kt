package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entities.Paseador

class PaseadorAdapter(var paseadores : MutableList<Paseador>,
                      var onClick : (Int) -> Unit
) : RecyclerView.Adapter<PaseadorAdapter.UserHolder>() {

       class UserHolder(v: View): RecyclerView.ViewHolder(v)
    {
        private var view : View
        init {
            this.view = v
        }

        fun setUserLastName (lastName : String){
            val textLastName : TextView = view.findViewById(R.id.textDetail)
            textLastName.text = lastName
        }
        fun setUserName (name : String){
            val textName : TextView = view.findViewById(R.id.txtPaseadorName)
            textName.text = name
        }

        fun setPromedio(promedio: Int) {
            val stars: Array<ImageView> = arrayOf(
                view.findViewById(R.id.star1),
                view.findViewById(R.id.star2),
                view.findViewById(R.id.star3),
                view.findViewById(R.id.star4),
                view.findViewById(R.id.star5)
            )

            // Ocultar todas las estrellas
            for (star in stars) {
                star.visibility = View.INVISIBLE
            }

            if (promedio > 0) {
                // Mostrar solo la cantidad de estrellas correspondientes al promedio
                for (i in 0 until promedio) {
                    stars[i].visibility = View.VISIBLE
                }
            }
        }




        fun getCard() : CardView {
            return view.findViewById(R.id.cardPaseoProgramado)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.paseador_item,parent,false)
        return (UserHolder(view))
    }

    override fun getItemCount(): Int {
        return paseadores.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.setUserLastName(paseadores[position].lastName)
        holder.setUserName(paseadores[position].name)
        holder.setPromedio(paseadores[position].promedioPuntuaciones)

        holder.getCard().setOnClickListener{
            onClick(position)
        }

    }


}