package com.example.fuzzynumbercomparator.fuzzynumber

enum class FuzzyNumberType {
    TRIANGLE,
    TRAPEZOID;
    override fun toString(): String {
        return when(this) {
            TRIANGLE -> "Triangle"
            TRAPEZOID -> "Trapezoid"
        }
    }
    companion object {
        fun fromString(name: String): FuzzyNumberType? {
            return entries.find { it.toString() == name }
        }

        fun toListOfStrings(): List<String> {
            return entries.map { it.toString() }
        }
    }
}
