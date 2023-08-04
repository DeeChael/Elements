package net.deechael.elements.core.impl.reaction

import net.deechael.elements.api.ElementType
import net.deechael.elements.api.reaction.ElementReaction
import net.deechael.elements.api.reaction.ElementReactionTrigger
import java.awt.Color

class ElementReactionImpl(
    private val id: String,
    private val color: Color,
    private val formerElementType: ElementType,
    private val latterElementTypes: Array<ElementType>,
    private val trigger: ElementReactionTrigger
): ElementReaction {

    override fun getId(): String {
        return this.id
    }

    override fun getColor(): Color {
        return this.color
    }

    override fun getFormerElementType(): ElementType {
        return this.formerElementType
    }

    override fun getLatterElementTypes(): Array<ElementType> {
        return this.latterElementTypes
    }

    override fun getTrigger(): ElementReactionTrigger {
        return this.trigger
    }

}