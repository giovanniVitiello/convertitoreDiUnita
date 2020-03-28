package com.example.convertitorediunita.ui.chronology

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.convertitorediunita.MainActivity
import com.example.convertitorediunita.R
import com.example.convertitorediunita.db.Value
import kotlin.random.Random
import kotlinx.android.synthetic.main.fragment_chronology.*

const val DELAY_UPLOAD_DATA = 500L

class ChronologyFragment : Fragment() {

    companion object {

        fun newInstance(data: String, type: String, conversion: String) =
            ChronologyFragment().apply {
                arguments = Bundle().apply {
                    putString("date", data)
                    putString("type", type)
                    putString("conversion", conversion)
                }
            }
    }

    private lateinit var chronologyViewModel: ChronologyViewModel
    private lateinit var chronologyAdapter: ChronologyAdapter
    private lateinit var arrayNetwork: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chronologyViewModel =
            ViewModelProviders.of(this).get(ChronologyViewModel::class.java)

        return inflater.inflate(R.layout.fragment_chronology, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LayoutManager and Adapter
        chronologyAdapter = ChronologyAdapter()
        recyclerView.adapter = chronologyAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        arrayNetwork = arrayOf("$/€", "$/£")

        val newConversion: Button = requireActivity().findViewById(R.id.new_conversion)
        val deleteButton: Button = requireActivity().findViewById(R.id.delete_data)

        observeViewModel()

        val upLoad = Runnable {
            uploadData()
        }
        Handler().postDelayed(upLoad, DELAY_UPLOAD_DATA)

        newConversion.setOnClickListener {

            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            chronologyViewModel.send(ConversionEvent.DeleteTransactions)
            Toast.makeText(context, "Deleted all data!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadData() {

        val args = this.arguments
        if (args != null && !args.isEmpty) {
            val dateConversion = args.getString("date")
            val typeConversion = args.getString("type")
            val conversion = args.getString("conversion")

            chronologyViewModel.send(
                ConversionEvent.AddConversion(
                    Value(
                        id = Random.nextInt().toString(),
                        dateConversion = dateConversion,
                        typeConversion = typeConversion,
                        conversion = conversion
                    )
                )
            )
            args.clear()
        }
    }

    private fun observeViewModel() {

        chronologyViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ConversionState.Error -> showError(state.error)
                is ConversionState.Success -> showConversions(state.conversion)
            }
        })
    }

    private fun showError(error: Throwable) {
        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
        Log.i("Error", "Error in Home", error)
    }

    private fun showConversions(conversion: List<Value>) {
        chronologyAdapter.submitList(conversion)
    }
}
