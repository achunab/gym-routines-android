package com.example.gymroutines.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entrenos")
data class Entreno (
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val fecha: String,
    val tipo: String = "4x15 Pierna",
    val seriesCompletadas: Int = 4
)