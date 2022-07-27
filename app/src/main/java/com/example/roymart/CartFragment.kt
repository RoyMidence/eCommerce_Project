package com.example.roymart

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CartFragment : Fragment() {
    private val ONE_MEGABYTE: Long = 1024*1024*5


    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private var list= ArrayList<Cart>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart)
        recyclerViewCart.layoutManager = LinearLayoutManager(context)
        cartAdapter = CartAdapter(list)
        recyclerViewCart.adapter = cartAdapter
        getCart()
    }

    private fun getCart() {
        // get users name
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        user?.let {
            db.collection("Carts")
                .whereEqualTo("UserID", user.uid)
                .get()
                .addOnSuccessListener { documents ->
                    list.clear()
                    for (doc in documents) {
                        val cart = doc.toObject<Cart>()
                        list.add(cart)
                    }
                    cartAdapter.notifyDataSetChanged()
                }.addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Failed to get Cart data")
                }
        }
    }
}