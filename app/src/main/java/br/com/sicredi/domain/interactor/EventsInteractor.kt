package br.com.sicredi.domain.interactor

import br.com.sicredi.data.model.events.CheckInData
import br.com.sicredi.data.model.events.EventsResponse
import io.reactivex.Completable
import io.reactivex.Observable

interface EventsInteractor {
    fun getEvents(): Observable<List<EventsResponse>>
    fun getEventById(eventId: Int): Observable<EventsResponse>
    fun doCheckIn(checkInData: CheckInData): Completable
}