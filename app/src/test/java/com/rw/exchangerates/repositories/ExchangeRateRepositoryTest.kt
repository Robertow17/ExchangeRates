package com.rw.exchangerates.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rw.exchangerates.adapters.ExchangeRateAdapter
import com.rw.exchangerates.models.DayExchangeRatesResponse
import com.rw.exchangerates.models.Rates
import com.rw.exchangerates.models.RecyclerViewData
import com.rw.exchangerates.net.RetrofitApi
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.time.LocalDate


class ExchangeRateRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testDateToDatePretty() {
        assertEquals(ExchangeRateRepository().dateToDatePretty("2021-11-19"), "piątek, 19 listopada 2021")
        assertEquals(ExchangeRateRepository().dateToDatePretty("2020-03-17"), "wtorek, 17 marca 2020")
        assertEquals(ExchangeRateRepository().dateToDatePretty("2001-06-24"), "niedziela, 24 czerwca 2001")
        assertEquals(ExchangeRateRepository().dateToDatePretty("2015-07-01"), "środa, 1 lipca 2015")
        assertEquals(ExchangeRateRepository().dateToDatePretty("2007-01-07"), "niedziela, 7 stycznia 2007")
        assertEquals(ExchangeRateRepository().dateToDatePretty("2021-09-13"), "poniedziałek, 13 września 2021")
    }

    @Test
    fun testConvertResponseToRVData() {
        val response1 = DayExchangeRatesResponse("EUR", "2021-11-19", Rates(1.125478, 1.589687, 1.478253, 4.701298, 21.456781),true, null,true, 1637084223)
        val response1Result = ArrayList<RecyclerViewData>()
        response1Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_DATE, "piątek, 19 listopada 2021", null, null))
        response1Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "piątek, 19 listopada 2021", 1.125478,ExchangeRateAdapter.CURRENCY_USD))
        response1Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE,"piątek, 19 listopada 2021", 1.589687,ExchangeRateAdapter.CURRENCY_AUD))
        response1Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "piątek, 19 listopada 2021", 1.478253,ExchangeRateAdapter.CURRENCY_CAD))
        response1Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "piątek, 19 listopada 2021", 4.701298,ExchangeRateAdapter.CURRENCY_PLN))
        response1Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "piątek, 19 listopada 2021", 21.456781,ExchangeRateAdapter.CURRENCY_MXN))

        Assert.assertArrayEquals(ExchangeRateRepository().convertResponseToRVData(response1).toArray(), response1Result.toArray())

        val response2 = DayExchangeRatesResponse("EUR", "2020-03-17", Rates(1.584789, 1.632589, 1.325896, 4.745896, 21.456123),true, null,true, 1637587223)
        val response2Result = ArrayList<RecyclerViewData>()
        response2Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_DATE, "wtorek, 17 marca 2020", null, null))
        response2Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "wtorek, 17 marca 2020", 1.584789,ExchangeRateAdapter.CURRENCY_USD))
        response2Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE,"wtorek, 17 marca 2020", 1.632589,ExchangeRateAdapter.CURRENCY_AUD))
        response2Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "wtorek, 17 marca 2020", 1.325896,ExchangeRateAdapter.CURRENCY_CAD))
        response2Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "wtorek, 17 marca 2020", 4.745896,ExchangeRateAdapter.CURRENCY_PLN))
        response2Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "wtorek, 17 marca 2020", 21.456123,ExchangeRateAdapter.CURRENCY_MXN))

        Assert.assertArrayEquals(ExchangeRateRepository().convertResponseToRVData(response2).toArray(), response2Result.toArray())

        val response3 = DayExchangeRatesResponse("EUR", "2001-06-24", Rates(1.025896, 1.214568, 1.089563, 4.301458, 24.456781),true, null,true, 1237084223)
        val response3Result = ArrayList<RecyclerViewData>()
        response3Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_DATE, "niedziela, 24 czerwca 2001", null, null))
        response3Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "niedziela, 24 czerwca 2001", 1.025896,ExchangeRateAdapter.CURRENCY_USD))
        response3Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE,"niedziela, 24 czerwca 2001", 1.214568,ExchangeRateAdapter.CURRENCY_AUD))
        response3Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "niedziela, 24 czerwca 2001", 1.089563,ExchangeRateAdapter.CURRENCY_CAD))
        response3Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "niedziela, 24 czerwca 2001", 4.301458,ExchangeRateAdapter.CURRENCY_PLN))
        response3Result.add(RecyclerViewData(ExchangeRateAdapter.VIEW_TYPE_CURRENCY_RATE, "niedziela, 24 czerwca 2001", 24.456781,ExchangeRateAdapter.CURRENCY_MXN))

        Assert.assertArrayEquals(ExchangeRateRepository().convertResponseToRVData(response3).toArray(), response3Result.toArray())

        val response4 = DayExchangeRatesResponse(null, null, null,false, com.rw.exchangerates.models.Error(100, "errorType", "Server error appeared"),true, 1637084223)
        val response4Result = ArrayList<RecyclerViewData>()

        Assert.assertArrayEquals(ExchangeRateRepository().convertResponseToRVData(response4).toArray(), response4Result.toArray())
    }
}
