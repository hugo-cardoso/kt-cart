package com.example.demo.services

interface GenericService<T> {
    fun getAll(): List<T>
    fun getById(id: String): T
    fun create(cart: T): T
    fun update(cart: T): T
    fun deleteById(id: String): T
}