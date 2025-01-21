package com.viewsandevents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocationAdapter(
    private var locations: MutableList<Location>,
    private val onItemClick: (Location) -> Unit,          // For item clicks
    private val onRemoveBookmark: (Location) -> Unit      // For remove bookmark clicks
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val latitudeText: TextView = itemView.findViewById(R.id.latitude_text)
        val longitudeText: TextView = itemView.findViewById(R.id.longitude_text)
        val removeBookmarkIcon: ImageView = itemView.findViewById(R.id.remove_bookmark_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.latitudeText.text = "Latitude: ${location.latitude}"
        holder.longitudeText.text = "Longitude: ${location.longitude}"

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClick(location)
        }

        // Handle remove bookmark click
        holder.removeBookmarkIcon.setOnClickListener {
            onRemoveBookmark(location)
        }
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    // Method to update the list
    fun updateData(newLocations: MutableList<Location>) {
        locations = newLocations
        notifyDataSetChanged()
    }
}
