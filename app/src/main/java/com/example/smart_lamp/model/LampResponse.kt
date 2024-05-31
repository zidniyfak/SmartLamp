package com.example.smart_lamp.model

data class LampResponse(
    val lampId: String? = null,
    val lampName: String? = null,
    val hexCode: String? = null,
    val red: Int? = null,
    val green: Int? = null,
    val blue: Int? = null,
    val status: Int? = null,
)
