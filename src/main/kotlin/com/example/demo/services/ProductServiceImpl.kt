package com.example.demo.services

import com.example.demo.exceptions.ProductNotFoundException
import com.example.demo.repositories.ProductRepository
import com.example.demo.schemas.ProductSchema
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    val productRepository: ProductRepository
): GenericService<ProductSchema> {

    override fun getById(id: String): ProductSchema = productRepository
        .findById(id)
        .orElseThrow { ProductNotFoundException() }

    override fun getAll(): List<ProductSchema> = productRepository.findAll()

    override fun create(product: ProductSchema) = productRepository.save(product)

    override fun update(product: ProductSchema): ProductSchema {
        val findProduct = getById(product.id.toString())

        product.name?.let { findProduct.name = it }
        product.price?.let { findProduct.price = it }

        return productRepository.save(findProduct)
    }

    override fun deleteById(id: String): ProductSchema {
        val findProduct = getById(id)

        productRepository.delete(findProduct)

        return findProduct
    }
}