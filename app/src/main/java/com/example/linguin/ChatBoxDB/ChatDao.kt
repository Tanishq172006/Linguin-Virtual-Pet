package com.example.myapplication.RetroChat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChatDao {
    @Query("SELECT * FROM ChatMessage ORDER BY timestamp ASC")
    fun getAllMessages(): LiveData<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM ChatMessage")
    suspend fun clearAll()
}
