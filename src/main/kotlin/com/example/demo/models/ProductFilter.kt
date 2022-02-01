package com.example.demo.models

data class ProductFilter(
    var name: String? = null,
    var minPrice: Float? = null,
    var maxPrice: Float? = null
) {
    fun hasFilter(): Boolean {
        name?.let { return true }
        minPrice?.let { return true }
        maxPrice?.let { return true }

        return false
    }
}