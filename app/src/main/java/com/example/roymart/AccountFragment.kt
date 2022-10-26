package com.example.roymart

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textViewAccountName = view.findViewById<TextView>(R.id.textViewAccountName)

        // get users name
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        user?.let {
            db.collection("MarketUsers").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if(document != null) {
                        textViewAccountName.text = document.getString("Name")
                    }
                }.addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Failed to get user data")
                }
        }
    }
}