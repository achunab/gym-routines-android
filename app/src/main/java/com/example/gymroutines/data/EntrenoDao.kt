package com.example.gymroutines.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EntrenoDao {
    @Insert
    suspend fun insertar(entreno: Entreno)

    @Query("SELECT * FROM entrenos ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Entreno>>

    @Query("DELETE FROM entrenos")
    suspend fun borrarTodo()

    @Query("SELECT * FROM entrenos ORDER BY id DESC")
    suspend fun obtenerTodosSync(): List<Entreno>
}