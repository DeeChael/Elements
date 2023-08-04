package net.deechael.elements.api

import net.deechael.elements.api.application.ApplicationTrigger
import net.deechael.elements.api.application.ElementApplicationManager
import net.deechael.elements.api.reaction.ElementReaction
import net.deechael.elements.api.reaction.ElementReactionTrigger
import org.bukkit.Bukkit
import java.awt.Color

interface ElementService {

    fun hasElementType(id: String): Boolean

    fun getElementType(id: String): ElementType

    fun getElementTypes(): List<ElementType>

    fun registerElementType(id: String, color: Color, applicable: Boolean, reactable: Boolean, isReactionElement: Boolean = false, trigger: ApplicationTrigger = ApplicationTrigger.EMPTY_TRIGGER): ElementType

    fun hasElementReaction(id: String): Boolean

    fun getElementReaction(id: String): ElementReaction

    fun getElementReactions(): List<ElementReaction>

    fun getElementReactionsWithFormerType(formerElementType: ElementType): List<ElementReaction>

    fun registerElementReaction(id: String, color: Color, formerElementType: ElementType, latterElementTypes: Array<ElementType>, trigger: ElementReactionTrigger): ElementReaction

    fun getApplicationManager(): ElementApplicationManager

    companion object {

        @JvmStatic
        fun getService(): ElementService {
            return Bukkit.getPluginManager().getPlugin("Elements")!! as ElementService
        }

    }

}