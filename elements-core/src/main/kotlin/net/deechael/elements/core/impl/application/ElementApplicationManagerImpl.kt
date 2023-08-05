package net.deechael.elements.core.impl.application

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import net.deechael.elements.api.application.ElementApplication
import net.deechael.elements.api.application.ElementApplicationManager
import net.deechael.elements.api.application.source.ApplicationSource
import net.deechael.elements.event.ElementalDamageEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.time.Duration

class ElementApplicationManagerImpl : ElementApplicationManager, Listener {

    private val applications = Caffeine.newBuilder()
        .maximumSize(10000L)
        .expireAfterWrite(Duration.ofMinutes(5))
        .expireAfterAccess(Duration.ofMinutes(5))
        .evictionListener<Int, ElementApplicationImpl> { key, value, cause ->
            if (cause != RemovalCause.EXPIRED)
                return@evictionListener
            if (value!!.getAppliedElementTypes().isEmpty())
                return@evictionListener
            this.recache(key!!, value)
        }
        .build<Int, ElementApplicationImpl>()

    @EventHandler
    fun playerQuiting(event: PlayerQuitEvent) {
        this.applications.invalidate(event.player.entityId)
    }

    @EventHandler
    fun entityDying(event: EntityDeathEvent) {
        this.applications.invalidate(event.entity.entityId)
    }

    override fun hasApplied(entity: Entity): Boolean {
        return this.applications.asMap().containsKey(entity.entityId)
    }

    override fun getApplication(entity: Entity): ElementApplication {
        return applications.get(entity.entityId) { ElementApplicationImpl(entity) }
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

    private fun recache(key: Int, value: ElementApplicationImpl) {
        this.applications.put(key, value)
    }

    suspend fun checkTimeout() {
        for (application in this.applications.asMap().values) {
            application.checkTimeOut()
        }
    }

    fun clear() {
        this.applications.cleanUp()
    }

}