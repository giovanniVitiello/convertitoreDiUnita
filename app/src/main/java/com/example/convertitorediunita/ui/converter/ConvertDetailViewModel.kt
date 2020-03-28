package com.example.convertitorediunita.ui.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.convertitorediunita.RetrofiteService
import com.example.convertitorediunita.ui.model.MoneyUtil
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

sealed class ConvertDetailEvent {
    data class Load(val newString: String) : ConvertDetailEvent()
    data class LoadGbp(val newString: String) : ConvertDetailEvent()
    data class GetFahrenheitFromCelsius(val fahrenait: String) : ConvertDetailEvent()
    data class GetCelsiusFromFahrenheit(val celsius: String) : ConvertDetailEvent()
    data class GetKgFromG(val g: String) : ConvertDetailEvent()
    data class GetGFromKg(val kg: String) : ConvertDetailEvent()
}

sealed class ConvertDetailState {
    object InProgress : ConvertDetailState()
    data class Error(val error: Throwable) : ConvertDetailState()
    data class Success(val newString: MoneyUtil) : ConvertDetailState()
    data class SuccessGbp(val newStringGbp: MoneyUtil) : ConvertDetailState()
    data class ReceiveFahrenheitFromCel(val fahrenait: String) : ConvertDetailState()
    data class ReceiveCelsiusFromFahr(val celsius: String) : ConvertDetailState()
    data class ReceiveKgFromG(val g: String) : ConvertDetailState()
    data class ReceiveGFromKg(val kg: String) : ConvertDetailState()
}

class ConvertDetailViewModel : ViewModel() {

    private val retrofiteService = RetrofiteService()

    private val _result = MutableLiveData<String>()

    val state: MutableLiveData<ConvertDetailState> = MutableLiveData()

    init {
        _result.value = ""
    }

    val result: LiveData<String> = _result

    fun sendEvent(event: ConvertDetailEvent) {
        when (event) {
            is ConvertDetailEvent.Load -> loadContent()
            is ConvertDetailEvent.LoadGbp -> loadContent2()
            is ConvertDetailEvent.GetCelsiusFromFahrenheit -> getCelsiusFromFahrenheit(event.celsius)
            is ConvertDetailEvent.GetFahrenheitFromCelsius -> getFahrenheitFromCelsius(event.fahrenait)
            is ConvertDetailEvent.GetKgFromG -> getKgFromG(event.g)
            is ConvertDetailEvent.GetGFromKg -> getGFromKg(event.kg)
        }
    }

    private fun loadContent() {
        state.value = ConvertDetailState.InProgress

        viewModelScope.launch {
            try {
                val moneyDetail = retrofiteService.moneyDetail()
                Log.d("GifDetailViewModel", "result: $moneyDetail")
                state.value = ConvertDetailState.Success(newString = moneyDetail)
            } catch (exception: Throwable) {
                state.value = ConvertDetailState.Error(exception)
            }
        }
    }

    private fun loadContent2() {
        state.value = ConvertDetailState.InProgress

        viewModelScope.launch {
            try {
                val moneyDetail = retrofiteService.moneyDetail()
                Log.d("GifDetailViewModel", "result: $moneyDetail")
                state.value = ConvertDetailState.SuccessGbp(newStringGbp = moneyDetail)
            } catch (exception: Throwable) {
                state.value = ConvertDetailState.Error(exception)
            }
        }
    }

    private fun getFahrenheitFromCelsius(celsius: String) {
        _result.value = (celsius.toDouble() * NINE / FIVE + THIRTY_TWO).times(ONE_HUNDRED).roundToInt().div(ONE_HUNDRED).toString()
    }

    private fun getCelsiusFromFahrenheit(fahrenait: String) {
        _result.value = ((fahrenait.toDouble() - THIRTY_TWO) * FIVE / NINE).times(ONE_HUNDRED).roundToInt().div(ONE_HUNDRED).toString()
    }

    private fun getKgFromG(kilo: String) {
        _result.value = kilo.toDouble().div(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt().div(ONE_HUNDRED * ONE_HUNDRED).toString()
    }

    private fun getGFromKg(g: String) {
        _result.value = g.toDouble().times(ONE_THOUSAND).times(ONE_HUNDRED * ONE_HUNDRED).roundToInt().div(ONE_HUNDRED * ONE_HUNDRED).toString()
    }
}
