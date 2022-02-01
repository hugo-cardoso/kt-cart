package com.example.demo.services

import com.example.demo.exceptions.ProductNotFoundException
import com.example.demo.models.ProductFilter
import com.example.demo.repositories.ProductCustomRepository
import com.example.demo.repositories.ProductRepository
import com.example.demo.schemas.ProductSchema
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