package com.example.myapplication.AIpetApp

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class PetViewModel(private val repository: PetRepository) : ViewModel() {

    private val _pet = MutableLiveData<Pet>()
    val pet: LiveData<Pet> = _pet

    init {
        loadPet()
    }

    private fun loadPet() {
        viewModelScope.launch {
            _pet.value = repository.getPet()
        }
    }

    fun feedPet() {
        _pet.value?.let {
            val updated = it.copy(hunger = (it.hunger - 10).coerceAtLeast(0))
            _pet.value = updated
            savePet(updated)
        }
    }

    fun playWithPet() {
        _pet.value?.let {
            val updated = it.copy(happiness = (it.happiness + 10).coerceAtMost(100))
            _pet.value = updated
            savePet(updated)
        }
    }

    private fun savePet(pet: Pet) {
        viewModelScope.launch {
            repository.updatePet(pet)
        }
    }

    fun increaseHunger(amount: Int) {
        _pet.value?.let {
            it.hunger += amount
            _pet.value = it
            savePet(it)
        }
    }

    fun increaseSadness(amount: Int) {
        _pet.value?.let {
            it.sad += amount
            _pet.value = it
            savePet(it)
        }
    }

    fun decreaseHappiness(amount: Int) {
        _pet.value?.let {
            it.happiness = (it.happiness - amount).coerceAtLeast(0)
            _pet.value = it
            savePet(it)
        }
    }

    fun increaseHappiness(amount: Int) {
        _pet.value?.let {
            it.happiness = (it.happiness + amount).coerceAtMost(100)
            _pet.value = it
            savePet(it)
        }
    }
}
