package com.rw.exchangerates.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rw.exchangerates.models.RecyclerViewData
import com.rw.exchangerates.repositories.ExchangeRateRepository
import com.rw.exchangerates.repositories.ExchangeRateRepository.Companion.getInstance

class ExchangeRateActivityViewModel : ViewModel() {
    private var mExchangeRateRecyclerViewData: MutableLiveData<ArrayList<RecyclerViewData>>? = null
    private var mRepo: ExchangeRateRepository? = null
    private var mServerErrorAppeared: MutableLiveData<Pair<Boolean, String>>? = null

    fun init() {
        if (mExchangeRateRecyclerViewData != null) return
        mRepo = getInstance()
        mExchangeRateRecyclerViewData = mRepo!!.getRecyclerViewData()
        mServerErrorAppeared = mRepo!!.serverErrorMutableLiveData
    }

    val exchangeRateRecyclerViewData: LiveData<ArrayList<RecyclerViewData>>?
        get() = mExchangeRateRecyclerViewData

    val serverErrorAppearedIndicator: LiveData<Pair<Boolean, String>>?
        get() = mServerErrorAppeared

    fun pullMoreData() {
        mRepo?.getEarlierExchangeRatesRecyclerViewData()
    }

}