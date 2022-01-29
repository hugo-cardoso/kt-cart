package com.example.demo.models

import com.example.demo.schemas.ProductSchema

class CartItem(
    val product: ProductSchema,
    var quantity: Int
)