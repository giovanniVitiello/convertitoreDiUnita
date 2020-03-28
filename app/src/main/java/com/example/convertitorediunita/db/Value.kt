package com.example.convertitorediunita.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*03/02/2020 - $/£ - 8 */

@Entity(tableName = "conversion_table")
data class Value(
    @PrimaryKey
    val id: String,
    @ColumnInfo
    val dateConversion: String?,
    val typeConversion: String?,
    val conversion: String?
)
