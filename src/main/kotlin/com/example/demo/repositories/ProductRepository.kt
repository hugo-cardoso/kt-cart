package com.example.demo.repositories

import com.example.demo.schemas.ProductSchema
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: MongoRepository<ProductSchema, String>