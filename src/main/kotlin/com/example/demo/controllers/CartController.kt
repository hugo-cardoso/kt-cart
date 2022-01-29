package com.example.demo.controllers

import com.example.demo.models.Cart
import com.example.demo.models.CartItem
import com.example.demo.models.CartItemSimple
import com.example.demo.schemas.CartSchema
import com.example.demo.schemas.ProductSchema
import com.example.demo.services.CartService
import com.example.demo.services.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cart")
class CartController(
    val cartService: CartService,
    val productService: ProductService
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
        return try {
            val cartResponse = cartService.getById(id)
            ResponseEntity.ok(cartResponse)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @GetMapping("/{id}/products")
    fun getProducts(
        @PathVariable id: String
    ): ResponseEntity<ArrayList<CartItem>> {
        return try {
            val cartResponse = cartService.getById(id)
            ResponseEntity.ok(cartResponse.items)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @PostMapping("/{id}/products")
    fun addProducts(
        @PathVariable id: String,
        @RequestBody items: ArrayList<CartItemSimple>
    ): ResponseEntity<CartSchema> {
        return try {
            val cartResponse = cartService.getById(id)
            val cart = Cart(
                id = cartResponse.id,
                items = cartResponse.items
            )

            for(item in items) {
                try {
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
                } catch (e: Exception) {}
            }

            ResponseEntity.ok(
                cartService.update(
                    CartSchema(
                        id = cart.id,
                        items = cart.items,
                        total = cart.getTotal()
                    )
                )
            )
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @PutMapping("/{id}/products/{itemId}")
    fun editProduct(
        @PathVariable id: String,
        @PathVariable itemId: String,
        @RequestParam quantity: String
    ): ResponseEntity<Nothing> {
        return try {
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

            ResponseEntity.status(HttpStatus.ACCEPTED).body(null)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @DeleteMapping("/{id}/products/{itemId}")
    fun removeProduct(
        @PathVariable id: String,
        @PathVariable itemId: String
    ): ResponseEntity<Nothing> {
        return try {
            val cartSchema = cartService.getById(id)

            val cart = Cart(
                id = cartSchema.id,
                items = cartSchema.items
            )

            val item = cart.items.find { it.product.id == itemId }

            if (item != null) {
                cart.removeItem(item)

                val response = cartService.update(
                    CartSchema(
                        id = cart.id,
                        items = cart.items,
                        total = cart.getTotal()
                    )
                )

                ResponseEntity.status(HttpStatus.ACCEPTED).body(null)
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Nothing> {
        return try {
            val cartSchema = cartService.getById(id)

            cartService.deleteById(cartSchema.id.toString())

            ResponseEntity.status(HttpStatus.ACCEPTED).body(null)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }
}