package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import entities.User

class UserAdapter(var users : MutableList<User>,
    var onClick : (Int) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    lateinit var perfil : Button

    class UserHolder(v: View): RecyclerView.ViewHolder(v)
    {
        private var view : View
        init {
            this.view = v
        }

        fun setUserLastName (lastName : String){
            val textLastName : TextView = view.findViewById(R.id.txtLastname)
            textLastName.text = lastName
        }
        fun setUserName (name : String){
            val textName : TextView = view.findViewById(R.id.txtUserName)
            textName.text = name
        }

        fun setUserMail (mail : String){
            val textDescription : TextView = view.findViewById(R.id.txtMail)
            textDescription.text = mail
        }

        fun getCard() : CardView {
            return view.findViewById(R.id.cardUser)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.paseador_item,parent,false)
        return (UserHolder(view))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.setUserLastName(users[position].lastName)
        holder.setUserName(users[position].name)
        holder.setUserMail(users[position].mail)
        holder.getCard().setOnClickListener{
            onClick(position)
        }

    }


}