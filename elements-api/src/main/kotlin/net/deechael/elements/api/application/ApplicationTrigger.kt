package net.deechael.elements.api.application

import net.deechael.elements.api.application.source.ApplicationSource
import org.bukkit.entity.Entity

@FunctionalInterface
fun interface ApplicationTrigger {

    fun trigger(applicationSource: ApplicationSource, sufferer: Entity)

    companion object {

        @JvmStatic
        val EMPTY_TRIGGER = ApplicationTrigger { _, _ -> }

    }

}