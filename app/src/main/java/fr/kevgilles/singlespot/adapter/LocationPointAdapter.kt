package fr.kevgilles.singlespot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.kevgilles.singlespot.R
import fr.kevgilles.singlespot.data.LocationPoint

class LocationPointAdapter(private val locationPoints: List<LocationPoint>) :
    RecyclerView.Adapter<LocationPointAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPointId = itemView.findViewById<TextView>(R.id.tv_point_id)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.card_location_point, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val point = locationPoints[position]
        holder.tvPointId.text = point.id.toString()
    }

    override fun getItemCount(): Int {
        return locationPoints.size
    }

}