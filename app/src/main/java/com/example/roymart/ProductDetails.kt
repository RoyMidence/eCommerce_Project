package com.example.roymart

import android.content.ContentValues
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class ProductDetails : AppCompatActivity() {
    private val ONE_MEGABYTE: Long = 1024*1024*5

    private lateinit var textViewProductDetailName : TextView
    private lateinit var textViewProductDetailPrice: TextView
    private lateinit var imageViewProductDetail : ImageView
    private lateinit var buttonAddToCart: Button
    private lateinit var ratingBarProductDetail : RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        textViewProductDetailName = findViewById(R.id.textViewProductDetailName)
        textViewProductDetailPrice = findViewById(R.id.textViewProductDetailPrice)
        ratingBarProductDetail = findViewById(R.id.ratingBarProductDetail)

        getProductImage()

        textViewProductDetailName.text = intent.getStringExtra("productName")
        textViewProductDetailPrice.text = intent.getStringExtra("productPrice")
        ratingBarProductDetail.rating = intent.getStringExtra("productRating")?.toFloat()!!

        buttonAddToCart = findViewById(R.id.buttonAddToCart)
        buttonAddToCart.setOnClickListener{
            val db = Firebase.firestore
            val user = Firebase.auth.currentUser

            user?.let {

                val docData = hashMapOf(
                    "ProductIDs" to intent.getStringExtra("productID"),
                    "UserID" to user.uid,
                    "ProductName" to intent.getStringExtra("productName"),
                    "ProductPrice" to intent.getStringExtra("productPrice")?.toDouble(),
                    "Quantity" to 1

                )

                val userID = user.uid
                db.collection("Carts")
                    .add(docData)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    }.addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }
            }
        }
    }

    // Getting image from db again
    private fun getProductImage() {
        val db = Firebase.firestore
        db.collection("Products")
            .whereEqualTo("Name", intent.getStringExtra("productName"))
            .get()
            .addOnSuccessListener { documents->
                for (doc  in documents) {
                    // Create Storage Reference
                    // Get Product Image
                    val storageReference = Firebase.storage.reference
                    val imageRef = storageReference.child("RoyMart/" + doc.getString("Image"))

                    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        // On Success
                        imageViewProductDetail = findViewById(R.id.imageViewProductDetail)
                        imageViewProductDetail.setImageBitmap(BitmapFactory.decodeByteArray(it,0,it.size))
                        Log.d(ContentValues.TAG, "Just after add")
                    }.addOnFailureListener { exception ->
                        // On Fail
                        Log.d(ContentValues.TAG, "Failed to download image ", exception)
                    }
                }
            }
    }
}