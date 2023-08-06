package net.deechael.elements.core.impl.mute

import net.deechael.elements.api.ElementType
import net.deechael.elements.api.mute.ElementMuteManager
import net.deechael.elements.core.ElementsPlugin
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.persistence.PersistentDataType

class ElementMuteManagerImpl : ElementMuteManager {

    private val muteElementsKey = NamespacedKey("elements", "mute")

    override fun addMuteElementType(entity: Entity, elementType: ElementType) {
        val newElements = this.getMuteElementTypes(entity).toMutableList()
        if (newElements.contains(elementType))
            return
        newElements.add(elementType)
        entity.persistentDataContainer.set(
            muteElementsKey,
            PersistentDataType.STRING,
            newElements.map(ElementType::getId).joinToString(";")
        )
    }

    override fun removeMuteElementType(entity: Entity, elementType: ElementType) {
        val newElements = this.getMuteElementTypes(entity).toMutableList()
        newElements.remove(elementType)
        entity.persistentDataContainer.set(
            muteElementsKey,
            PersistentDataType.STRING,
            newElements.map(ElementType::getId).joinToString(";")
        )
    }

    override fun getMuteElementTypes(entity: Entity): List<ElementType> {
        if (!entity.persistentDataContainer.has(muteElementsKey))
            return listOf()
        val rawElements = entity.persistentDataContainer.get(muteElementsKey, PersistentDataType.STRING)!!
        val elementIds = if (rawElements.contains(";"))
            rawElements.split(";")
        else
            listOf(rawElements)
        val elements = mutableListOf<ElementType>()
        for (elementId in elementIds) {
            if (!ElementsPlugin.getInstance().hasElementType(elementId))
                continue
            elements.add(ElementsPlugin.getInstance().getElementType(elementId))
        }
        return elements.toList()
    }

    override fun hasMuteElementType(entity: Entity, elementType: ElementType): Boolean {
        return this.getMuteElementTypes(entity).contains(elementType)
    }

}