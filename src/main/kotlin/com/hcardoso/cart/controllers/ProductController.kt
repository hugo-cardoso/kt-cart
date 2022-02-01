package com.hcardoso.cart.controllers

import com.hcardoso.cart.models.ProductCreateBody
import com.hcardoso.cart.models.ProductFilter
import com.hcardoso.cart.models.ProductUpdateBody
import com.hcardoso.cart.schemas.ProductSchema
import com.hcardoso.cart.services.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getProducts(
        @RequestParam(required = false) name: String? = null,
        @RequestParam(required = false) minPrice: Float? = null,
        @RequestParam(required = false) maxPrice: Float? = null,
    ): ResponseEntity<List<ProductSchema>> {
        val filter = ProductFilter()

        name?.let { filter.name = it }
        minPrice?.let { filter.minPrice = it }
        maxPrice?.let { filter.maxPrice = it }

        return if (filter.hasFilter()) {
            ResponseEntity.ok(productService.findByFilter(filter))
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