package com.example.myapplication.AIpetApp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet")
data class Pet(
    @PrimaryKey val id: Int = 0,
    var happiness: Int = 50,
    var hunger: Int = 50,
    var sad: Int = 0
)


