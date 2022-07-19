package com.example.roymart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class StoreActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        loadFragment(ShopFragment())

        // Bottom bar stuff to switch fragments
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.shopMenuItem -> loadFragment(ShopFragment())
                R.id.cartMenuItem -> loadFragment(CartFragment())
                R.id.accountMenuItem -> loadFragment(AccountFragment())
            }
            true
        }

    }

    // Simple method to switch fragments
    private  fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_a_container,fragment)
            commit()
        }
    }
}