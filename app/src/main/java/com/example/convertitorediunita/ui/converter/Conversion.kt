package com.example.convertitorediunita.ui.converter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.convertitorediunita.MoneyUtilResultReceiver
import com.example.convertitorediunita.RetrofitService
import com.example.convertitorediunita.exhaustive
import kotlin.math.roundToInt

const val ONE_HUNDRED = 100.0
const val ONE_THOUSAND = 1000.0
const val FIVE = 5
const val NINE = 9
const val THIRTY_TWO = 32
const val THIRTY_SIX = 36

class Conversion(context: Context) {

    private val retrofitService = RetrofitService(context)

    fun getFahrenheitFromCelsius(celsius: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveFahrenheitFromCel(
            (celsius.toDouble() * NINE / FIVE + THIRTY_TWO)
                .times(ONE_HUNDRED).roundToInt().div(ONE_HUNDRED).toString()
        )
    }

    fun getCelsiusFromFahrenheit(fahrenait: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveCelsiusFromFahrenheit(
            ((fahrenait.toDouble() - THIRTY_TWO) * FIVE / NINE)
                .times(ONE_HUNDRED).roundToInt().div(ONE_HUNDRED).toString()
        )
    }

    fun getKgFromG(kilo: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveKgFromG(
            kilo.toDouble().div(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
                .div(ONE_HUNDRED * ONE_HUNDRED).toString()
        )
    }

    fun getGFromKg(grams: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveGFromKg(
            grams.toDouble().times(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
                .div(ONE_HUNDRED * ONE_HUNDRED).toString()
        )
    }

    fun loadEuro(event: String, state: MutableLiveData<ConvertState>) {
        state.value = ConvertState.InProgress

        try {
            retrofitService.getDataFromApi(object : MoneyUtilResultReceiver {
                override fun receive(result: MoneyUtilResult) {
                    when (result) {
                        is MoneyUtilResult.Error -> state.value = ConvertState.Error(Throwable("error"))
                        is MoneyUtilResult.Success -> {
                            val euroLast = result.money.rates?.eUR ?: 0.0
                            val moneyDouble: Double? = euroLast.times(event.toDouble())
                            val moneyDouble2F: Double? = moneyDouble?.times(ONE_HUNDRED)?.roundToInt()?.div(ONE_HUNDRED)
                            state.value = ConvertState.Success(moneyDouble2F.toString())
                        }
                        is MoneyUtilResult.SuccessWithoutNetwork -> {
                            val euroLast = result.money.rates?.eUR ?: 0.0
                            val moneyDouble: Double? = euroLast.times(event.toDouble())
                            val moneyDouble2F: Double? = moneyDouble?.times(ONE_HUNDRED)?.roundToInt()?.div(ONE_HUNDRED)
                            state.value = ConvertState.SuccessWithoutNetwork(moneyDouble2F.toString())
                        }
                    }.exhaustive
                }
            })
        } catch (exception: Throwable) {
            state.value = ConvertState.Error(Exception("error"))
        }
    }

    fun loadGbp(event: String, state: MutableLiveData<ConvertState>) {
        state.value = ConvertState.InProgress

        try {
            retrofitService.getDataFromApi(object : MoneyUtilResultReceiver {
                override fun receive(result: MoneyUtilResult) {
                    when (result) {
                        is MoneyUtilResult.Error -> state.value = ConvertState.Error(Throwable("error"))

                        is MoneyUtilResult.Success -> {
                            val dollarLast = result.money.rates?.gBP ?: 0.0
                            val moneyDouble: Double? = dollarLast.times(event.toDouble())
                            val moneyDouble2F: Double? = moneyDouble?.times(ONE_HUNDRED)?.roundToInt()?.div(ONE_HUNDRED)
                            state.value = ConvertState.SuccessGbp(moneyDouble2F.toString())
                        }
                        is MoneyUtilResult.SuccessWithoutNetwork -> {
                            val dollarLast = result.money.rates?.gBP ?: 0.0
                            val moneyDouble: Double? = dollarLast.times(event.toDouble())
                            val moneyDouble2F: Double? = moneyDouble?.times(ONE_HUNDRED)?.roundToInt()?.div(ONE_HUNDRED)
                            state.value = ConvertState.SuccessGbpWithoutNetwork(moneyDouble2F.toString())
                        }
                    }.exhaustive
                }
            })
        } catch (exception: Throwable) {
            state.value = ConvertState.Error(Exception("error"))
        }
    }

    fun getJouleFromKwh(kwh: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveJouleFromKwh(
            (kwh.toDouble() * THIRTY_SIX * ONE_THOUSAND)
                .times(ONE_HUNDRED).roundToInt().div(ONE_HUNDRED).toString()
        )
    }

    fun getKwhFromJoule(joule: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveKwhFromJoule(
            (joule.toDouble() / THIRTY_SIX * ONE_THOUSAND)
                .times(ONE_HUNDRED).roundToInt().div(ONE_HUNDRED).toString()
        )
    }

    fun getLiterFromCubicMeter(cubicMeter: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveLiterFromCubicMeter(
            cubicMeter.toDouble().div(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
                .div(ONE_HUNDRED * ONE_HUNDRED).toString()
        )
    }

    fun getCubicMeterFromLiter(liter: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveCubicMeterFromLiter(
            liter.toDouble().times(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
                .div(ONE_HUNDRED * ONE_HUNDRED).toString()
        )
    }

    fun getMFromKm(km: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveMFromKm(
            km.toDouble().times(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
                .div(ONE_HUNDRED * ONE_HUNDRED).toString()
        )
    }

    fun getKmFromM(meter: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveKmFromM(
            meter.toDouble().div(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
                .div(ONE_HUNDRED * ONE_HUNDRED).toString()
        )
    }
}
