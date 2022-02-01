package com.hcardoso.cart.repositories

import com.hcardoso.cart.schemas.ProductSchema
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: MongoRepository<ProductSchema, String>