package com.light.domain.model


data class FilterWattage(val nameFilter: String, var isSelected: Boolean = false) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as FilterWattage

        return other.nameFilter == this.nameFilter
    }

    override fun hashCode(): Int {
        return nameFilter.hashCode()
    }
}

data class FilterColor(val nameFilter: String, var isSelected: Boolean = false){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as FilterColor

        return other.nameFilter == this.nameFilter
    }

    override fun hashCode(): Int {
        return nameFilter.hashCode()
    }

}
data class FilterFinish(val nameFilter: String, var isSelected: Boolean = false) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as FilterFinish

        return other.nameFilter == this.nameFilter
    }

    override fun hashCode(): Int {
        return nameFilter.hashCode()
    }
}


enum class TYPE {
    SPEC1, SPEC3, PRODUCT_SCENE
}