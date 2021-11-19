package com.rw.exchangerates.models

import com.google.gson.annotations.SerializedName

data class Error (
    @SerializedName("code") val code : Int,
    @SerializedName("type") val type : String,
    @SerializedName("info") val info : String
)