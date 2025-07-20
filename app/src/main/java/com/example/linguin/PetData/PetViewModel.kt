package com.example.myapplication.AIpetApp

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class PetViewModel(private val repository: PetRepository) : ViewModel() {

    private val _pet = MutableLiveData<Pet>()
    val pet: LiveData<Pet> = _pet

    var hasHandledShoppingItem: Boolean = false

    init {
        refreshPet()
    }

    fun refreshPet() {
        viewModelScope.launch {
            _pet.value = repository.getPet()
        }
    }

    private fun savePet(pet: Pet) {
        viewModelScope.launch {
            repository.updatePet(pet)
        }
    }

    fun feedPet(amount: Int) {
        _pet.value?.let {
            val newHunger = (it.hunger - amount).coerceAtLeast(0)
            val updated = it.copy(hunger = newHunger, sad = (100 - it.happiness).coerceIn(0, 100))
            _pet.value = updated
            savePet(updated)
        }
    }

    fun setHappiness(value: Int) {
        _pet.value?.let {
            val happiness = value.coerceIn(0, 100)
            val sadness = (100 - happiness).coerceIn(0, 100)
            val updated = it.copy(happiness = happiness, sad = sadness)
            _pet.value = updated
            savePet(updated)
        }
    }

    fun setHappinessRelative(delta: Int) {
        _pet.value?.let {
            setHappiness((it.happiness + delta).coerceIn(0, 100))
        }
    }

    fun setSadness(value: Int) {
        _pet.value?.let {
            val sadness = value.coerceIn(0, 100)
            val happiness = (100 - sadness).coerceIn(0, 100)
            val updated = it.copy(sad = sadness, happiness = happiness)
            _pet.value = updated
            savePet(updated)
        }
    }

    fun setSadnessRelative(delta: Int) {
        _pet.value?.let {
            setSadness((it.sad + delta).coerceIn(0, 100))
        }
    }
}
