package com.example.myapplication.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.R

class FiltroSpinnerAdapter(context: Context, resource: Int, private val data: List<String>) :
    ArrayAdapter<String>(context, resource, data) {

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        val textView = view.findViewById<TextView>(android.R.id.text1)

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

        return view
    }
}
