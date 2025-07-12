package com.example.myapplication.AIpetApp

class PetRepository(private val petDao: PetDao) {
    suspend fun getPet(): Pet = petDao.getPet() ?: Pet().also { petDao.insertPet(it) }

    suspend fun updatePet(pet: Pet) = petDao.insertPet(pet)
}