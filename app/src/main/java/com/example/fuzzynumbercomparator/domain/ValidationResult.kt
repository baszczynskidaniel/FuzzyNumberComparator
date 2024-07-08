package com.example.fuzzynumbercomparator.domain

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
