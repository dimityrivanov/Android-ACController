package com.mvp.accontrol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mvp.accontrol.viewmodel.ACUnitListViewModel

class ACUnitAdapter(val unitData: ArrayList<String>, val viewModel: ACUnitListViewModel) :
    RecyclerView.Adapter<ACUnitAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ac_unit_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = unitData.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtClimate.text = unitData[position]

        holder.txtClimate.setOnClickListener {
            viewModel.deviceBrand.value = position
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtClimate: TextView = view.findViewById(R.id.txtClimate)
    }
}