package net.deechael.elements.core.impl.application.source

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementType
import net.deechael.elements.api.application.source.EnvironmentSource
import org.bukkit.Location

class EnvironmentSourceImpl(
    private val location: Location,
    elementType: ElementType,
    gauge: ElementGauge
) : ApplicationSourceImpl(elementType, gauge), EnvironmentSource {

    override fun getLocation(): Location {
        return this.location
    }

}