package net.deechael.elements.api.application

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementType
import net.deechael.elements.api.application.source.ApplicationSource
import org.bukkit.entity.Entity

interface ElementApplication {

    fun clear()

    fun getAppliedElementTypes(): List<ElementType>

    fun getGauge(elementType: ElementType): ElementGauge

    fun applyElement(source: ApplicationSource)

    fun applyElementWithDamage(source: ApplicationSource, damage: Double): Double

}