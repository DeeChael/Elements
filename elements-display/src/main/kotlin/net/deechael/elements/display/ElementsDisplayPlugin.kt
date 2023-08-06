package net.deechael.elements.display

import net.deechael.elements.api.ElementService
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityInteractEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class ElementsDisplayPlugin: JavaPlugin(), Listener {

    private lateinit var service: ElementService

    private val cache = mutableMapOf<UUID, BukkitTask>()

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        cache[player.uniqueId] = object : BukkitRunnable() {
            override fun run() {
                var component: Component = Component.empty()
                for (element in service.getApplicationManager().getApplication(player).getAppliedElementTypes()) {
                    component = component.append(Component.text(element.getIcon()).color(TextColor.color(element.getColor().rgb))).appendSpace()
                }
                player.sendActionBar(component)
            }
        }.runTaskTimerAsynchronously(this, 0L, 20L)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        cache[event.player.uniqueId]?.cancel()
    }

    @EventHandler
    fun onInteract(event: PlayerInteractAtEntityEvent) {
        if (!this.service.getApplicationManager().hasApplied(event.rightClicked))
            return
        if (event.hand == EquipmentSlot.OFF_HAND)
            return
        var component: Component = Component.text("Applied Elements:")
        for (element in service.getApplicationManager().getApplication(event.rightClicked).getAppliedElementTypes()) {
            component = component.appendSpace().append(Component.text(element.getIcon()).color(TextColor.color(element.getColor().rgb)))
        }
        event.player.sendMessage(component)
    }

    override fun onEnable() {
        this.service = ElementService.getService()
        Bukkit.getPluginManager().registerEvents(this, this)
    }

}