package net.deechael.elements.core.impl.application.source

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementType
import net.deechael.elements.api.application.source.*
import net.deechael.elements.api.reaction.ElementReaction
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin

class SourceManagerImpl : SourceManager {
    override fun entity(entity: Entity, elementType: ElementType, gauge: ElementGauge): EntitySource {
        return EntitySourceImpl(entity, elementType, gauge)
    }

    override fun environment(location: Location, elementType: ElementType, gauge: ElementGauge): EnvironmentSource {
        return EnvironmentSourceImpl(location, elementType, gauge)
    }

    override fun plugin(plugin: Plugin, elementType: ElementType, gauge: ElementGauge): PluginSource {
        return PluginSourceImpl(plugin, elementType, gauge)
    }

    override fun reaction(reaction: ElementReaction, elementType: ElementType, gauge: ElementGauge): ReactionSource {
        return ReactionSourceImpl(reaction, elementType, gauge)
    }

}