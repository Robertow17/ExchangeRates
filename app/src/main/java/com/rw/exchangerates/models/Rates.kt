package com.rw.exchangerates.models

import com.google.gson.annotations.SerializedName

data class Rates (
    @SerializedName("USD") val usd : Double,
    @SerializedName("AUD") val aud : Double,
    @SerializedName("CAD") val cad : Double,
    @SerializedName("PLN") val pln : Double,
    @SerializedName("MXN") val mxn : Double
)
