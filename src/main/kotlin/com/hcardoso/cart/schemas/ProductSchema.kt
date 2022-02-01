package com.hcardoso.cart.schemas

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ProductSchema(
    @Id var id: String? = null,
    var name: String? = null,
    var price: Float? = null,
)