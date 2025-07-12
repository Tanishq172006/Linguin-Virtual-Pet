package com.example.linguin.Gpt

data class GPTRequest(
    val model: String,
    val messages: List<UserMessage>
)