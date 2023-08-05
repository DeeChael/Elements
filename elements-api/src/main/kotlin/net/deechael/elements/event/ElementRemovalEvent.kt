package net.deechael.elements.event

import net.deechael.elements.api.ElementType
import net.deechael.elements.api.ElementTypeRemovalReason
import org.bukkit.entity.Entity
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityEvent

class ElementRemovalEvent(
    what: Entity,
    val reason: ElementTypeRemovalReason,
    val elementType: ElementType
) : EntityEvent(what) {

    override fun getHandlers(): HandlerList {
        return HANDLER_LIST
    }

    companion object {

        private val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLER_LIST
        }

    }

}