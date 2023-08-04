package net.deechael.elements.core.impl.application

import net.deechael.elements.api.application.ElementApplication
import net.deechael.elements.api.application.ElementApplicationManager
import net.deechael.elements.api.application.source.ApplicationSource
import net.deechael.elements.event.ElementalDamageEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class ElementApplicationManagerImpl: ElementApplicationManager, Listener {

    private val applications = mutableMapOf<Int, ElementApplication>()

    @EventHandler
    fun playerQuiting(event: PlayerQuitEvent) {
        this.applications.remove(event.player.entityId)
    }

    override fun getApplication(entity: Entity): ElementApplication {
        return applications.getOrPut(entity.entityId) { ElementApplicationImpl(entity) }
    }

    override fun doElementalDamage(sufferer: Entity, source: ApplicationSource, damage: Double) {
        val newDamage = this.getApplication(sufferer)
            .applyElementWithDamage(source, damage)
        if (sufferer.isDead)
            return
        sufferer as LivingEntity
        val event = ElementalDamageEvent(
            sufferer,
            source,
            newDamage
        )
        Bukkit.getPluginManager().callEvent(event)
        sufferer.damage(event.damage)
    }

}