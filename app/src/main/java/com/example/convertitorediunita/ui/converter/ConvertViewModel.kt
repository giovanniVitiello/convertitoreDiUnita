package com.example.convertitorediunita.ui.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.convertitorediunita.ui.model.MoneyUtil

sealed class ConvertEvent {
    data class Load(val newString: String) : ConvertEvent()
    data class LoadGbp(val newString: String) : ConvertEvent()
    data class GetFahrenheitFromCelsius(val fahrenheit: String) : ConvertEvent()
    data class GetCelsiusFromFahrenheit(val celsius: String) : ConvertEvent()
    data class GetKgFromG(val g: String) : ConvertEvent()
    data class GetGFromKg(val kg: String) : ConvertEvent()
    data class GetJouleFromKwh(val kwh: String) : ConvertEvent()
    data class GetKwhFromJoule(val joule: String) : ConvertEvent()
    data class GetLiterFromCubicMeter(val mc: String) : ConvertEvent()
    data class GetCubicMeterFromLiter(val liter: String) : ConvertEvent()
    data class GetMFromKm(val km: String) : ConvertEvent()
    data class GetKmFromM(val m: String) : ConvertEvent()
}

sealed class ConvertState {
    object InProgress : ConvertState()
    data class Error(val error: Throwable) : ConvertState()
    data class Success(val newString: String) : ConvertState()
    data class SuccessGbp(val newStringGbp: String) : ConvertState()
    data class ReceiveFahrenheitFromCel(val fahrenheit: String) : ConvertState()
    data class ReceiveCelsiusFromFahrenheit(val celsius: String) : ConvertState()
    data class ReceiveKgFromG(val g: String) : ConvertState()
    data class ReceiveGFromKg(val kg: String) : ConvertState()
    data class ReceiveJouleFromKwh(val kwh: String) : ConvertState()
    data class ReceiveKwhFromJoule(val joule: String) : ConvertState()
    data class ReceiveLiterFromCubicMeter(val mc: String) : ConvertState()
    data class ReceiveCubicMeterFromLiter(val liter: String) : ConvertState()
    data class ReceiveMFromKm(val km: String) : ConvertState()
    data class ReceiveKmFromM(val m: String) : ConvertState()
}

sealed class MoneyUtilResult {
    data class Error(val error: Throwable) : MoneyUtilResult()
    data class Success(val money: MoneyUtil) : MoneyUtilResult()
}

class ConvertViewModel : ViewModel() {

    val result = MutableLiveData<ConvertState>()

    val state: MutableLiveData<ConvertState> = MutableLiveData()

    private val conversion = Conversion()

    fun sendEvent(event: ConvertEvent) {
        when (event) {
            is ConvertEvent.Load -> conversion.loadEuro(event.newString, state)
            is ConvertEvent.LoadGbp -> conversion.loadGbp(event.newString, state)
            is ConvertEvent.GetCelsiusFromFahrenheit -> conversion.getCelsiusFromFahrenheit(event.celsius, result)
            is ConvertEvent.GetFahrenheitFromCelsius -> conversion.getFahrenheitFromCelsius(event.fahrenheit, result)
            is ConvertEvent.GetKgFromG -> conversion.getKgFromG(event.g, result)
            is ConvertEvent.GetGFromKg -> conversion.getGFromKg(event.kg, result)
            is ConvertEvent.GetJouleFromKwh -> conversion.getJouleFromKwh(event.kwh, result)
            is ConvertEvent.GetKwhFromJoule -> conversion.getKwhFromJoule(event.joule, result)
            is ConvertEvent.GetLiterFromCubicMeter -> conversion.getLiterFromCubicMeter(event.mc, result)
            is ConvertEvent.GetCubicMeterFromLiter -> conversion.getCubicMeterFromLiter(event.liter, result)
            is ConvertEvent.GetMFromKm -> conversion.getMFromKm(event.km, result)
            is ConvertEvent.GetKmFromM -> conversion.getKmFromM(event.m, result)
        }
    }
}
