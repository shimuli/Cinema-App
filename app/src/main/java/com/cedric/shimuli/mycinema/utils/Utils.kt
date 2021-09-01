package com.cedric.shimuli.mycinema.utils

import android.content.Context
import android.content.Intent
import com.cedric.shimuli.mycinema.model.MoviesModel
import java.text.DecimalFormat
import java.text.NumberFormat

object Utils {
    fun sendDataActivity(
        c: Context, movie: MoviesModel?, clazz: Class<*>?) {
        val i = Intent(c, clazz)
        i.putExtra("Data", movie)
        c.startActivity(i)
    }

     fun fetchMovieDetails(intent: Intent, c: Context?): MoviesModel? {
        try {
            return intent.getSerializableExtra("Data") as MoviesModel
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun convertCurrency(productPrice: Double?): String {
        //val productPrice: Double = pro_price.toDouble()
        val priceFormat = NumberFormat.getCurrencyInstance()
        val priceFormatSymbols = (priceFormat as DecimalFormat).decimalFormatSymbols
        priceFormatSymbols.currencySymbol = "Ksh. "
        priceFormat.decimalFormatSymbols = priceFormatSymbols
        return priceFormat.format(productPrice).trim { it <= ' ' }
    }

}