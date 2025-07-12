package com.example.myapplication.AIpetApp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface PetDao {
    @Query("SELECT * FROM pet LIMIT 1")
    suspend fun getPet(): Pet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet)



}