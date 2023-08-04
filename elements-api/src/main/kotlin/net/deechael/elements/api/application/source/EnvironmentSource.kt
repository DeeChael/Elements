package net.deechael.elements.api.application.source

import net.deechael.elements.api.ElementSourceType
import org.bukkit.Location

interface EnvironmentSource: ApplicationSource {

    override fun getElementSourceType(): ElementSourceType {
        return ElementSourceType.ENVIRONMENT
    }

    fun getLocation(): Location

}