package com.hcardoso.cart.controllers

import com.hcardoso.cart.exceptions.ProductNotFoundException
import com.hcardoso.cart.models.Cart
import com.hcardoso.cart.models.CartItem
import com.hcardoso.cart.models.CartItemSimple
import com.hcardoso.cart.schemas.CartSchema
import com.hcardoso.cart.schemas.ProductSchema
import com.hcardoso.cart.services.CartService
import com.hcardoso.cart.services.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cart/{id}/products")
class CartProductsController(
    private val cartService: CartService,
    private val productService: ProductService
) {
    @GetMapping
    fun getProducts(@PathVariable id: String): ResponseEntity<ArrayList<CartItem>> {
        return try {
            val cartResponse = cartService.getById(id)
            ResponseEntity.ok(cartResponse.items)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @PostMapping
    fun addProducts(
        @PathVariable id: String,
        @RequestBody items: ArrayList<CartItemSimple>
    ): ResponseEntity<CartSchema> {
        val cartResponse = cartService.getById(id)
        val cart = Cart(
            id = cartResponse.id,
            items = cartResponse.items
        )

        for(item in items) {
            val cartItem = cart.items.find { it.product.id == item.productId }

            if (cartItem != null) {
                cart.changeQuantity(item.productId, item.quantity + cartItem.quantity)
            } else {
                val product = productService.getById(item.productId)
                cart.addItem(
                    CartItem(
                        product = ProductSchema(
                            id = product.id,
                            name = product.name,
                            price = product.price
                        ),
                        quantity = item.quantity
                    )
                )
            }
        }

        return ResponseEntity.ok(
            cartService.update(
                CartSchema(
                    id = cart.id,
                    items = cart.items,
                    total = cart.getTotal()
                )
            )
        )
    }

    @PutMapping("/{itemId}")
    fun editProduct(
        @PathVariable id: String,
        @PathVariable itemId: String,
        @RequestParam quantity: String
    ): ResponseEntity<Nothing> {
        val cartSchema = cartService.getById(id)

        val cart = Cart(
            id = cartSchema.id,
            items = cartSchema.items
        )

        val item = cart.items.find { it.product.id == itemId }

        if (item != null) {
            cart.changeQuantity(itemId, quantity.toInt())

            cartService.update(
                CartSchema(
                    id = cart.id,
                    items = cart.items,
                    total = cart.getTotal()
                )
            )
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null)
    }

    @DeleteMapping("/{itemId}")
    fun removeProduct(
        @PathVariable id: String,
        @PathVariable itemId: String
    ): ResponseEntity<Nothing> {
        val cartSchema = cartService.getById(id)

        val cart = Cart(
            id = cartSchema.id,
            items = cartSchema.items
        )

        val item = cart.items.find { it.product.id == itemId }

        if (item != null) {
            cart.removeItem(item)

            cartService.update(
                CartSchema(
                    id = cart.id,
                    items = cart.items,
                    total = cart.getTotal()
                )
            )

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null)
        } else {
            throw ProductNotFoundException()
        }
    }
}