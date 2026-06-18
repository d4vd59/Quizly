package com.quizly.android.data.model

import kotlinx.serialization.Serializable

/** Ported from Kategorie.cs / kategorie.py */
@Serializable
data class Kategorie(
    val id: Int,
    val name: String
)
