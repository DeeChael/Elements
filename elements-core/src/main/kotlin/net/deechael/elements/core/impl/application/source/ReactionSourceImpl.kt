package net.deechael.elements.core.impl.application.source

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementType
import net.deechael.elements.api.application.source.ReactionSource
import net.deechael.elements.api.reaction.ElementReaction

class ReactionSourceImpl(val reaction: ElementReaction, elementType: ElementType, gauge: ElementGauge) : ApplicationSourceImpl(elementType, gauge), ReactionSource {

    override fun getReaction(): ElementReaction {
        return this.reaction
    }

}