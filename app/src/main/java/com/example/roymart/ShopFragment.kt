package com.example.roymart

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ShopFragment : Fragment() {
    private val ONE_MEGABYTE: Long = 1024*1024*5


    private lateinit var recyclerViewShop: RecyclerView
    private lateinit var adapter: ProductAdapter
    private var list= ArrayList<Product>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(context,2)
        recyclerViewShop = view.findViewById(R.id.recyclerViewShop)
        recyclerViewShop.layoutManager = layoutManager
        adapter = ProductAdapter(list, ProductAdapter.OnClickListener {item ->
            // OnClickListener Stuff Here
            // Will get image from DB again
            // Too much hassle to move big bitmaps
            val intent = Intent(context,ProductDetails::class.java)
            intent.putExtra("productName",item.productName)
            intent.putExtra("productPrice",item.productPrice.toString())
            intent.putExtra("productID", item.productID)
            intent.putExtra("productRating", item.productRating.toString())
            startActivity(intent)
        })
        recyclerViewShop.adapter = adapter
        getProducts()
    }

    private fun getProducts() {
        val db = Firebase.firestore
        db.collection("Products")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    list.clear()
                    for (doc in it.result) {
                        var temp = Product()
                        temp.productID = doc.id
                        temp.productName = doc.getString("Name")!!
                        temp.productCategory = doc.getString("Category")!!
                        temp.productRating = doc.getDouble("Rating")!!
                        temp.productPrice = doc.getDouble("Price")!!

                        // Create Storage Reference
                        // Get Product Image
                        val storageReference = Firebase.storage.reference
                        val imageRef = storageReference.child("RoyMart/" + doc.getString("Image"))

                        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                            // On Success
                            temp.productImage = BitmapFactory.decodeByteArray(it,0,it.size)
                            list.add(temp)
                            adapter.notifyDataSetChanged()
                            Log.d(TAG, "Just after add")
                        }.addOnFailureListener { exception ->
                            // On Fail
                            Log.d(TAG, "Failed to download image ", exception)
                        }
                    }
                    adapter.setData(list)
                } else {
                    Log.d(TAG, "Failed to load shop ", it.exception)
                }
            }
    }
}