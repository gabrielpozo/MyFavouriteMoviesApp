package com.light.domain.model

data class FilterVariationCF(
    var codeFilter: Int,
    var isSelected: Boolean = false,
    var isAvailable: Boolean = false,
    var type: TYPE,
    var order: Int = -1
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as FilterVariationCF

        return other.codeFilter == this.codeFilter
    }

    override fun hashCode(): Int {
        return codeFilter.hashCode()
    }
}

enum class TYPE {
    WATTAGE, COLOR, FINISH
}