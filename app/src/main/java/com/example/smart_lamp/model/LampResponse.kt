package com.example.smart_lamp.model

data class LampResponse(
    val lampId: String,
    val lampName: String,
    val hexCode: String,
    val red: Int,
    val green: Int,
    val blue: Int,
    val status: Int,
)
