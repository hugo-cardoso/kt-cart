package com.example.demo.schemas

import com.example.demo.models.CartItem
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class CartSchema(
    @Id val id: String? = null,
    var items: ArrayList<CartItem> = ArrayList(),
    var total: Float
)