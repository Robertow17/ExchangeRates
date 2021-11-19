package com.rw.exchangerates.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rw.exchangerates.R
import com.rw.exchangerates.adapters.ExchangeRateAdapter
import com.rw.exchangerates.databinding.ActivityMainBinding
import com.rw.exchangerates.viewmodels.ExchangeRateActivityViewModel
import java.util.ArrayList

class ExchangeRateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var exchangeRateActivityViewModel: ExchangeRateActivityViewModel
    private lateinit var exchangeRateAdapter : ExchangeRateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.listContainer.setBackgroundResource(R.drawable.list_background)

        exchangeRateActivityViewModel = ViewModelProvider(this)[ExchangeRateActivityViewModel::class.java]
        exchangeRateActivityViewModel.init()
        exchangeRateActivityViewModel.exchangeRateRecyclerViewData!!.observe(this, Observer { exchangeRateRecyclerViewData ->
            if (exchangeRateRecyclerViewData != null) {
                exchangeRateAdapter.setData(exchangeRateRecyclerViewData)
                if(exchangeRateRecyclerViewData.size>0){
                    with(binding){
                        mainProgressBar.visibility = View.GONE
                        downloadMoreProgressBar.visibility = View.GONE
                    }
                }
            }
        })
        exchangeRateActivityViewModel.serverErrorAppearedIndicator!!.observe(this, Observer { errorAppeared ->
            if (errorAppeared.first) {
                showServerErrorAlert(errorAppeared.second)
            }
        })
        initRecyclerView()
    }

    private fun showServerErrorAlert(message : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Server error")
        builder.setMessage("API responded with message:\n$message ")
        builder.setNeutralButton("OK") { dialog, which -> Unit }
        builder.show()
    }

    private fun initRecyclerView() {
        exchangeRateAdapter = ExchangeRateAdapter(ArrayList(), this)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.exchangeRateRV.layoutManager = mLayoutManager
        binding.exchangeRateRV.itemAnimator = DefaultItemAnimator()
        binding.exchangeRateRV.adapter = exchangeRateAdapter

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!binding.exchangeRateRV.canScrollVertically(1)){
                    binding.downloadMoreProgressBar.visibility = View.VISIBLE
                    exchangeRateActivityViewModel.pullMoreData()
                }
            }
        }
        binding.exchangeRateRV.addOnScrollListener(scrollListener)
    }


}