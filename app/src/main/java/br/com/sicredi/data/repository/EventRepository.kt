package br.com.sicredi.data.repository

import br.com.sicredi.data.model.events.CheckInData
import br.com.sicredi.data.model.events.EventsResponse
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventRepository {

    @GET("events")
    fun getEvents(): Observable<List<EventsResponse>>

    @GET("events/{eventId}")
    fun getEventsById(@Path("eventId") eventId: Int): Observable<EventsResponse>

    @POST("checkin")
    fun doCheckIn(@Body checkinData: CheckInData): Completable
}
