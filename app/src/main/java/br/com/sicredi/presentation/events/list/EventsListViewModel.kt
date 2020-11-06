package br.com.sicredi.presentation.events.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.sicredi.core.extension.mutableLiveData
import br.com.sicredi.core.extension.rx.observe
import br.com.sicredi.data.model.events.EventsResponse
import br.com.sicredi.domain.interactor.EventsInteractor
import br.com.sicredi.provider.scheduler.BaseSchedulerProvider
import br.com.sicredi.rx.DefaultObservable

class EventsListViewModel(
    private val eventInteractor: EventsInteractor,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    var eventMutableLiveData = MutableLiveData<List<EventsResponse>>()
    val isLoading = mutableLiveData(false)

    fun getEvents() {

        isLoading.value = true

        eventInteractor.getEvents().observe(schedulerProvider,
            object : DefaultObservable<List<EventsResponse>>() {
                override fun onNext(eventsList: List<EventsResponse>) {
                    eventMutableLiveData.value = eventsList
                    isLoading.value = false
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    isLoading.value = false
                }
            }
        )
    }
}
