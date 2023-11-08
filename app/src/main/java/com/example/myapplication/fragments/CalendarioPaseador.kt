package com.example.myapplication.fragments

import android.app.AlertDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.R.id.calendarPaseador
import com.example.myapplication.entities.EstadoEnum
import com.example.myapplication.entities.MedioDePagoEnum
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.User
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession.user
import com.example.myapplication.repository.PaseoRepository
import com.google.android.material.snackbar.Snackbar

class CalendarioPaseador : Fragment() {

    lateinit var v : View
    lateinit var calendar : CalendarView
    lateinit var selectedDate: TextView
    lateinit var btnConfirm : Button
    lateinit var spinner : Spinner
    lateinit var mediosDePago : Spinner
    lateinit var mpAdaptador: ArrayAdapter<String>
    lateinit var switchExclusivo: Switch
    lateinit var adaptador: ArrayAdapter<String>
    var medioDePagoText: String? = null
    val programadoRepository = PaseoRepository.getInstance()
    val horarios = mutableListOf("10:00hs", "11:00hs", "12:00hs")
    private val originalHorarios = mutableListOf("10:00hs", "11:00hs", "12:00hs")
    val mediosDePagoList = mutableListOf("Efectivo", "Transferencia")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_calendario_paseador, container, false)
        calendar = v.findViewById(calendarPaseador)
        selectedDate = v.findViewById(R.id.selectedDate)
        btnConfirm = v.findViewById(R.id.btnConfirm)
        switchExclusivo = v.findViewById(R.id.switchPaseoExclusivo)

        mediosDePago = v.findViewById(R.id.medioDePago)
        mpAdaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mediosDePagoList)
        mpAdaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mediosDePago.adapter = mpAdaptador
        spinner = v.findViewById(R.id.listaHorarios)
        adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, horarios)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador

        return v
    }

    override fun onStart() {
        super.onStart()
        user = user as User

        val currentDateCalendar = Calendar.getInstance()
        val maxSelectableDateCalendar = Calendar.getInstance()

        maxSelectableDateCalendar.add(Calendar.DAY_OF_MONTH, 7)
        val maxSelectableDateInMillis = maxSelectableDateCalendar.timeInMillis

        calendar.minDate = currentDateCalendar.timeInMillis
        calendar.maxDate = maxSelectableDateInMillis

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDateCalendar =
                "$dayOfMonth/${month + 1}/$year" // Suma 1 al mes ya que los meses comienzan en 0
            selectedDate.text = "$selectedDateCalendar"

            if (!switchExclusivo.isChecked){
                programadoRepository.getPaseos { paseos ->
                    val fechasBuscadas = paseos.filter { it.fecha.substring(0, it.fecha.length - 7).trim() == selectedDateCalendar}
                    val fechas = fechasBuscadas.map { it.fecha.substring(it.fecha.length - 7) }
                    val fechasAgrupadas = fechas.groupingBy { it }.eachCount()
                    val fechasRepetidas5Veces = fechasAgrupadas.filter { it.value == 5 }.keys.toList()
                    if (fechasRepetidas5Veces.isNotEmpty()) {
                        val fechasFiltradas = horarios.filter { it !in fechasRepetidas5Veces }
                        val adaptadorActual = spinner.adapter as ArrayAdapter<String>
                        adaptadorActual.clear()
                        adaptadorActual.addAll(fechasFiltradas)
                        adaptadorActual.notifyDataSetChanged()
                    } else {
                        val adaptadorActual = spinner.adapter as ArrayAdapter<String>
                        adaptadorActual.clear()
                        adaptadorActual.addAll(originalHorarios)
                        adaptadorActual.notifyDataSetChanged()
                    }
                }
            }else {
                programadoRepository.getPaseos { paseos ->
                    val filtrados = paseos.filter {
                        it.fecha.substring(0, 10).trim() == "$dayOfMonth/${(month + 1)}/${year}"
                    }
                    if (filtrados.isNotEmpty()) {
                        val horariosAEliminar = filtrados.map { it.fecha.substring(it.fecha.length - 7)}
                        val horariosFiltrados = horarios.filter { hora ->
                            !horariosAEliminar.any { horaEliminar ->
                                hora.equals(horaEliminar)
                            }
                        }
                        val adaptadorActual = spinner.adapter as ArrayAdapter<String>
                        adaptadorActual.clear()
                        adaptadorActual.addAll(horariosFiltrados)
                        adaptadorActual.notifyDataSetChanged()
                    }
                }
            }


        }
        mediosDePago.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                medioDePagoText = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Manejar el caso cuando no se ha seleccionado ningún elemento
            }
        }
        btnConfirm.setOnClickListener {
            val selectedDateString = selectedDate.text.toString()
            val selectedTime = spinner.selectedItem.toString()
            if (selectedDateString.isNotEmpty() && selectedTime.isNotEmpty() && !medioDePagoText.isNullOrEmpty()) {
                val dateTimeString = "$selectedDateString $selectedTime"
                showConfirmationDialog(user, dateTimeString)

            } else {
                Snackbar.make(v, "Debe seleccionar una fecha válida", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConfirmationDialog(user: UserAbstract, dateTimeString: String) {
        val builder = AlertDialog.Builder(requireContext())
        val paseador = CalendarioPaseadorArgs.fromBundle(requireArguments()).Paseador
        val paseoProgramadoRepository = PaseoRepository.getInstance()
        builder.setTitle("Confirmación")
        builder.setMessage("¿Confirma la fecha y hora seleccionadas?")
        builder.setPositiveButton("Sí") { _, _ ->
            paseador.tarifa = if(switchExclusivo.isChecked) {
                paseador.tarifa * 5
            }else {
                paseador.tarifa
            }
            val paseoProgramado = PaseoProgramado(paseador, user, dateTimeString, EstadoEnum.PENDIENTE,0)
            paseoProgramado.medioDePago = if(medioDePagoText == "EFECTIVO") {
                MedioDePagoEnum.EFECTIVO
            } else {
                MedioDePagoEnum.TRANSFERENCIA
            }
            paseoProgramadoRepository.addPaseo(paseoProgramado)
            Snackbar.make(v, "Fecha y hora asignadas con éxito", Snackbar.LENGTH_SHORT).show()
            val action = CalendarioPaseadorDirections.actionCalendarioPaseadorToPaseadoresList()
            findNavController().navigate(action)
        }

        builder.setNegativeButton("No") { _, _ ->
            // Usuario canceló, no se realiza la actualización
        }

        val dialog = builder.create()
        dialog.show()
    }

}

