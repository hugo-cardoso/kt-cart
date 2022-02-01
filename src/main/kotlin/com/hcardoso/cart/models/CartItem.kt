package com.hcardoso.cart.models

import com.hcardoso.cart.schemas.ProductSchema

class CartItem(
    val product: ProductSchema,
    var quantity: Int
)