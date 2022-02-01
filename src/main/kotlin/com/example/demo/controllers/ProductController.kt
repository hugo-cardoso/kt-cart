package com.example.demo.controllers

import com.example.demo.models.ProductCreateBody
import com.example.demo.models.ProductUpdateBody
import com.example.demo.schemas.ProductSchema
import com.example.demo.services.ProductServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductController(private val productService: ProductServiceImpl) {

    @GetMapping
    fun getProducts(
        @RequestParam(required = false) name: String? = null,
        @RequestParam(required = false) price: Float? = null
    ): ResponseEntity<List<ProductSchema>> {
        return if (
            name != null ||
            price != null
        ) {
            ResponseEntity.ok(productService.findByProperties(name, price))
        } else {
            ResponseEntity.ok(productService.getAll())
        }
    }

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: String) = ResponseEntity.ok(productService.getById(id))

    @PostMapping
    fun createProduct(@RequestBody body: ProductCreateBody): ResponseEntity<ProductSchema> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                productService.create(
                    ProductSchema(
                        name = body.name,
                        price = body.price
                    )
                )
            )
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @RequestBody body: ProductUpdateBody,
        @PathVariable id: String
    ): ResponseEntity<ProductSchema> {
        return ResponseEntity.ok(
            productService.update(
                ProductSchema(
                    id = id,
                    name = body.name,
                    price = body.price
                )
            )
        )
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: String) = ResponseEntity.ok(productService.deleteById(id))
}