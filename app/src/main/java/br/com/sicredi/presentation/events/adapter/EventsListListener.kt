package br.com.sicredi.presentation.events.adapter

import android.view.View

interface EventsListListener {
    fun eventsOnClick(v: View?, position: Int)
}
