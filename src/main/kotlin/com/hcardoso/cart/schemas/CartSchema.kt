package com.hcardoso.cart.schemas

import com.hcardoso.cart.models.CartItem
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class CartSchema(
    @Id var id: String? = null,
    var items: ArrayList<CartItem> = ArrayList(),
    var total: Float
)