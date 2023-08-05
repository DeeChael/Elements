package net.deechael.elements.api.application.source

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementType
import net.deechael.elements.api.reaction.ElementReaction
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin

interface SourceManager {

    fun entity(
        entity: Entity,
        elementType: ElementType,
        gauge: ElementGauge
    ): EntitySource

    fun environment(
        location: Location,
        elementType: ElementType,
        gauge: ElementGauge
    ): EnvironmentSource

    fun plugin(
        plugin: Plugin,
        elementType: ElementType,
        gauge: ElementGauge
    ): PluginSource

    fun reaction(
        reaction: ElementReaction,
        elementType: ElementType,
        gauge: ElementGauge
    ): ReactionSource

}