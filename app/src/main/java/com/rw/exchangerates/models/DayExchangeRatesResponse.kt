package com.rw.exchangerates.models

import com.google.gson.annotations.SerializedName

data class DayExchangeRatesResponse(
    @SerializedName("base") val base : String?,
    @SerializedName("date") val date : String?,
    @SerializedName("rates") val rates : Rates?,
    @SerializedName("success") val success : Boolean,
    @SerializedName("error") val error: Error?,
    @SerializedName("historical") val historical : Boolean?,
    @SerializedName("timestamp") val timestamp : Long?
)
