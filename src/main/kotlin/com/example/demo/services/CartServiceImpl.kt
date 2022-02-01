package com.example.demo.services

import com.example.demo.exceptions.CartNotFoundException
import com.example.demo.repositories.CartRepository
import com.example.demo.schemas.CartSchema
import org.springframework.stereotype.Service

@Service
class CartServiceImpl(private val cartRepository: CartRepository) {

    fun getAll(): List<CartSchema> = cartRepository.findAll()

    fun getById(id: String): CartSchema {
        return cartRepository
            .findById(id)
            .orElseThrow { throw CartNotFoundException() }
    }

    fun create(cart: CartSchema) = cartRepository.save(cart)

    fun update(cart: CartSchema): CartSchema {
        val findCart = getById(cart.id.toString())

        return cartRepository.save(
            cart.apply {
                this.id = findCart.id
                this.items = cart.items
                this.total = cart.total
            }
        )
    }

    fun deleteById(id: String): CartSchema {
        val findCart = getById(id)

        cartRepository.delete(findCart)

        return findCart
    }
}