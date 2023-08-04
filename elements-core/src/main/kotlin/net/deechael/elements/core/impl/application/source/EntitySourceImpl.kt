package net.deechael.elements.core.impl.application.source

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementType
import net.deechael.elements.api.application.source.EntitySource
import org.bukkit.entity.Entity

class EntitySourceImpl(
    private val entity: Entity,
    elementType: ElementType,
    gauge: ElementGauge
) : ApplicationSourceImpl(elementType, gauge), EntitySource {

    override fun getEntity(): Entity {
        return this.entity
    }

}