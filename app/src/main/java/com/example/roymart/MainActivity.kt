package com.example.roymart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

class MainActivity : AppCompatActivity() {
    private var YOUR_CLIENT_ID = "AQ7RtPsLDF8jV1UM3rmKG5aMwnVEXXWfpfWXW37dX6KTLRq6WOm62PhPvfVgbfiFo77Tf5SPmr8lPoDJ"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = CheckoutConfig(
            application = application,
            clientId = YOUR_CLIENT_ID,
            environment = Environment.SANDBOX,
            returnUrl = "com.example.roymart://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                true
            )
        )
        PayPalCheckout.setConfig(config)


        setContentView(R.layout.activity_main)

        // get ID's for Edit Texts
        editTextEmail = findViewById(R.id.emailEditText)
        editTextPassword = findViewById(R.id.passwordEditText)

        // Button time
        loginButton = findViewById(R.id.loginButton)
        mAuth = Firebase.auth

        loginButton.setOnClickListener{
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Start activity here
                        startActivity(Intent(this, StoreActivity::class.java))
                    } else {
                        // Bad login
                        Toast.makeText(baseContext, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}