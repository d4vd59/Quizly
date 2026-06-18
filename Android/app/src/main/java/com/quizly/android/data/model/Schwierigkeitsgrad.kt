package com.quizly.android.data.model

import kotlinx.serialization.Serializable

/** Ported from Schwierigkeitsgrad.cs / schwierigkeitsgrad.py */
@Serializable
data class Schwierigkeitsgrad(
    val level: Int,
    val description: String? = null
)
