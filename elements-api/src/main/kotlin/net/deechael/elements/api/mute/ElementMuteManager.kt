package net.deechael.elements.api.mute

import net.deechael.elements.api.ElementType
import org.bukkit.entity.Entity

interface ElementMuteManager {

    fun addMuteElementType(entity: Entity, elementType: ElementType)

    fun removeMuteElementType(entity: Entity, elementType: ElementType)

    fun getMuteElementTypes(entity: Entity): List<ElementType>

    fun hasMuteElementType(entity: Entity, elementType: ElementType): Boolean

}