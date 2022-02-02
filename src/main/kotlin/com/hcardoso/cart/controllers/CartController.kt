package com.hcardoso.cart.controllers

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
@RequestMapping("/cart")
class CartController(
    private val cartService: CartService,
    private val productService: ProductService
) {

    @GetMapping
    fun getAll(): List<CartSchema> = cartService.getAll()

    @PostMapping
    fun create(@RequestBody cartBody: ArrayList<CartItemSimple>): ResponseEntity<CartSchema> {
        var items: ArrayList<CartItem> = ArrayList()

        for(item in cartBody) {
            try {
                val product = productService.getById(item.productId)
                items.add(
                    CartItem(
                        product = ProductSchema(
                            id = product.id,
                            name = product.name,
                            price = product.price
                        ),
                        quantity = item.quantity
                    )
                )
            } catch (e: Exception) {}
        }

        val cart = Cart(items = items)

        val response = cartService.create(
            CartSchema(
                items = cart.items,
                total = cart.getTotal()
            )
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: String
    ): ResponseEntity<CartSchema> {
        val cartResponse = cartService.getById(id)
        return ResponseEntity.ok(cartResponse)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Nothing> {
        cartService.deleteById(cartService.getById(id).id.toString())

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null)
    }
}