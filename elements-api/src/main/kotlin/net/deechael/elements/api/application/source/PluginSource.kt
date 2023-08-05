package net.deechael.elements.api.application.source

import net.deechael.elements.api.ElementSourceType
import org.bukkit.plugin.Plugin

interface PluginSource : ApplicationSource {

    override fun getElementSourceType(): ElementSourceType {
        return ElementSourceType.PLUGIN
    }

    fun getPlugin(): Plugin

}