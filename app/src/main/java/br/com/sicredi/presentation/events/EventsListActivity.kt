package br.com.sicredi.presentation.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.sicredi.R

class EventsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_events)
    }
}