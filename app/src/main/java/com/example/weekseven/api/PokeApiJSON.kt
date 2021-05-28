package com.example.weekseven.api

import com.example.weekseven.model.Result

data class PokeApiJSON(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: ArrayList<Result>
)