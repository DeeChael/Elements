package net.deechael.elements.event

import net.deechael.elements.api.application.source.ApplicationSource
import org.bukkit.entity.Entity
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityEvent

class ElementalDamageEvent(
    what: Entity,
    val source: ApplicationSource,
    var damage: Double
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