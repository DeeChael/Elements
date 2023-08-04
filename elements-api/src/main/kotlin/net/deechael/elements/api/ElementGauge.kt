package net.deechael.elements.api

data class ElementGauge(
    val unit: Int,
    val decimal: Int = 0
) {

    fun toDouble(): Double {
        return "$unit.$decimal".toDouble()
    }

    fun multiple(value: Double): ElementGauge {
        val newValue = this.toDouble() * value
        val newUnit = ((newValue - (newValue % 1)) / 1).toInt()
        val newDecimal = (((newValue - newUnit) - ((newValue - newUnit) % 0.1)) / 0.1).toInt()
        return ElementGauge(newUnit, newDecimal)
    }

}
