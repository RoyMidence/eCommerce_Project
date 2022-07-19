package com.example.roymart

import android.graphics.Bitmap

class Product(pID: Int, pName: String, pImage: Bitmap,
            pRating: Float, pPrice: Float, pCategory: String) {

    var productID: Int
    var productName: String
    var productImage: Bitmap
    var productRating: Float
    var productPrice: Float
    var productCategory: String

    init {
        productID = pID
        productName = pName
        productImage = pImage
        productRating = pRating
        productPrice = pPrice
        productCategory = pCategory
    }



}