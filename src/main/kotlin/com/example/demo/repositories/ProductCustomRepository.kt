package com.example.demo.repositories

import com.example.demo.models.ProductFilter
import com.example.demo.schemas.ProductSchema
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
class ProductCustomRepository(private val mongoTemplate: MongoTemplate) {

    fun query(filter: ProductFilter): List<ProductSchema> {
        val query = Query()
        val criteriaList: ArrayList<Criteria> = ArrayList()

        filter.name?.let { criteriaList.add(Criteria.where("name").`is`(it)) }
        filter.minPrice?.let { criteriaList.add(Criteria.where("price").gte(it)) }
        filter.maxPrice?.let { criteriaList.add(Criteria.where("price").lte(it)) }

        query.addCriteria(Criteria().andOperator(*criteriaList.toTypedArray()))

        return mongoTemplate.find(query, ProductSchema::class.java)
    }
}