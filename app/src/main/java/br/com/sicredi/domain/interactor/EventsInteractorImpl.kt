package br.com.sicredi.domain.interactor

import br.com.sicredi.data.model.events.CheckInData
import br.com.sicredi.data.model.events.EventsResponse
import br.com.sicredi.data.repository.EventRepository
import io.reactivex.Completable
import io.reactivex.Observable

class EventsInteractorImpl(private val eventRepository: EventRepository) :
    EventsInteractor {

    override fun getEvents(): Observable<List<EventsResponse>> {
        return eventRepository.getEvents()
    }

    override fun getEventById(eventId: Int): Observable<EventsResponse> {
        return eventRepository.getEventsById(eventId)
    }

    override fun doCheckIn(checkInData: CheckInData): Completable {
        return eventRepository.doCheckIn(checkInData)
    }
}