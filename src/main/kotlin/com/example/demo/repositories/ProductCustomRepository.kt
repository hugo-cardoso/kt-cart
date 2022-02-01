package com.example.demo.repositories

import com.example.demo.schemas.ProductSchema
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
class ProductCustomRepository(private val mongoTemplate: MongoTemplate) {

    fun query(name: String?, price: Float?): List<ProductSchema> {
        val query = Query()
        val criteriaList: ArrayList<Criteria> = ArrayList()

        name?.let { criteriaList.add(Criteria.where("name").`is`(it)) }
        price?.let { criteriaList.add(Criteria.where("price").`is`(it)) }

        query.addCriteria(Criteria().andOperator(*criteriaList.toTypedArray()))

        return mongoTemplate.find(query, ProductSchema::class.java)
    }
}