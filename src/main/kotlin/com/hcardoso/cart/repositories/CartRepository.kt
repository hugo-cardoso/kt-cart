package com.hcardoso.cart.repositories

import com.hcardoso.cart.schemas.CartSchema
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository: MongoRepository<CartSchema, String>