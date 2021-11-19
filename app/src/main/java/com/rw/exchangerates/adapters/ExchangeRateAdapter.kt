package com.rw.exchangerates.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rw.exchangerates.R
import com.rw.exchangerates.databinding.CurrencyRateListRowBinding
import com.rw.exchangerates.databinding.DateListRowBinding
import com.rw.exchangerates.models.RecyclerViewData
import com.rw.exchangerates.views.ExchangeRateDetailsActivity
import java.text.SimpleDateFormat
import java.util.*

class ExchangeRateAdapter (private var exchangeRateRecyclerViewData: List<RecyclerViewData>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_DATE = 1
        const val VIEW_TYPE_CURRENCY_RATE = 2

        const val CURRENCY_USD = "USD"
        const val CURRENCY_AUD = "AUD"
        const val CURRENCY_CAD = "CAD"
        const val CURRENCY_PLN = "PLN"
        const val CURRENCY_MXN = "MXN"

        const val USD_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Flag_of_the_United_States.svg/1920px-Flag_of_the_United_States.svg.png"
        const val AUD_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b9/Flag_of_Australia.svg/360px-Flag_of_Australia.svg.png"
        const val CAD_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Flag_of_Canada_%28Pantone%29.svg/1920px-Flag_of_Canada_%28Pantone%29.svg.png"
        const val PLN_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e9/Flag_of_Poland_%28normative%29.svg/360px-Flag_of_Poland_%28normative%29.svg.png"
        const val MXN_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fc/Flag_of_Mexico.svg/360px-Flag_of_Mexico.svg.png"
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DateListRowBinding.bind(view)
    }

    inner class CurrencyRateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = CurrencyRateListRowBinding.bind(view)
    }

    fun setData(data: List<RecyclerViewData>) {
        this.exchangeRateRecyclerViewData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_DATE) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.date_list_row, parent, false)

            return DateViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.currency_rate_list_row, parent, false)

            val holder = CurrencyRateViewHolder(itemView)

            itemView.setOnClickListener {
                val pos = holder.absoluteAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val myIntent = Intent(parent.context, ExchangeRateDetailsActivity::class.java)
                    val data = exchangeRateRecyclerViewData[pos]
                    with(data) {
                        myIntent.putExtra("Date", date)
                        myIntent.putExtra("Currency", currency)
                        myIntent.putExtra("Rate", rate)
                    }
                    parent.context.startActivity(myIntent)
                }
            }
            return holder
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recyclerViewData = exchangeRateRecyclerViewData[position]
        if(recyclerViewData.viewType== VIEW_TYPE_DATE){
            (holder as DateViewHolder).binding.dateTV.text = recyclerViewData.date   //
        }
        else{
            with ((holder as CurrencyRateViewHolder).binding) {
                currencyTV.text = recyclerViewData.currency
                rateTV.text = recyclerViewData.rate.toString()
                when (recyclerViewData.currency) {
                    CURRENCY_USD -> {
                        currencyFullName.text = context.resources.getString(R.string.usd_name)
                        Glide.with(context).load(USD_IMAGE).into(flagImage)
                    }
                    CURRENCY_CAD -> {
                        currencyFullName.text = context.resources.getString(R.string.cad_name)
                        Glide.with(context).load(CAD_IMAGE).into(flagImage)
                    }
                    CURRENCY_PLN -> {
                        currencyFullName.text = context.resources.getString(R.string.pln_name)
                        Glide.with(context).load(PLN_IMAGE).into(flagImage)
                    }
                    CURRENCY_AUD -> {
                        currencyFullName.text = context.resources.getString(R.string.aud_name)
                        Glide.with(context).load(AUD_IMAGE).into(flagImage)
                    }
                    CURRENCY_MXN -> {
                        currencyFullName.text = context.resources.getString(R.string.mxn_name)
                        Glide.with(context).load(MXN_IMAGE).into(flagImage)
                    }
                    else -> {}
                }

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return exchangeRateRecyclerViewData[position].viewType
    }

    override fun getItemCount(): Int {
        return exchangeRateRecyclerViewData.size
    }
}