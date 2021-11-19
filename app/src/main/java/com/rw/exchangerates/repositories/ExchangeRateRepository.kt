package com.rw.exchangerates.repositories

import androidx.lifecycle.MutableLiveData
import com.rw.exchangerates.adapters.ExchangeRateAdapter
import com.rw.exchangerates.adapters.ExchangeRateAdapter.Companion.CURRENCY_AUD
import com.rw.exchangerates.adapters.ExchangeRateAdapter.Companion.CURRENCY_CAD
import com.rw.exchangerates.adapters.ExchangeRateAdapter.Companion.CURRENCY_MXN
import com.rw.exchangerates.adapters.ExchangeRateAdapter.Companion.CURRENCY_PLN
import com.rw.exchangerates.adapters.ExchangeRateAdapter.Companion.CURRENCY_USD
import com.rw.exchangerates.adapters.ExchangeRateAdapter.Companion.VIEW_TYPE_CURRENCY_RATE
import com.rw.exchangerates.adapters.ExchangeRateAdapter.Companion.VIEW_TYPE_DATE
import com.rw.exchangerates.models.DayExchangeRatesResponse
import com.rw.exchangerates.models.RecyclerViewData
import com.rw.exchangerates.net.RetrofitApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ExchangeRateRepository {
    private val exchangeRatesRecyclerViewData : MutableLiveData<ArrayList<RecyclerViewData>> = MutableLiveData()
    private var lastUsedDay : LocalDate = LocalDate.now()
    private var recyclerViewData : ArrayList<RecyclerViewData> = ArrayList<RecyclerViewData>()
    val serverErrorMutableLiveData : MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

    init {
        serverErrorMutableLiveData.postValue(Pair(false, ""))
    }

    fun getRecyclerViewData() : MutableLiveData<ArrayList<RecyclerViewData>> {
        if(exchangeRatesRecyclerViewData.value == null){
            exchangeRatesRecyclerViewData.postValue(recyclerViewData)
            getLatestExchangeRatesRecyclerViewData()
        }
        return exchangeRatesRecyclerViewData
    }

    private fun getRequestsList(now : LocalDate, minusDays : Long) : ArrayList<Observable<DayExchangeRatesResponse>> {
        val requests = ArrayList<Observable<DayExchangeRatesResponse>>()
        for(date in now.rangeTo(now.minusDays(minusDays))) {
            requests.add(RetrofitApi.myApi.getDayExchangesRate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            lastUsedDay = date
        }
        return requests
    }

    private fun getLatestExchangeRatesRecyclerViewData() {
        val requests = getRequestsList(LocalDate.now(), 4)
        downloadDataFromServer(requests)
    }


     fun getEarlierExchangeRatesRecyclerViewData() {
         val requests = getRequestsList(lastUsedDay.minusDays(1), 1)
         downloadDataFromServer(requests)
    }

    private fun downloadDataFromServer(requests : ArrayList<Observable<DayExchangeRatesResponse>>) {
        val subscribe = Observable
            .zip(requests) {
                    results ->
                val list = mutableListOf<DayExchangeRatesResponse>()
                for (res in results){
                    list.add(res as DayExchangeRatesResponse)
                }
                list
            }
            .subscribeOn(Schedulers.io())
            .subscribe({
                    list ->
                val listOfRows = arrayListOf<RecyclerViewData>()
                for (item in list) {
                    if (!item.success){
                        serverErrorMutableLiveData.postValue(item.error?.let { Pair(true, it.info) })
                        break
                    }
                    listOfRows.addAll(convertResponseToRVData(item))
                }
                recyclerViewData.addAll(listOfRows)
                exchangeRatesRecyclerViewData.postValue(recyclerViewData)
            }) {e ->
                serverErrorMutableLiveData.postValue(Pair(true, e.toString()))
            }
    }

    fun convertResponseToRVData(response: DayExchangeRatesResponse) : ArrayList<RecyclerViewData> {
        val list = arrayListOf<RecyclerViewData>()
        if(!response.success) {return list}
        with(response) {
            list.add(RecyclerViewData(VIEW_TYPE_DATE, dateToDatePretty(date!!), null, null))
            list.add(RecyclerViewData(VIEW_TYPE_CURRENCY_RATE, dateToDatePretty(date), rates!!.usd, CURRENCY_USD))
            list.add(RecyclerViewData(VIEW_TYPE_CURRENCY_RATE, dateToDatePretty(date), rates.aud, CURRENCY_AUD))
            list.add(RecyclerViewData(VIEW_TYPE_CURRENCY_RATE, dateToDatePretty(date), rates.cad, CURRENCY_CAD))
            list.add(RecyclerViewData(VIEW_TYPE_CURRENCY_RATE, dateToDatePretty(date), rates.pln, CURRENCY_PLN))
            list.add(RecyclerViewData(VIEW_TYPE_CURRENCY_RATE, dateToDatePretty(date), rates.mxn, CURRENCY_MXN))
        }
        return list
    }

    fun dateToDatePretty(date: String): String {
        val dateSplit = date.split("-")
        val localDate = LocalDate.of(dateSplit[0].toInt(), dateSplit[1].toInt(), dateSplit[2].toInt())
        val pattern = "EEEE, d MMMM yyyy"
        return localDate.format(DateTimeFormatter.ofPattern(pattern))
    }

    companion object {
        private var instance: ExchangeRateRepository? = null
        @JvmStatic
        fun getInstance(): ExchangeRateRepository? {
            if (instance == null) {
                instance = ExchangeRateRepository()
            }
            return instance
        }
    }

    operator fun ClosedRange<LocalDate>.iterator() : Iterator<LocalDate>{
        return object: Iterator<LocalDate> {
            private var next = this@iterator.start
            private val finalElement = this@iterator.endInclusive
            private var hasNext = !next.isBefore(this@iterator.endInclusive)
            override fun hasNext(): Boolean = hasNext

            override fun next(): LocalDate {
                val value = next
                if(value == finalElement) {
                    hasNext = false
                }
                else {
                    next = next.minusDays(1)
                }
                return value
            }
        }
    }

}


