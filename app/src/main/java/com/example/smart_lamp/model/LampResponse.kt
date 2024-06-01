package com.example.smart_lamp.model

data class LampResponse(
    val lampId: String? = null,
    val lampName: String = "",
    val hexCode: String = "",
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0,
    var localStatus: Int = 0, // Mutable for UI update
    val firebaseStatus: Int = 0,
) {
    constructor() : this("", "", "", 0, 0, 0, 0, 0)

    fun copyWithStatus(localStatus: Int, firebaseStatus: Int): LampResponse {
        return this.copy(localStatus = localStatus, firebaseStatus = firebaseStatus)
    }
}
