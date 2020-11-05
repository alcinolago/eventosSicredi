package br.com.sicredi.presentation.events.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sicredi.R
import br.com.sicredi.core.util.SafeClickListener
import br.com.sicredi.data.model.events.EventsResponse

class EventListAdapter(
    private var items: MutableList<EventsResponse>,
    private val context: Context,
    private val onClickListener: EventsListListener
) : RecyclerView.Adapter<EventsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_event, parent, false)
        return EventsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventsListViewHolder, position: Int) {
        holder.bind(items[position], position, onClickListener)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    fun setItems(eventsList: List<EventsResponse>) {
        items.clear()
        items.addAll(eventsList)
        notifyDataSetChanged()
    }
}
