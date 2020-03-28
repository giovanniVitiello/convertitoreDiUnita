package com.example.convertitorediunita.ui.chronology

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.convertitorediunita.R
import com.example.convertitorediunita.db.Value

class ChronologyAdapter :
    ListAdapter<Value, ValueViewHolder>(MoneyTransactionDiffUtil()) {

    // take the entire view from layout home_row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValueViewHolder {
        val cellForRow = LayoutInflater.from(parent.context).inflate(R.layout.home_row, parent, false)
        return ValueViewHolder(cellForRow)
    }

    // second: set the value of single textView item with the value of date conversion
    override fun onBindViewHolder(holder: ValueViewHolder, position: Int) {
        val category = getItem(position)
        holder.moneyDateConversion.text = category.dateConversion
        holder.moneyTypeConversion.text = category.typeConversion
        holder.moneyConversion.text = category.conversion
    }
}

//  first: take the single id View from the layout
class ValueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val moneyDateConversion = itemView.findViewById<TextView>(R.id.date_conversion)
    val moneyTypeConversion = itemView.findViewById<TextView>(R.id.type_conversion)
    val moneyConversion = itemView.findViewById<TextView>(R.id.currency_result)
}

class MoneyTransactionDiffUtil : DiffUtil.ItemCallback<Value>() {
    override fun areItemsTheSame(oldItem: Value, newItem: Value): Boolean {
        return oldItem.conversion == newItem.conversion
    }

    override fun areContentsTheSame(oldItem: Value, newItem: Value): Boolean {
        return oldItem.equals(newItem)
    }
}
