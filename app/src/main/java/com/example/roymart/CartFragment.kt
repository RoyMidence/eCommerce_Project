package com.example.roymart

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.*
import com.paypal.checkout.paymentbutton.PayPalButton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CartFragment : Fragment() {
    private val ONE_MEGABYTE: Long = 1024*1024*5


    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private var list= ArrayList<Cart>()
    private lateinit var payPalButton: PayPalButton
    private lateinit var textViewCartTotal: TextView
    private var cartTotal = 0.0
    private lateinit var userID: String

    val db = Firebase.firestore
    val user = Firebase.auth.currentUser


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
        textViewCartTotal = view.findViewById(R.id.textViewCartTotal)

        recyclerViewCart.layoutManager = LinearLayoutManager(context)
        cartAdapter = CartAdapter(list)
        recyclerViewCart.adapter = cartAdapter
        getCart()

        payPalButton = view.findViewById(R.id.payPalButton)
        payPalButton.setup(
            createOrder =
                CreateOrder { createOrderActions->
                    val order =
                        Order(
                            intent = OrderIntent.CAPTURE,
                            appContext = AppContext(userAction = UserAction.PAY_NOW),
                            purchaseUnitList =
                                listOf(
                                    PurchaseUnit(
                                        amount =
                                        Amount(currencyCode = CurrencyCode.USD, value = cartTotal.toString())
                                    )
                                )
                        )
                    createOrderActions.create(order)
                },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")

                    val ProductIDs = hashMapOf<String, Int>()
                    for (order in list) {
                        ProductIDs[order.ProductID!!] = order.Quantity!!
                    }

                    val docData = hashMapOf(
                        "UserID" to userID,
                        "DateOrdered" to Timestamp(Date()),
                        "OrderTotal" to cartTotal,
                        "ProductIDs" to ProductIDs
                    )

                    db.collection("Orders")
                        .add(docData)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                    db.collection("Carts")
                        .whereEqualTo("UserID", userID)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                db.collection("Carts").document(document.id)
                                    .delete()
                                    .addOnSuccessListener {
                                        list.clear()
                                        cartAdapter.notifyDataSetChanged()
                                        textViewCartTotal.text = getString(R.string.empty_cart)
                                    }
                                    .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error getting documents: ", exception)
                        }
                }
            },
            onCancel = OnCancel {
                Log.d("OnCancel", "Buyer canceled the PayPal experience.")
            },
            onError = OnError { errorInfo ->
                Log.d("OnError", "Error: $errorInfo")
            }
        )
    }

    private fun getCart() {
        // get users name

        user?.let {
            userID = user.uid
            db.collection("Carts")
                .whereEqualTo("UserID", user.uid)
                .get()
                .addOnSuccessListener { documents ->
                    list.clear()
                    for (doc in documents) {
                        val cart = doc.toObject<Cart>()
                        cartTotal += cart.Price!! * cart.Quantity!!
                        list.add(cart)
                    }
                    textViewCartTotal.text = "Cart Total: " + cartTotal
                    cartAdapter.notifyDataSetChanged()
                }.addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Failed to get Cart data")
                }
        }
    }
}