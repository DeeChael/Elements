package net.deechael.elements.api.reaction

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.application.source.ApplicationSource
import org.bukkit.entity.Entity

@FunctionalInterface
interface ElementReactionTrigger {

    fun trigger(
        reaction: ElementReaction,
        applicationSource: ApplicationSource,
        sufferer: Entity,
        formerElementTypeGauge: ElementGauge
    ) {
    }

    fun triggerDamage(
        reaction: ElementReaction,
        applicationSource: ApplicationSource,
        sufferer: Entity,
        formerElementTypeGauge: ElementGauge,
        originalDamage: Double
    ): Double {
        return originalDamage
    }

}