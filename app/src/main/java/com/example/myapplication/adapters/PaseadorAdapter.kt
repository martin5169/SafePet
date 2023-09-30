package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val textName : TextView = view.findViewById(R.id.txtMascotaName)
            textName.text = name
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

        holder.getCard().setOnClickListener{
            onClick(position)
        }

    }


}