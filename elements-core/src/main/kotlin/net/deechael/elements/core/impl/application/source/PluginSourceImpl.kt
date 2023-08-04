package net.deechael.elements.core.impl.application.source

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementType
import net.deechael.elements.api.application.source.PluginSource
import org.bukkit.plugin.Plugin

class PluginSourceImpl(
    val plugin: Plugin,
    elementType: ElementType,
    gauge: ElementGauge
) : ApplicationSourceImpl(elementType, gauge), PluginSource {

    override fun getPlugin(): Plugin {
        return this.plugin
    }

}