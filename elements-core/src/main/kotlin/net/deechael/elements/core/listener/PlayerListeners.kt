package net.deechael.elements.core.listener

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.core.ElementsPlugin
import net.deechael.elements.core.impl.application.source.EnvironmentSourceImpl
import net.deechael.elements.core.registry.DefaultElementTypeRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerMoveEvent

object PlayerListeners: Listener {

    @EventHandler
    fun event(event: PlayerMoveEvent) {
        if (event.player.isInWaterOrRainOrBubbleColumn) {
            ElementsPlugin.getInstance()
                .getApplicationManager()
                .getApplication(event.player)
                .applyElement(
                    EnvironmentSourceImpl(
                        event.to,
                        DefaultElementTypeRegistry.HYDRO,
                        ElementGauge(1)
                    )
                )
        }
        if (event.player.isInLava) {
            ElementsPlugin.getInstance()
                .getApplicationManager()
                .getApplication(event.player)
                .applyElement(
                    EnvironmentSourceImpl(
                        event.to,
                        DefaultElementTypeRegistry.PYRO,
                        ElementGauge(1)
                    )
                )
        }
        if (event.player.isInPowderedSnow) {
            ElementsPlugin.getInstance()
                .getApplicationManager()
                .getApplication(event.player)
                .applyElement(
                    EnvironmentSourceImpl(
                        event.to,
                        DefaultElementTypeRegistry.CRYO,
                        ElementGauge(1)
                    )
                )
        }
    }

}