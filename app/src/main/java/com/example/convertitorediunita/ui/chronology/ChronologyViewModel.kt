package com.example.convertitorediunita.ui.chronology

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.convertitorediunita.db.Value
import com.example.convertitorediunita.db.ValueDatabase
import com.example.convertitorediunita.db.WordRepository
import kotlinx.coroutines.launch

sealed class ConversionEvent {
    //    object Load : ConversionEvent()
    object DeleteTransactions : ConversionEvent()

    data class AddConversion(val conversion: Value) : ConversionEvent()
}

// States that HomeViewModel can have
sealed class ConversionState {

    data class Error(val error: Throwable) : ConversionState()

    data class Success(val conversion: List<Value>) : ConversionState()
}

class ChronologyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository
    var allValue = MutableLiveData<List<Value>>()
    var state: MutableLiveData<ConversionState> = MutableLiveData()

    init {

        val valueDao = ValueDatabase.getDatabase(application).valueDao()
        repository = WordRepository(valueDao)
        updateValue()
    }

    private fun updateValue() = viewModelScope.launch {
        allValue.value = repository.getValue()
        state.value = ConversionState.Success(repository.getValue())
    }

    // Function to send events from ChronologyFragment
    fun send(event: ConversionEvent) {
        when (event) {
            is ConversionEvent.DeleteTransactions -> deleteAll()
            is ConversionEvent.AddConversion -> {
                // Add new transaction
                insert(value = event.conversion)

                // Set state to success
                // TODO: Handle other states

                state.value = ConversionState.Success(allValue.value!!)
            }
        }
    }

    private fun insert(value: Value) = viewModelScope.launch {
        repository.insert(value)
        updateValue()
    }

    private fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
        updateValue()
    }
}
