package com.example.myapplication.AIpetApp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PetModelFactory(private val repository: PetRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PetViewModel(repository) as T
    }
}