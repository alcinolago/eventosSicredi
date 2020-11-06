package br.com.sicredi.presentation.events.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.sicredi.R
import br.com.sicredi.core.extension.Event
import br.com.sicredi.core.extension.mutableLiveData
import br.com.sicredi.core.extension.rx.observe
import br.com.sicredi.data.model.events.CheckInData
import br.com.sicredi.data.model.events.EventsResponse
import br.com.sicredi.domain.interactor.EventsInteractor
import br.com.sicredi.provider.scheduler.BaseSchedulerProvider
import br.com.sicredi.provider.scheduler.SchedulerProvider
import br.com.sicredi.provider.string.StringProvider
import br.com.sicredi.rx.DefaultCompletableObserver
import br.com.sicredi.rx.DefaultObservable

class EventDetailViewModel(
    private val eventInteractor: EventsInteractor,
    private val stringProvider: StringProvider,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    val eventMutableLiveData = MutableLiveData<EventsResponse>()
    val eventCheckIMessage = MutableLiveData<String>()
    val isLoading = mutableLiveData(false)
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val eventSelected = MutableLiveData<String>()
    val nameErrorMessage = MutableLiveData<Event<String>>()
    val emailErrorMessage = MutableLiveData<Event<String>>()

    fun getEventById(eventId: Int) {

        isLoading.value = true

        eventInteractor.getEventById(eventId).observe(schedulerProvider,
            object : DefaultObservable<EventsResponse>() {
                override fun onNext(event: EventsResponse) {
                    eventMutableLiveData.value = event
                    eventSelected.value = event.id.toString()
                    isLoading.value = false
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    isLoading.value = false
                }
            }
        )
    }

    fun validateDataEntry() {

        isLoading.value = true

        val nameInput = name.value
        val emailInput = email.value

        if (nameInput?.trim().isNullOrEmpty()) {
            isLoading.value = false
            nameErrorMessage.value =
                Event(stringProvider.getString(R.string.error_input_name))
        }

        if (emailInput?.trim().isNullOrEmpty()) {
            isLoading.value = false
            emailErrorMessage.value = Event(stringProvider.getString(R.string.error_input_email))
        }

        if (!nameInput.isNullOrEmpty() && !emailInput.isNullOrEmpty()) {
            doCheckIn()
        }
    }

    fun doCheckIn() {

        val checkInData = CheckInData(eventId = eventSelected.value, name = name.value, email = email.value)

        eventInteractor.doCheckIn(checkInData).observe(schedulerProvider,
            object : DefaultCompletableObserver() {
                override fun onComplete() {
                    super.onComplete()
                    clearData()
                    eventCheckIMessage.value = stringProvider.getString(R.string.checkin_success)
                    isLoading.value = false
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    clearData()
                    eventCheckIMessage.value = stringProvider.getString(R.string.checkin_error)
                    isLoading.value = false
                }
            }
        )
    }

    private fun clearData(){
        eventSelected.value = ""
        name.value = ""
        email.value = ""
    }
}
