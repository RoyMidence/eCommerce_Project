package com.example.roymart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(private val dataSet: ArrayList<Cart>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewCartName: TextView
        val textViewTotalPrice: TextView
        val textViewQuantity: TextView
        val textViewRemove : TextView

        init {
            textViewCartName = view.findViewById(R.id.textViewCartName)
            textViewTotalPrice = view.findViewById(R.id.textViewTotalPrice)
            textViewQuantity = view.findViewById(R.id.textViewQuantity)
            textViewRemove = view.findViewById(R.id.textViewRemove)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cart_recyclerview_item, viewGroup, false)

        return CartAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: CartAdapter.ViewHolder, position: Int) {
        val total = dataSet[position].ProductPrice!! * dataSet[position].Quantity!!

        viewHolder.textViewRemove.setOnClickListener {

        }

        viewHolder.textViewCartName.text= dataSet[position].ProductName
        viewHolder.textViewTotalPrice.text = total.toString()
        viewHolder.textViewQuantity.text = dataSet[position].Quantity.toString()
    }

    override fun getItemCount() = dataSet.size
}