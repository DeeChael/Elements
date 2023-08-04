package net.deechael.elements.core.listener

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.core.ElementsPlugin
import net.deechael.elements.core.impl.application.source.EntitySourceImpl
import net.deechael.elements.core.impl.application.source.EnvironmentSourceImpl
import net.deechael.elements.core.registry.DefaultElementTypeRegistry
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityAirChangeEvent
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.ProjectileHitEvent

object EntityListeners: Listener {

    @EventHandler
    fun event(event: EntityDamageByBlockEvent) {
        if (event.damager == null)
            return
        if (event.damager!!.type == Material.MAGMA_BLOCK) {
            ElementsPlugin.getInstance()
                .getApplicationManager()
                .getApplication(event.entity)
                .applyElement(
                    EnvironmentSourceImpl(
                        event.damager!!.location,
                        DefaultElementTypeRegistry.PYRO,
                        ElementGauge(1)
                    )
                )
        }
    }

    @EventHandler
    fun event(event: EntityDamageByEntityEvent) {
        val damager = event.damager as LivingEntity
        val item = damager.equipment!!.itemInMainHand
        val itemMeta = item.itemMeta!!
        if (itemMeta.hasEnchant(Enchantment.FIRE_ASPECT)) {
            event.isCancelled = true
            val level = itemMeta.getEnchantLevel(Enchantment.FIRE_ASPECT).let {
                return@let if (it < 0) {
                    0
                } else {
                    it
                }
            }
            ElementsPlugin.getInstance()
                .getApplicationManager()
                .doElementalDamage(
                    event.entity,
                    EntitySourceImpl(
                        damager,
                        DefaultElementTypeRegistry.PYRO,
                        ElementGauge(1, level)
                    ),
                    event.finalDamage
                )
        }
    }

    @EventHandler
    fun event(event: EntityDamageEvent) {
        if (event.cause == DamageCause.LIGHTNING) {
            ElementsPlugin.getInstance()
                .getApplicationManager()
                .getApplication(event.entity)
                .applyElement(
                    EnvironmentSourceImpl(
                        event.entity.location,
                        DefaultElementTypeRegistry.ELECTRO,
                        ElementGauge(1)
                    )
                )
        }
    }

}