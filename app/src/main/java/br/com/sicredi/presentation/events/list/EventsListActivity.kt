package br.com.sicredi.presentation.events.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.sicredi.Constants
import br.com.sicredi.R
import br.com.sicredi.core.util.DialogProgressBar
import br.com.sicredi.data.model.events.EventsResponse
import br.com.sicredi.presentation.events.adapter.EventListAdapter
import br.com.sicredi.presentation.events.adapter.EventsListListener
import br.com.sicredi.presentation.events.details.EventDetailActivity
import kotlinx.android.synthetic.main.activity_list_events.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsListActivity : AppCompatActivity(), EventsListListener {

    private val eventsListViewModel: EventsListViewModel by viewModel()
    private var eventsList = ArrayList<EventsResponse>()
    private lateinit var eventListAdapter: EventListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_events)

        observeViewModel()
        configureRecyclerView()
        configureAdapterRecyclerView()
        setupToolbar(Constants.TOOLBAR_EVENTS_LIST)
    }

    override fun onResume() {
        super.onResume()
        eventsListViewModel.getEvents()
    }

    private fun setupToolbar(title: String) {

        setSupportActionBar(toolbar_main)
        supportActionBar?.title = title
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    private fun observeViewModel() {

        eventsListViewModel.eventMutableLiveData.observe(this, Observer { eventList ->
            eventsList.clear()
            eventsList.addAll(eventList)
            eventListAdapter.setItems(eventList)
        })

        eventsListViewModel.isLoading.observe(this, {
            it?.let { loading ->
                if (loading) DialogProgressBar.show(this) else DialogProgressBar.dismiss()
            }
        })
    }

    private fun configureRecyclerView() {

        val layout = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL, false
        )

        recyclerViewEvents.setHasFixedSize(true)
        recyclerViewEvents.layoutManager = layout
    }

    private fun configureAdapterRecyclerView() {
        eventListAdapter = EventListAdapter(eventsList, this, this)
        recyclerViewEvents.adapter = eventListAdapter
    }

    override fun eventsOnClick(v: View?, position: Int) {
        val bookingIntent = Intent(this@EventsListActivity, EventDetailActivity::class.java)
        val bundle = Bundle()
        val eventId = eventsList[position].id
        bundle.putInt(Constants.EVENT_DETAIL, eventId!!)
        bookingIntent.putExtras(bundle)
        startActivity(bookingIntent)
    }
}
