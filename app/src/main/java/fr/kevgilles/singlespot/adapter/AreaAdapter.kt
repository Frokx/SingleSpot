package fr.kevgilles.singlespot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fr.kevgilles.singlespot.R

class AreaAdapter(private val areaNames: List<String>, private val onClickListener: View.OnClickListener) :
    RecyclerView.Adapter<AreaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAreaName = itemView.findViewById<TextView>(R.id.tv_area_name)!!
        val cardView = itemView.findViewById<CardView>(R.id.card_view_area)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.card_area, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val area = areaNames[position]
        holder.tvAreaName.text = area
        holder.cardView.tag = position
        holder.cardView.setOnClickListener(onClickListener)
    }

    override fun getItemCount(): Int {
        return areaNames.size
    }

    fun getItemAt(position: Int): String {
        return areaNames[position]
    }

}