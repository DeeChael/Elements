package net.deechael.elements.api.application

import net.deechael.elements.api.application.source.ApplicationSource
import org.bukkit.entity.Entity

interface ElementApplicationManager {

    fun getApplication(entity: Entity): ElementApplication

    fun doElementalDamage(sufferer: Entity, source: ApplicationSource, damage: Double)

}