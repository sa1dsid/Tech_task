package com.example.tech_task.data

data class CharacterData(
    val id: Int,
    val image: String,
    val name: String,
    val status: String
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
