package com.rw.exchangerates.net

import retrofit2.http.GET
import retrofit2.http.Path
import com.rw.exchangerates.models.DayExchangeRatesResponse
import io.reactivex.Observable

interface Api {

    companion object {
        const val BASE_URL = "http://data.fixer.io/api/"
    }

    @GET("{date}?access_key=e431f235810f9496816b34a1ed57b1c9&symbols=USD,AUD,CAD,PLN,MXN&format=1")
    fun getDayExchangesRate(@Path("date") date: String) : Observable<DayExchangeRatesResponse>


}