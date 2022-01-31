package com.example.demo.models

data class Cart(
    val id: String? = null,
    var items: ArrayList<CartItem> = ArrayList(),
) {
    fun getTotal(): Float {
        var total = 0f;
        for (item in items) {
            item.product.price?.let {
                total += it * item.quantity
            }
        }
        return total
    }

    fun addItem(item: CartItem) {
        items.add(item)
    }

    fun removeItem(item: CartItem) {
        val cartItem = items.find { it.product.id == item.product.id }
        if (cartItem != null) items.remove(cartItem)
    }

    fun changeQuantity(itemId: String, quantity: Int) {
        val item = items.find { it.product.id == itemId }

        if (item != null) {
            val itemIndex = items.indexOf(item)

            items[itemIndex].quantity = quantity
        }
    }
}