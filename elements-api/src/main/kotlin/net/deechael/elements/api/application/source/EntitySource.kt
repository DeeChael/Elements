package net.deechael.elements.api.application.source

import net.deechael.elements.api.ElementSourceType
import org.bukkit.entity.Entity

interface EntitySource : ApplicationSource {

    override fun getElementSourceType(): ElementSourceType {
        return ElementSourceType.ENTITY
    }

    fun getEntity(): Entity

}