package br.com.sicredi.presentation.events.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.sicredi.R
import br.com.sicredi.core.extension.setSafeOnClickListener
import br.com.sicredi.core.util.SafeClickListener
import br.com.sicredi.core.util.Util.getDateTime
import br.com.sicredi.data.model.events.EventsResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_event.view.*

class EventsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        event: EventsResponse,
        position: Int,
        listener: EventsListListener
    ) {
        itemView.eventLayout.setOnClickListener() { listener.eventsOnClick(itemView, position) }
        Picasso.get()
            .load(event.image)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(itemView.eventImage)
        itemView.eventTitle.text = event.title
        itemView.eventDate.text = getDateTime(event.date!!)
    }
}
