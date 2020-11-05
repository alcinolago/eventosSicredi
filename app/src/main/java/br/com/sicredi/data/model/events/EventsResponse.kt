package br.com.sicredi.data.model.events

import com.google.gson.annotations.SerializedName

data class EventsResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("price") var price: Double? = null,
    @SerializedName("title") var title: String? = null
)