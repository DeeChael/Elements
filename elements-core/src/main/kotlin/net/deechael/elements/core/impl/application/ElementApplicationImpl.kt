package net.deechael.elements.core.impl.application

import kotlinx.coroutines.*
import kotlinx.coroutines.Runnable
import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementType
import net.deechael.elements.api.ElementTypeRemovalReason
import net.deechael.elements.api.application.ElementApplication
import net.deechael.elements.api.application.source.ApplicationSource
import net.deechael.elements.core.ElementsPlugin
import net.deechael.elements.event.ElementAppliedEvent
import net.deechael.elements.event.ElementReactedEvent
import net.deechael.elements.event.ElementRemovalEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Entity

class ElementApplicationImpl(private val entity: Entity) : ElementApplication {

    private val appliedElements = mutableMapOf<ElementType, ElementGauge>()
    private val expiredTime = mutableMapOf<ElementType, Long>()

    override fun clear() {
        this.appliedElements.clear()
        this.expiredTime.clear()
    }

    override fun getAppliedElementTypes(): List<ElementType> {
        return this.appliedElements.keys.toList()
    }

    override fun getGauge(elementType: ElementType): ElementGauge {
        return this.appliedElements[elementType]!!
    }

    override fun applyElement(source: ApplicationSource) {
        if (ElementsPlugin.getInstance().getMuteManager().hasMuteElementType(this.entity, source.getElementType()))
            return
        if (source.getElementGauge().toDouble() <= 0)
            return
        if (source.getElementType().isReactable()) {
            for (appliedElement in this.appliedElements.keys) {
                for (reaction in ElementsPlugin.getInstance().getElementReactionsWithFormerType(appliedElement)) {
                    if (reaction.getLatterElementTypes().contains(source.getElementType())) {
                        val event = ElementReactedEvent(
                            this.entity,
                            source,
                            reaction
                        )
                        Bukkit.getPluginManager().callEvent(event)
                        reaction.getTrigger().trigger(reaction, source, entity, this.getGauge(appliedElement))
                        this.appliedElements.remove(appliedElement)
                        this.expiredTime.remove(appliedElement)
                        return
                    }
                }
            }
        }
        if (!source.getElementType().isApplicable())
            return
        val event = ElementAppliedEvent(
            this.entity,
            source
        )
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled)
            return
        this.appliedElements[source.getElementType()] = source.getElementGauge()
        this.expiredTime[source.getElementType()] =
            System.currentTimeMillis() + ((10.0 * source.getElementGauge().toDouble()).toLong() * 1000L)
        source.getElementType().getTrigger().trigger(source, this.entity)
    }

    override fun applyElementWithDamage(source: ApplicationSource, damage: Double): Double {
        if (ElementsPlugin.getInstance().getMuteManager().hasMuteElementType(this.entity, source.getElementType()))
            return 0.0
        if (source.getElementGauge().toDouble() <= 0)
            return damage
        if (source.getElementType().isReactable()) {
            for (appliedElement in this.appliedElements.keys) {
                for (reaction in ElementsPlugin.getInstance().getElementReactionsWithFormerType(appliedElement)) {
                    if (reaction.getLatterElementTypes().contains(source.getElementType())) {
                        val event = ElementReactedEvent(
                            this.entity,
                            source,
                            reaction
                        )
                        Bukkit.getPluginManager().callEvent(event)
                        reaction.getTrigger().trigger(reaction, source, entity, this.getGauge(appliedElement))
                        val newDamage = reaction.getTrigger()
                            .triggerDamage(reaction, source, entity, this.getGauge(appliedElement), damage)
                        this.appliedElements.remove(appliedElement)
                        this.expiredTime.remove(appliedElement)
                        return newDamage
                    }
                }
            }
        }
        if (!source.getElementType().isApplicable())
            return damage
        val event = ElementAppliedEvent(
            this.entity,
            source
        )
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled)
            return 0.0
        this.appliedElements[source.getElementType()] = source.getElementGauge()
        this.expiredTime[source.getElementType()] =
            System.currentTimeMillis() + ((10.0 * source.getElementGauge().toDouble()).toLong() * 1000L)
        source.getElementType().getTrigger().trigger(source, this.entity)
        return damage
    }

    private fun callExpired(type: ElementType) {
        appliedElements.remove(type)
        expiredTime.remove(type)
        Bukkit.getScheduler().runTask(ElementsPlugin.getInstance(), Runnable {
            Bukkit.getPluginManager().callEvent(
                ElementRemovalEvent(
                    this@ElementApplicationImpl.entity,
                    ElementTypeRemovalReason.RUNNING_OUT,
                    type
                )
            )
        })
    }

    fun checkTimeOut() {
        val current = System.currentTimeMillis()
        for (elementType in this.getAppliedElementTypes()) {
            if (this.expiredTime[elementType]!! <= current) {
                runBlocking {
                    callExpired(elementType)
                }
            }
        }
    }

}