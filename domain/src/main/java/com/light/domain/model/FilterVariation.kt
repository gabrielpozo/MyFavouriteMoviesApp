package com.light.domain.model


data class FilterVariation(
    var nameFilter: String,
    var isSelected: Boolean = false,
    var isAvailable: Boolean = false,
    var type: TYPE
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as FilterVariation

        return other.nameFilter == this.nameFilter
    }

    override fun hashCode(): Int {
        return nameFilter.hashCode()
    }
}


enum class TYPE {
    WATTAGE, COLOR, FINISH
}