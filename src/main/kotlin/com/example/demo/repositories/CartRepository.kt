package com.example.demo.repositories

import com.example.demo.schemas.CartSchema
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository: MongoRepository<CartSchema, String>