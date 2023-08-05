package net.deechael.elements.core.listener

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.core.ElementsPlugin
import net.deechael.elements.core.registry.DefaultElementTypeRegistry
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EntityListeners : Listener {

    @EventHandler
    fun event(event: EntityDamageByBlockEvent) {
        if (event.damager == null)
            return
        if (event.damager!!.type == Material.MAGMA_BLOCK) {
            ElementsPlugin.getInstance()
                .getApplicationManager()
                .getApplication(event.entity)
                .applyElement(
                    ElementsPlugin.getInstance()
                        .getSourceManager()
                        .environment(
                            event.damager!!.location,
                            DefaultElementTypeRegistry.PYRO,
                            ElementGauge(1)
                        )
                )
        }
    }

    @EventHandler
    fun event(event: EntityDamageByEntityEvent) {
        if (event.damager is LightningStrike) {
            ElementsPlugin.getInstance()
                .getApplicationManager()
                .getApplication(event.entity)
                .applyElement(
                    ElementsPlugin.getInstance()
                        .getSourceManager()
                        .environment(
                            event.entity.location,
                            DefaultElementTypeRegistry.ELECTRO,
                            ElementGauge(1)
                        )
                )
            return
        }
        if (event.damager !is LivingEntity)
            return
        val damager = event.damager as LivingEntity
        val item = damager.equipment!!.itemInMainHand
        val itemMeta = item.itemMeta ?: return
        if (itemMeta.hasEnchant(Enchantment.FIRE_ASPECT)) {
            event.damage = 0.0
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
                    ElementsPlugin.getInstance()
                        .getSourceManager()
                        .entity(
                            damager,
                            DefaultElementTypeRegistry.PYRO,
                            ElementGauge(1, level)
                        ),
                    event.finalDamage
                )
        }
    }

}