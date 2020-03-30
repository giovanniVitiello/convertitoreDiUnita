package com.example.convertitorediunita.ui.converter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
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
import com.example.convertitorediunita.ui.chronology.ChronologyFragment
import com.example.convertitorediunita.ui.home.ENERGY
import com.example.convertitorediunita.ui.home.LENGTH
import com.example.convertitorediunita.ui.home.LITER
import com.example.convertitorediunita.ui.home.NETWORK
import com.example.convertitorediunita.ui.home.TEMPERATURE
import com.example.convertitorediunita.ui.home.WEIGHT
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

const val HIDE_KEYBOARD_TIME = 1000L

@Suppress("LargeClass")
class ConvertActivity : AppCompatActivity() {

    private lateinit var converterViewModel: ConvertViewModel
    private lateinit var activityConvert: ConstraintLayout
    private lateinit var insert: EditText
    private lateinit var result: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var spinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var currentDateAndTime: String
    private val initialArraySpinner = mutableListOf<String>()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        converterViewModel = ViewModelProviders.of(this).get(ConvertViewModel::class.java)
        setContentView(R.layout.activity_convert)

        setupView()
    }

    private fun setupView() {
        activityConvert = findViewById(R.id.activity_convert)
        insert = findViewById(R.id.editText)
        result = findViewById(R.id.numero)
        progressBar = findViewById(R.id.progress)
        spinner = findViewById(R.id.spinner)
        saveButton = findViewById(R.id.save_network)
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activityConvert.windowToken, 0)
    }

    @Suppress("LongMethod", "ComplexMethod")
    override fun onStart() {
        Log.i("MainActivity", "onStart Called")
        super.onStart()

        val intId = intent.extras?.getInt("id")

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, initialArraySpinner)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)

        spinner.adapter = arrayAdapter

        @Suppress("LongMethod")
        fun elaborate(selectedItem: String) {
            when (selectedItem) {
                "fahrenheit/celsius" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.GetCelsiusFromFahrenheit(insert.text.toString()))
                }
                "celsius/fahrenheit" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.GetFahrenheitFromCelsius(insert.text.toString()))
                }
                "g/kg" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.GetKgFromG(insert.text.toString()))
                }
                "kg/g" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.GetGFromKg(insert.text.toString()))
                }
                "$/€" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.Load(insert.text.toString()))
                }
                "$/£" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.LoadGbp(insert.text.toString()))
                }
                "joule/kwh" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.GetJouleFromKwh(insert.text.toString()))
                }
                "kwh/joule" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.GetKwhFromJoule(insert.text.toString()))
                }
                "l/cubicMeter" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.GetLiterFromCubicMeter(insert.text.toString()))
                }
                "cubicMeter/l" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.GetCubicMeterFromLiter(insert.text.toString()))
                }
                "m/km" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.GetMFromKm(insert.text.toString()))
                }
                "km/m" -> {
                    if (insert.text.toString().isEmpty()) showToast()
                    else converterViewModel.sendEvent(ConvertEvent.GetKmFromM(insert.text.toString()))
                }
            }
        }

        fun spinnerCall() {
            spinner.onItemSelectedListener = object : OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) { /*emptyBlock*/
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    elaborate(selectedItem)
                }
            }
        }

        when (intId) {
            TEMPERATURE -> {
                createIntent("arrayTemperature")

                converterViewModel.result.observe(this, Observer { result ->
                    when (result) {
                        is ConvertState.ReceiveCelsiusFromFahrenheit -> {
                            val celsius: String = result.celsius
                            this.result.text = celsius
                            conversion(celsius, "arrayTemperature", 0)
                        }
                        is ConvertState.ReceiveFahrenheitFromCel -> {
                            val farhen: String = result.fahrenheit
                            this.result.text = farhen
                            conversion(farhen, "arrayTemperature", 1)
                        }
                    }
                })
                spinnerCall()
            }

            WEIGHT -> {
                createIntent("arrayWeight")

                converterViewModel.result.observe(this, Observer { state ->
                    when (state) {
                        is ConvertState.ReceiveKgFromG -> {
                            val grams: String = state.g
                            result.text = grams
                            conversion(grams, "arrayWeight", 0)
                        }
                        is ConvertState.ReceiveGFromKg -> {
                            val kg: String = state.kg
                            result.text = kg
                            conversion(kg, "arrayWeight", 1)
                        }
                    }
                })
                spinnerCall()
            }

            NETWORK -> {
                createIntent("arrayNetwork")

                converterViewModel.state.observe(this, Observer { state ->

                    when (state) {
                        is ConvertState.InProgress -> showProgress()
                        is ConvertState.Error -> {
                            hideProgress()
                            Handler().postDelayed({ hideKeyboard() }, HIDE_KEYBOARD_TIME)
                            showError(state.error)
                        }
                        is ConvertState.Success -> {
                            hideProgress()
                            val eUR = state.newString
                            result.text = eUR
                            conversion(eUR, "arrayNetwork", 0)
                        }
                        is ConvertState.SuccessGbp -> {
                            hideProgress()
                            val gBP = state.newStringGbp
                            result.text = gBP
                            conversion(gBP, "arrayNetwork", 1)
                        }
                    }
                })
                spinnerCall()
            }

            ENERGY -> {
                createIntent("arrayEnergy")

                converterViewModel.result.observe(this, Observer { result ->
                    when (result) {
                        is ConvertState.ReceiveKwhFromJoule -> {
                            val celsius: String = result.joule
                            this.result.text = celsius
                            conversion(celsius, "arrayEnergy", 0)
                        }
                        is ConvertState.ReceiveJouleFromKwh -> {
                            val farhen: String = result.kwh
                            this.result.text = farhen
                            conversion(farhen, "arrayEnergy", 1)
                        }
                    }
                })
                spinnerCall()
            }

            LITER -> {
                createIntent("arrayLiter")

                converterViewModel.result.observe(this, Observer { result ->
                    when (result) {
                        is ConvertState.ReceiveLiterFromCubicMeter -> {
                            val celsius: String = result.mc
                            this.result.text = celsius
                            conversion(celsius, "arrayLiter", 0)
                        }
                        is ConvertState.ReceiveCubicMeterFromLiter -> {
                            val farhen: String = result.liter
                            this.result.text = farhen
                            conversion(farhen, "arrayLiter", 1)
                        }
                    }
                })
                spinnerCall()
            }

            LENGTH -> {
                createIntent("arrayLength")

                converterViewModel.result.observe(this, Observer { result ->
                    when (result) {
                        is ConvertState.ReceiveKmFromM -> {
                            val celsius: String = result.m
                            this.result.text = celsius
                            conversion(celsius, "arrayLength", 0)
                        }
                        is ConvertState.ReceiveMFromKm -> {
                            val farhen: String = result.km
                            this.result.text = farhen
                            conversion(farhen, "arrayLength", 1)
                        }
                    }
                })
                spinnerCall()
            }
        }

        hideProgress()

        insert.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                elaborate(spinner.selectedItem.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /*emptyBlock*/
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /*emptyBlock*/
            }
        })
    }

    private fun conversion(typeConversion: String, typeArray: String, id: Int) {



        saveButton.setOnClickListener {

            saveDataExchange()
            val type = createIntent(typeArray)[id]

            val chronologyFragment = ChronologyFragment.newInstance(
                currentDateAndTime,
                type,
                insert.text.toString() + "/" + typeConversion
            )
            supportFragment(chronologyFragment)
        }
    }

    private fun supportFragment(fragment: ChronologyFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_convert, fragment, ChronologyFragment::javaClass.name)
            .addToBackStack(null)
            .commit()
    }

    private fun createIntent(typeArray: String): Array<String> {
        val intent = intent
        val arraySpinner = intent.getStringArrayExtra(typeArray)
        initialArraySpinner.add(arraySpinner[0])
        initialArraySpinner.add(arraySpinner[1])
        arrayAdapter.notifyDataSetChanged()
        return arraySpinner
    }

    private fun saveDataExchange() {
        changeButtonVisibility()
        hideKeyboard()

        simpleDateFormat = SimpleDateFormat("dd MM yyyy HH mm ss", Locale.ITALY)

        currentDateAndTime = simpleDateFormat.format(Date())
    }

    private fun showToast() {
        Toast.makeText(this, "please enter value", Toast.LENGTH_LONG).show()
    }

    private fun changeButtonVisibility() {
        saveButton.visibility = View.GONE
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
            .setAction("Retry") {
                converterViewModel.sendEvent(ConvertEvent.Load("error"))
            }
            .show()
    }
}
