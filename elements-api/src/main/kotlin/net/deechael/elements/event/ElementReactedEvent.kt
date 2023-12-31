package net.deechael.elements.event

import net.deechael.elements.api.application.source.ApplicationSource
import net.deechael.elements.api.reaction.ElementReaction
import org.bukkit.entity.Entity
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityEvent

class ElementReactedEvent(
    what: Entity,
    val source: ApplicationSource,
    val reaction: ElementReaction
) : EntityEvent(what), Cancellable {

    private var cancelled = false

    override fun getHandlers(): HandlerList {
        return HANDLER_LIST
    }

    override fun isCancelled(): Boolean {
        return this.cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    companion object {

        private val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLER_LIST
        }

    }

}