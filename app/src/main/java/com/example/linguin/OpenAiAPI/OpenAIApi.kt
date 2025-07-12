package com.example.linguin.OpenAiAPI

import com.example.linguin.Gpt.GPTRequest
import com.example.linguin.Gpt.GPTResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIApi {
    @POST("chat/completions")
    suspend fun generateResponse(
        @Header("Authorization") authHeader: String,
        @Body request: GPTRequest
    ): GPTResponse
}
