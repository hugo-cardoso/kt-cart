package com.example.demo.services

import com.example.demo.repositories.ProductRepository
import com.example.demo.schemas.ProductSchema
import org.springframework.stereotype.Service

@Service
class ProductService(
    val productRepository: ProductRepository
) {
    fun getById(id: String): ProductSchema {
        val findProduct = productRepository.findById(id)

        if (findProduct.isEmpty) throw object: Exception("Product not found"){}

        return findProduct.get()
    }

    fun getAll(): List<ProductSchema> = productRepository.findAll()

    fun create(product: ProductSchema): ProductSchema = productRepository.save(product)

    fun update(product: ProductSchema): ProductSchema {
        val findProduct = productRepository.findById(product.id.toString())

        if (findProduct.isEmpty) throw object: Exception("Product not found"){}

        val newProduct = productRepository.save(
            product.apply {
                this.id = findProduct.get().id
                this.name = product.name
                this.price = product.price
            }
        )

        return newProduct
    }

    fun delete(id: String): ProductSchema {
        val findProduct = productRepository.findById(id)

        if (findProduct.isEmpty) throw object: Exception("Product not found"){}

        productRepository.delete(findProduct.get())

        return findProduct.get()
    }
}