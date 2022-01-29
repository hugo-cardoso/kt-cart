package com.example.demo.controllers

import com.example.demo.models.ProductCreateBody
import com.example.demo.models.ProductUpdateBody
import com.example.demo.schemas.ProductSchema
import com.example.demo.services.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductController(val productService: ProductService) {

    @GetMapping
    fun getProducts() = ResponseEntity.ok(productService.getAll())

    @PostMapping
    fun createProduct(@RequestBody body: ProductCreateBody): ResponseEntity<ProductSchema> {
        val response = productService.create(
            ProductSchema(
                name = body.name,
                price = body.price
            )
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @RequestBody body: ProductUpdateBody,
        @PathVariable id: String
    ): ResponseEntity<ProductSchema> {
        return try {
            val response = productService.update(
                ProductSchema(
                    id = id,
                    name = body.name,
                    price = body.price
                )
            )

            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: String): ResponseEntity<ProductSchema> {
        return try {
            val response = productService.delete(id)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }
}