package com.example.demo.services

import com.example.demo.models.CartCreateBody
import com.example.demo.repositories.CartRepository
import com.example.demo.schemas.CartSchema
import org.springframework.stereotype.Service

@Service
class CartService(
    val cartRepository: CartRepository
) {
    fun getAll() = cartRepository.findAll()

    fun getById(id: String): CartSchema {
        val cart = cartRepository.findById(id)

        if (cart.isEmpty) throw object : Exception("Cart not found"){}

        return cart.get()
    }

    fun create(cart: CartSchema) = cartRepository.save(cart)

    fun update(cart: CartSchema): CartSchema {
        val findCart = cartRepository.findById(cart.id.toString())

        if (findCart.isEmpty) throw object : Exception("Cart not found"){}

        return cartRepository.save(
            cart.apply {
                this.items = cart.items
                this.total = cart.total
            }
        )
    }

    fun deleteById(id: String): CartSchema {
        val findCart = cartRepository.findById(id)

        if (findCart.isEmpty) throw object : Exception("Cart not found"){}

        cartRepository.delete(findCart.get())

        return findCart.get()
    }
}