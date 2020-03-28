package com.example.convertitorediunita.ui.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.convertitorediunita.R
import com.example.convertitorediunita.ui.home.NETWORK
import com.example.convertitorediunita.ui.home.TEMPERATURE
import com.example.convertitorediunita.ui.home.WEIGHT
import com.example.convertitorediunita.ui.converter.ConvertDetailEvent
import com.example.convertitorediunita.ui.converter.ConvertDetailState
import com.example.convertitorediunita.ui.converter.ConvertDetailViewModel
import com.google.android.material.snackbar.Snackbar

class ConvertDetailActivity : AppCompatActivity() {

    lateinit var converterDetailViewModel: ConvertDetailViewModel
    lateinit var activityConvert: ConstraintLayout
    lateinit var insert: EditText
    lateinit var result: TextView
    lateinit var progressBar: ProgressBar
    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        converterDetailViewModel = ViewModelProviders.of(this).get(ConvertDetailViewModel::class.java)
        setContentView(R.layout.activity_convert)

        Log.i("MainActivity", "onCreate Called")

        setupView()
    }

    fun setupView() {
        activityConvert = findViewById(R.id.activity_convert)
        insert = findViewById(R.id.editText)
        result = findViewById(R.id.numero)
        progressBar = findViewById(R.id.progress)
        spinner = findViewById(R.id.spinner) as Spinner
    }

    override fun onStart() {
        Log.i("MainActivity", "onStart Called")
        super.onStart()

        val initialArraySpinner = mutableListOf<String>()

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, initialArraySpinner)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.setAdapter(arrayAdapter)

        fun elaborate(selectedItem: String) {
            if (selectedItem == "fahrenait/celsius") {
                if (insert.text.toString().isEmpty()) {
                    Toast.makeText(this, "please enter value", Toast.LENGTH_LONG).show()
                } else {
                    converterDetailViewModel.sendEvent(
                        ConvertDetailEvent.GetCelsiusFromFahrenheit(
                            insert.text.toString()
                        )
                    )
                }
            } else if (selectedItem == "celsius/fahrenait") {
                if (insert.text.toString().isEmpty()) {
                    Toast.makeText(this, "please enter value", Toast.LENGTH_LONG).show()
                } else {
                    converterDetailViewModel.sendEvent(
                        ConvertDetailEvent.GetFahrenheitFromCelsius(
                            insert.text.toString()
                        )
                    )
                }
            } else if (selectedItem == "g/kg") {
                if (insert.text.toString().isEmpty()) {
                    Toast.makeText(this, "please enter value", Toast.LENGTH_LONG).show()
                } else {
                    converterDetailViewModel.sendEvent(
                        ConvertDetailEvent.GetKgFromG(
                            insert.text.toString()
                        )
                    )
                }
            } else if (selectedItem == "kg/g") {
                if (insert.text.toString().isEmpty()) {
                    Toast.makeText(this, "please enter value", Toast.LENGTH_LONG).show()
                } else {
                    converterDetailViewModel.sendEvent(
                        ConvertDetailEvent.GetGFromKg(
                            insert.text.toString()
                        )
                    )
                }
            } else if (selectedItem == "$/€") {
                if (insert.text.toString().isEmpty()) {
                    Toast.makeText(this, "please enter value", Toast.LENGTH_LONG).show()
                } else {
                    converterDetailViewModel.sendEvent(
                        ConvertDetailEvent.Load(
                            insert.text.toString()
                        )
                    )
                }
            } else if (selectedItem == "$/£") {
                if (insert.text.toString().isEmpty()) {
                    Toast.makeText(this, "please enter value", Toast.LENGTH_LONG).show()
                } else {
                    converterDetailViewModel.sendEvent(
                        ConvertDetailEvent.LoadGbp(
                            insert.text.toString()
                        )
                    )
                }
            }
        }

        fun spinnerTemperature() {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) { /*emptyBlock*/
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem =
                        parent?.getItemAtPosition(position).toString()
                    elaborate(selectedItem)
                }
            }
        }

        fun spinnerWeight() {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // to close the onItemSelected
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem =
                        parent?.getItemAtPosition(position).toString()
                    elaborate(selectedItem)
                }
            }
        }

        fun spinnerNetwork() {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // to close the onItemSelected
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem =
                        parent?.getItemAtPosition(position).toString()
                    elaborate(selectedItem)
                }
            }
        }

        hideProgress()

        val intId = intent.extras?.getInt("id")

        converterDetailViewModel.result.observe(this, Observer {
            result.text = it
        })

        insert.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                elaborate(spinner.selectedItem.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /*emptyBlock*/
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /*emptyBlock*/
            }
        })

        when (intId) {
            TEMPERATURE -> {
                val intent = intent
                val arraySpinner2 =
                    intent.getStringArrayExtra("arrayTemperature")
                initialArraySpinner.add(arraySpinner2[0])
                initialArraySpinner.add(arraySpinner2[1])
                arrayAdapter.notifyDataSetChanged()

                converterDetailViewModel.state.observe(this, Observer { state ->
                    when (state) {
                        is ConvertDetailState.ReceiveCelsiusFromFahr -> {
                            result.text = state.celsius
                        }
                        is ConvertDetailState.ReceiveFahrenheitFromCel -> {
                            result.text = state.fahrenait
                        }
                    }
                })
                spinnerTemperature()
            }

            WEIGHT -> {
                val intent = intent
                val arraySpinner2 =
                    intent.getStringArrayExtra("arrayWeight")
                initialArraySpinner.add(arraySpinner2[0])
                initialArraySpinner.add(arraySpinner2[1])
                arrayAdapter.notifyDataSetChanged()

                converterDetailViewModel.state.observe(this, Observer { state ->
                    when (state) {
                        is ConvertDetailState.ReceiveKgFromG -> {
                            result.text = state.g
                        }
                        is ConvertDetailState.ReceiveGFromKg -> {
                            result.text = state.kg
                        }
                    }
                })
                spinnerWeight()
            }

            NETWORK -> {
                val intent = intent
                val arraySpinner2 =
                    intent.getStringArrayExtra("arrayNetwork")
                initialArraySpinner.add(arraySpinner2[0])
                initialArraySpinner.add(arraySpinner2[1])
                arrayAdapter.notifyDataSetChanged()

                converterDetailViewModel.state.observe(this, Observer { state ->

                    when (state) {
                        is ConvertDetailState.InProgress -> showProgress()
                        is ConvertDetailState.Error -> {
                            hideProgress()
                            showError(state.error)
                        }
                        is ConvertDetailState.Success -> {
                            hideProgress()
                            result.text = state.newString.toString()
                        }
                        is ConvertDetailState.SuccessGbp -> {
                            hideProgress()
                            result.text = state.newStringGbp.toString()
                        }
                    }
                })
                spinnerNetwork()
            }
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
        result.visibility = View.GONE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
        result.visibility = View.VISIBLE
    }

    private fun showError(error: Throwable) {
        Snackbar.make(activityConvert, "error: $error", Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry", View.OnClickListener {
                converterDetailViewModel.sendEvent(ConvertDetailEvent.Load("error"))
            })
            .show()
    }

    override fun onResume() {
        Log.i("MainActivity", "onResume Called")
        super.onResume()
    }

    override fun onPause() {
        Log.i("MainActivity", "onPause Called")
        super.onPause()
    }

    override fun onStop() {
        Log.i("MainActivity", "onStop Called")
        super.onStop()
    }

    override fun onRestart() {
        Log.i("MainActivity", "onRestart Called")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.i("MainActivity", "onDestroy Called")
        super.onDestroy()
    }
}
