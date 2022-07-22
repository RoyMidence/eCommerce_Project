package com.example.roymart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val dataSet: ArrayList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewStore: ImageView
        val textViewStoreName: TextView
        val textViewStorePrice: TextView

        init {
            imageViewStore = view.findViewById(R.id.imageViewStore)
            textViewStoreName = view.findViewById(R.id.textViewStoreName)
            textViewStorePrice = view.findViewById(R.id.textViewStorePrice)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_recyclerview_layout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.imageViewStore.setImageBitmap(dataSet[position].productImage)
        viewHolder.textViewStoreName.text = dataSet[position].productName
        viewHolder.textViewStorePrice.text = dataSet[position].productPrice.toString()
    }

    override fun getItemCount() = dataSet.size

    fun setData(newData: ArrayList<Product> ) {
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }
}
