package com.example.convertitorediunita.ui.model

data class MoneyUtil(val rates: Rates?) {
    data class Rates(
        val eUR: Double?,
        val gBP: Double?
    )
}
