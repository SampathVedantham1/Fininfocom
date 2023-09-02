package com.example.fin.customAdapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fin.R

/**
 * `RealmViewHolder` is a custom ViewHolder class that represents an item view within a RecyclerView for displaying `RealmModel` data.
 *
 * @param itemView The View object representing the item view layout.
 */
class RealmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Declare TextViews to hold the UI elements from the item view layout
    var name: TextView = itemView.findViewById(R.id.name_item)
    var city: TextView = itemView.findViewById(R.id.city_item)
    var age: TextView = itemView.findViewById(R.id.age_item)
}
