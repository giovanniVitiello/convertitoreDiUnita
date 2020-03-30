package com.example.convertitorediunita.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.convertitorediunita.R
import com.example.convertitorediunita.ui.converter.ConvertActivity

const val TEMPERATURE = 0
const val WEIGHT = 1
const val NETWORK = 2
const val ENERGY = 3
const val LITER = 4
const val LENGTH = 5

class HomeFragment : Fragment() {

    private lateinit var temperature: ImageButton
    private lateinit var weight: ImageButton
    private lateinit var networkButton: ImageButton
    private lateinit var energy: ImageButton
    private lateinit var liter: ImageButton
    private lateinit var lenght: ImageButton
    private lateinit var arrayTemperature: Array<String>
    private lateinit var arrayWeight: Array<String>
    private lateinit var arrayNetwork: Array<String>
    private lateinit var arrayEnergy: Array<String>
    private lateinit var arrayLiter: Array<String>
    private lateinit var arrayLenght: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayTemperature = arrayOf("celsius/fahrenheit", "fahrenheit/celsius")
        arrayWeight = arrayOf("kg/g", "g/kg")
        arrayNetwork = arrayOf("$/€", "$/£")
        arrayEnergy = arrayOf("joule/kwh", "kwh/joule")
        arrayLiter = arrayOf("l/cubicMeter", "cubicMeter/l")
        arrayLenght = arrayOf("m/km", "km/m")
        setupViews()
    }

    private fun setupViews() {
        temperature = view!!.findViewById(R.id.Temperature)
        weight = view!!.findViewById(R.id.Weight)
        networkButton = view!!.findViewById(R.id.Exchange)
        energy = view!!.findViewById(R.id.Energy)
        liter = view!!.findViewById(R.id.Liter)
        lenght = view!!.findViewById(R.id.Length)
    }

    @Suppress("LongMethod")
    override fun onStart() {
        super.onStart()
        temperature.setOnClickListener {
            val intent = Intent(activity, ConvertActivity::class.java)
            intent.putExtra("id", TEMPERATURE)
            intent.putExtra("arrayTemperature", arrayTemperature)
            startActivity(intent)
        }

        weight.setOnClickListener {
            val intent = Intent(activity, ConvertActivity::class.java)
            intent.putExtra("id", WEIGHT)
            intent.putExtra("arrayWeight", arrayWeight)
            startActivity(intent)
        }

        networkButton.setOnClickListener {
            val intent = Intent(activity, ConvertActivity::class.java)
            intent.putExtra("id", NETWORK)
            intent.putExtra("arrayNetwork", arrayNetwork)
            startActivity(intent)
        }
        energy.setOnClickListener {
            val intent = Intent(activity, ConvertActivity::class.java)
            intent.putExtra("id", ENERGY)
            intent.putExtra("arrayEnergy", arrayEnergy)
            startActivity(intent)
        }

        liter.setOnClickListener {
            val intent = Intent(activity, ConvertActivity::class.java)
            intent.putExtra("id", LITER)
            intent.putExtra("arrayLiter", arrayLiter)
            startActivity(intent)
        }

        lenght.setOnClickListener {
            val intent = Intent(activity, ConvertActivity::class.java)
            intent.putExtra("id", LENGTH)
            intent.putExtra("arrayLength", arrayLenght)
            startActivity(intent)
        }
    }
}
