package com.example.roymart

import android.graphics.Bitmap

class Product {
    var productName: String
    var productCategory: String
    var productRating: Number
    var productPrice: Number
    var productImage: Bitmap?
    var productID: String

    constructor(n: String, c: String, r: Double, p: Double,
                i: Bitmap, ID: String) {
        productName = n
        productCategory = c
        productRating = r
        productPrice = p
        productImage = i
        productID = ID
    }

    constructor() {
        productName = ""
        productCategory = ""
        productRating = 0.0
        productPrice = 0.0
        productImage = null
        productID = ""
    }
}