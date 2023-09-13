package fragments

import adapters.PaseadorAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import entities.PaseadorRepository

class PaseadoresList : Fragment() {

    lateinit var v : View
    lateinit var recyclerPaseadores: RecyclerView
    lateinit var paseadoresRepository: PaseadorRepository
    lateinit var adapter: PaseadorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_paseadores_list, container, false)
        recyclerPaseadores = v.findViewById(R.id.recyclerUser)
        paseadoresRepository = PaseadorRepository.getInstance()


        return v
    }


    override fun onStart() {
        super.onStart()
        paseadoresRepository.getPaseadores { paseadoresList ->
            adapter = PaseadorAdapter(paseadoresList.toMutableList()){ position ->
                val action = PaseadoresListDirections.actionPaseadoresListToPaseadorDetail(paseadoresList[position])
                    findNavController().navigate(action)
            }
            recyclerPaseadores.layoutManager = LinearLayoutManager(context)
            recyclerPaseadores.adapter = adapter
        }

    }


}