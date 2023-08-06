package net.deechael.elements.core.impl

import net.deechael.elements.api.ElementType
import net.deechael.elements.api.application.ApplicationTrigger
import java.awt.Color

class ElementTypeImpl(
    private val id: String,
    private val icon: String,
    private val color: Color,
    private val applicable: Boolean,
    private val reactable: Boolean,
    private val isReactionElement: Boolean,
    private val trigger: ApplicationTrigger
) : ElementType {

    override fun getId(): String {
        return this.id
    }

    override fun getIcon(): String {
        return this.icon
    }

    override fun getColor(): Color {
        return this.color
    }

    override fun isApplicable(): Boolean {
        return this.applicable
    }

    override fun isReactable(): Boolean {
        return this.reactable
    }

    override fun isReactionElement(): Boolean {
        return this.isReactionElement
    }

    override fun getTrigger(): ApplicationTrigger {
        return this.trigger
    }

}