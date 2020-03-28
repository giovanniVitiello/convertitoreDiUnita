package com.example.convertitorediunita.ui.converter

import androidx.lifecycle.MutableLiveData
import com.example.convertitorediunita.MoneyUtilResultReceiver
import com.example.convertitorediunita.RetrofiteService
import com.example.convertitorediunita.exhaustive
import kotlin.math.roundToInt

const val ONE_HUNDRED = 100.0
const val ONE_THOUSAND = 1000.0
const val FIVE = 5
const val NINE = 9
const val THIRTY_TWO = 32
const val THIRTY_SIX = 36

class Conversion {
    private val retrofitService = RetrofiteService()

    fun getFahrenheitFromCelsius(celsius: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveFahrenheitFromCel((celsius.toDouble() * NINE / FIVE + THIRTY_TWO)
            .times(ONE_HUNDRED).roundToInt().div(ONE_HUNDRED).toString())
    }

    fun getCelsiusFromFahrenheit(fahrenait: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveCelsiusFromFahrenheit(((fahrenait.toDouble() - THIRTY_TWO) * FIVE / NINE)
            .times(ONE_HUNDRED).roundToInt().div(ONE_HUNDRED).toString())
    }

    fun getKgFromG(kilo: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveKgFromG(kilo.toDouble().div(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
            .div(ONE_HUNDRED * ONE_HUNDRED).toString())
    }

    fun getGFromKg(grams: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveGFromKg(grams.toDouble().times(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
            .div(ONE_HUNDRED * ONE_HUNDRED).toString())
    }

    fun loadEuro(event: String, state: MutableLiveData<ConvertState>) {
        state.value = ConvertState.InProgress

        try {
            retrofitService.getDataFromApi(object : MoneyUtilResultReceiver {
                override fun receive(result: MoneyUtilResult) {
                    when (result) {
                        is MoneyUtilResult.Error -> state.value = ConvertState.Error(Throwable("error"))
                        is MoneyUtilResult.Success -> {
                            val moneyDouble: Double? = result.money.rates?.eUR?.times(event.toDouble())
                            val moneyDouble2F: Double? = moneyDouble?.times(ONE_HUNDRED)?.roundToInt()?.div(ONE_HUNDRED)
                            state.value = ConvertState.Success(moneyDouble2F.toString())
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
                            val moneyDouble: Double? = result.money.rates?.gBP?.times(event.toDouble())
                            val moneyDouble2F: Double? = moneyDouble?.times(ONE_HUNDRED)?.roundToInt()?.div(ONE_HUNDRED)
                            state.value = ConvertState.SuccessGbp(moneyDouble2F.toString())
                        }
                    }.exhaustive
                }
            })
        } catch (exception: Throwable) {
            state.value = ConvertState.Error(Exception("error"))
        }
    }

    fun getJouleFromKwh(kwh: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveJouleFromKwh((kwh.toDouble() * THIRTY_SIX * ONE_THOUSAND)
            .times(ONE_HUNDRED).roundToInt().div(ONE_HUNDRED).toString())
    }

    fun getKwhFromJoule(joule: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveKwhFromJoule((joule.toDouble() / THIRTY_SIX * ONE_THOUSAND)
            .times(ONE_HUNDRED).roundToInt().div(ONE_HUNDRED).toString())
    }

    fun getLiterFromCubicMeter(cubicMeter: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveLiterFromCubicMeter(cubicMeter.toDouble().div(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
            .div(ONE_HUNDRED * ONE_HUNDRED).toString())
    }

    fun getCubicMeterFromLiter(liter: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveCubicMeterFromLiter(liter.toDouble().times(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
            .div(ONE_HUNDRED * ONE_HUNDRED).toString())
    }

    fun getMFromKm(km: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveMFromKm(km.toDouble().times(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
            .div(ONE_HUNDRED * ONE_HUNDRED).toString())
    }

    fun getKmFromM(meter: String, result: MutableLiveData<ConvertState>) {
        result.value = ConvertState.ReceiveKmFromM(meter.toDouble().div(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt()
            .div(ONE_HUNDRED * ONE_HUNDRED).toString())
    }
}
