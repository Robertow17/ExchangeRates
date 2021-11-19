package com.rw.exchangerates.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.rw.exchangerates.adapters.ExchangeRateAdapter
import com.rw.exchangerates.databinding.ActivityExchangeRateDetailsBinding

class ExchangeRateDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExchangeRateDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangeRateDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setValuesToViews(intent)
    }

    private fun setValuesToViews(intent: Intent) {
        var imageURL = ""
        with (intent) {
            binding.chosenRateDate.text = getStringExtra("Date")?.newLineAfterComma() ?: ""
            val rateText = "${getDoubleExtra("Rate",0.00)} ${getStringExtra("Currency")}"
            binding.otherCurrencyRateTV.text = rateText
            when (getStringExtra("Currency")) {
                ExchangeRateAdapter.CURRENCY_USD -> {
                    imageURL = ExchangeRateAdapter.USD_IMAGE
                }
                ExchangeRateAdapter.CURRENCY_CAD -> {
                    imageURL = ExchangeRateAdapter.CAD_IMAGE
                }
                ExchangeRateAdapter.CURRENCY_PLN -> {
                    imageURL = ExchangeRateAdapter.PLN_IMAGE
                }
                ExchangeRateAdapter.CURRENCY_AUD -> {
                    imageURL = ExchangeRateAdapter.AUD_IMAGE
                }
                ExchangeRateAdapter.CURRENCY_MXN -> {
                    imageURL = ExchangeRateAdapter.MXN_IMAGE
                }
                else -> {}
            }
        }
        Glide.with(this).load(imageURL).into(binding.otherCurrencyFlag)
    }


}

fun String.newLineAfterComma(): String {
    var result = ""
    val split = this.split(",")
    for(i in split.indices){
        result = if(i+1!=split.size)
            "$result${split[i]},\n"
        else
            "$result${split[i]}"
    }
    return result
}