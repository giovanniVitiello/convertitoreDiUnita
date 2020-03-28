package com.example.convertitorediunita.db

class WordRepository(private val valueDao: ValueDao) {

    suspend fun insert(value: Value) = valueDao.insert(value)

    suspend fun deleteAll() = valueDao.deleteAll()

    suspend fun getValue() = valueDao.getConversion()
}
