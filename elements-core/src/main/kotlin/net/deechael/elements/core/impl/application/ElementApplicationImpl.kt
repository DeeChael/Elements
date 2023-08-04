package net.deechael.elements.core.impl.application

import net.deechael.elements.api.*
import net.deechael.elements.api.application.ElementApplication
import net.deechael.elements.api.application.source.ApplicationSource
import net.deechael.elements.core.ElementsPlugin
import net.deechael.elements.event.ElementAppliedEvent
import net.deechael.elements.event.ElementReactedEvent
import net.deechael.elements.event.ElementRemovalEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class ElementApplicationImpl(private val entity: Entity) : ElementApplication {

    private val appliedElements = mutableMapOf<ElementType, ElementGauge>()
    private val elementCancellingTasks = mutableMapOf<ElementType, BukkitTask>()
    override fun clear() {
        this.elementCancellingTasks.values.forEach(BukkitTask::cancel)
        this.appliedElements.clear()
        this.elementCancellingTasks.clear()
    }

    override fun getAppliedElementTypes(): List<ElementType> {
        return this.appliedElements.keys.toList()
    }

    override fun getGauge(elementType: ElementType): ElementGauge {
        return this.appliedElements[elementType]!!
    }

    override fun applyElement(source: ApplicationSource) {
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
        source.getElementType().getTrigger().trigger(source, this.entity)
        if (elementCancellingTasks.containsKey(source.getElementType()))
            elementCancellingTasks[source.getElementType()]!!.cancel()
        elementCancellingTasks[source.getElementType()] = object : BukkitRunnable() {
            override fun run() {
                appliedElements.remove(source.getElementType())
                elementCancellingTasks.remove(source.getElementType())
                Bukkit.getPluginManager().callEvent(
                    ElementRemovalEvent(
                        this@ElementApplicationImpl.entity,
                        ElementTypeRemovalReason.RUNNING_OUT,
                        source.getElementType()
                    )
                )
            }
        }.runTaskLater(ElementsPlugin.getInstance(), (10.0 * source.getElementGauge().toDouble()).toLong() * 20L)
    }

    override fun applyElementWithDamage(source: ApplicationSource, damage: Double): Double {
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
                        val newDamage = reaction.getTrigger().triggerDamage(reaction, source, entity, this.getGauge(appliedElement), damage)
                        this.appliedElements.remove(appliedElement)
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
        source.getElementType().getTrigger().trigger(source, this.entity)
        if (elementCancellingTasks.containsKey(source.getElementType()))
            elementCancellingTasks[source.getElementType()]!!.cancel()
        elementCancellingTasks[source.getElementType()] = object : BukkitRunnable() {
            override fun run() {
                appliedElements.remove(source.getElementType())
                elementCancellingTasks.remove(source.getElementType())
                Bukkit.getPluginManager().callEvent(
                    ElementRemovalEvent(
                        this@ElementApplicationImpl.entity,
                        ElementTypeRemovalReason.RUNNING_OUT,
                        source.getElementType()
                    )
                )
            }
        }.runTaskLater(ElementsPlugin.getInstance(), (10.0 * source.getElementGauge().toDouble()).toLong() * 20L)
        return damage
    }

}