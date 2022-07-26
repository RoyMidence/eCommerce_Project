package com.example.roymart

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CartFragment : Fragment() {
    private var list = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textViewCartItem = view.findViewById<TextView>(R.id.textViewCartItem)

        // get users name
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        user?.let {
            db.collection("Carts")
                .whereEqualTo("UserID", user.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (doc in documents) {
                        textViewCartItem.text = doc.getString("ProductName")
                    }
                }.addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Failed to get Cart data")
                }
        }
    }
}