package com.example.fin.customAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fin.R
import com.example.fin.realm.RealmModel

/**
 * `RealmAdapter` is a custom RecyclerView adapter for displaying a list of `RealmModel` items in a RecyclerView.
 *
 * @param context      The application context.
 * @param realmItemList The list of `RealmModel` items to be displayed in the RecyclerView.
 */
class RealmAdapter(private val context: Context?, private val realmItemList: List<RealmModel>) :
    RecyclerView.Adapter<RealmViewHolder>() {

    /**
     * Called when a new ViewHolder is needed, typically when the RecyclerView is initialized.
     *
     * @param parent   The parent ViewGroup into which the new View will be added.
     * @param viewType The type of the new View.
     * @return A new `RealmViewHolder` that holds the View for a single list item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealmViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false)
        return RealmViewHolder(view)
    }

    /**
     * Returns the total number of items in the dataset held by the adapter.
     *
     * @return The total number of `RealmModel` items in the `realmItemList`.
     */
    override fun getItemCount(): Int {
        return realmItemList.count()
    }

    /**
     * Called by RecyclerView to display the data at a specific position.
     *
     * @param holder   The `RealmViewHolder` to bind the data to.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: RealmViewHolder, position: Int) {
        // Set the name, age, and city fields of the ViewHolder to the corresponding values from the data
        holder.name.text = realmItemList[position].name
        holder.age.text = realmItemList[position].age.toString()
        holder.city.text = realmItemList[position].city

        // Log information for debugging purposes
        Log.d("mybug", "onBindViewHolder: ${holder.name.text}")
        Log.d("mybug", "onBindViewHolder: ${holder.age.text}")
        Log.d("mybug", "onBindViewHolder: ${holder.city.text}")
    }
}
