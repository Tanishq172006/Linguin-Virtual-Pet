package com.example.myapplication.AIpetApp


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.virtualpet.data.PetDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PetWorkManager(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val dao = PetDatabase.getInstance(applicationContext).petDao()
            val pet = dao.getPet() ?: return@withContext Result.success()

            val updatedPet = pet.copy(
                hunger = (pet.hunger + 5).coerceAtMost(100),
                happiness = (pet.happiness - 5).coerceAtLeast(0)
            )

            dao.insertPet(updatedPet)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}

