package com.example.convertitorediunita.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ValueDao {

    @Query("SELECT * from conversion_table ORDER BY dateConversion DESC")
    suspend fun getConversion(): List<Value>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: Value)

    @Query("DELETE FROM conversion_table")
    suspend fun deleteAll()
}
