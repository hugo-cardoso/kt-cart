package com.hcardoso.cart.services

import com.hcardoso.cart.exceptions.ProductNotFoundException
import com.hcardoso.cart.models.ProductFilter
import com.hcardoso.cart.repositories.ProductCustomRepository
import com.hcardoso.cart.repositories.ProductRepository
import com.hcardoso.cart.schemas.ProductSchema
import org.springframework.stereotype.Service

@Service
class ProductService(
    val productRepository: ProductRepository,
    val productCustomRepository: ProductCustomRepository
) {

    fun getById(id: String): ProductSchema = productRepository
        .findById(id)
        .orElseThrow { ProductNotFoundException() }

    fun getAll(): List<ProductSchema> = productRepository.findAll()

    fun create(product: ProductSchema) = productRepository.save(product)

    fun update(product: ProductSchema): ProductSchema {
        val findProduct = getById(product.id.toString())

        product.name?.let { findProduct.name = it }
        product.price?.let { findProduct.price = it }

        return productRepository.save(findProduct)
    }

    fun deleteById(id: String): ProductSchema {
        val findProduct = getById(id)

        productRepository.delete(findProduct)

        return findProduct
    }

    fun findByFilter(filter: ProductFilter) = productCustomRepository.query(filter)
}