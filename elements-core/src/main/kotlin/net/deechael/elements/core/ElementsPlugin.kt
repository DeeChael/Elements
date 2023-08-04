package net.deechael.elements.core

import net.deechael.elements.api.application.ElementApplicationManager
import net.deechael.elements.api.ElementService
import net.deechael.elements.api.ElementType
import net.deechael.elements.api.application.ApplicationTrigger
import net.deechael.elements.api.reaction.ElementReaction
import net.deechael.elements.api.reaction.ElementReactionTrigger
import net.deechael.elements.core.impl.exception.ElementReactionExistedException
import net.deechael.elements.core.impl.exception.ElementTypeExistedException
import net.deechael.elements.core.impl.application.ElementApplicationManagerImpl
import net.deechael.elements.core.impl.ElementTypeImpl
import net.deechael.elements.core.impl.reaction.ElementReactionImpl
import net.deechael.elements.core.registry.DefaultElementReactionRegistry
import net.deechael.elements.core.registry.DefaultElementTypeRegistry
import org.bukkit.plugin.java.JavaPlugin
import java.awt.Color

class ElementsPlugin: JavaPlugin(), ElementService {

    private val elementTypes = mutableMapOf<String, ElementType>()
    private val elementReactions = mutableMapOf<String, ElementReaction>()
    private val elementReactionsWithFormer = mutableMapOf<ElementType, MutableList<ElementReaction>>()
    private val elementApplicationManager = ElementApplicationManagerImpl()

    override fun onEnable() {
        DefaultElementTypeRegistry.registerAll(this)
        DefaultElementReactionRegistry.registerAll(this)
    }

    override fun hasElementType(id: String): Boolean {
        return this.elementTypes.containsKey(id.lowercase())
    }

    override fun getElementType(id: String): ElementType {
        return this.elementTypes[id.lowercase()]!!
    }

    override fun getElementTypes(): List<ElementType> {
        return this.elementTypes.values.toList()
    }

    override fun registerElementType(
        id: String,
        color: Color,
        applicable: Boolean,
        reactable: Boolean,
        isReactionElement: Boolean,
        trigger: ApplicationTrigger
    ): ElementType {
        if (this.hasElementType(id.lowercase()))
            throw ElementTypeExistedException()
        val elementType = ElementTypeImpl(
            id.lowercase(),
            color,
            applicable,
            reactable,
            isReactionElement,
            trigger
        )
        this.elementTypes[id.lowercase()] = elementType
        return elementType
    }

    override fun hasElementReaction(id: String): Boolean {
        return this.elementReactions.containsKey(id.lowercase())
    }

    override fun getElementReaction(id: String): ElementReaction {
        return this.elementReactions[id.lowercase()]!!
    }

    override fun getElementReactions(): List<ElementReaction> {
        return this.elementReactions.values.toList()
    }

    override fun getElementReactionsWithFormerType(formerElementType: ElementType): List<ElementReaction> {
        return this.elementReactionsWithFormer.getOrPut(formerElementType, ::mutableListOf).toList()
    }

    override fun registerElementReaction(
        id: String,
        color: Color,
        formerElementType: ElementType,
        latterElementTypes: Array<ElementType>,
        trigger: ElementReactionTrigger
    ) : ElementReaction {
        if (this.hasElementReaction(id.lowercase()))
            throw ElementReactionExistedException()
        val elementReaction = ElementReactionImpl(
            id.lowercase(),
            color,
            formerElementType,
            latterElementTypes,
            trigger
        )
        this.elementReactionsWithFormer.getOrPut(formerElementType, ::mutableListOf).add(elementReaction)
        this.elementReactions[id.lowercase()] = elementReaction
        return elementReaction
    }

    override fun getApplicationManager(): ElementApplicationManager {
        return this.elementApplicationManager
    }

    companion object {

        @JvmStatic
        fun getInstance(): ElementsPlugin {
            return getPlugin(ElementsPlugin::class.java)
        }

    }

}