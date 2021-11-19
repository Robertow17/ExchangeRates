package com.rw.exchangerates.models


data class RecyclerViewData (
    val viewType : Int,
    val date : String,
    val rate : Double?,
    val currency : String?
)
