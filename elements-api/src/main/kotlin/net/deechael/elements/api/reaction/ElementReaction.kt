package net.deechael.elements.api.reaction

import net.deechael.elements.api.ElementType
import java.awt.Color

interface ElementReaction {

    fun getId(): String

    fun getColor(): Color

    fun getFormerElementType(): ElementType

    fun getLatterElementTypes(): Array<ElementType>

    fun getTrigger(): ElementReactionTrigger

}