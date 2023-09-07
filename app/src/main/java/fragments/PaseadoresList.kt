package fragments

import adapters.UserAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import entities.UserRepository

class PaseadoresList : Fragment() {

    lateinit var v : View
    lateinit var recyclerUsers: RecyclerView
    lateinit var userRepository: UserRepository
    lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_paseadores_list, container, false)
        recyclerUsers = v.findViewById(R.id.recyclerUser)
        userRepository = UserRepository.getInstance()


        return v
    }


    override fun onStart() {
        super.onStart()
        userRepository.getUsers { userList ->
            adapter = UserAdapter(userList.toMutableList()){ position ->
                val action = PaseadoresListDirections.actionPaseadoresListToPaseadorDetail(userList[position])
                    findNavController().navigate(action)
            }
            recyclerUsers.layoutManager = LinearLayoutManager(context)
            recyclerUsers.adapter = adapter
        }

    }


}