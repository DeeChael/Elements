package net.deechael.elements.api.application.source

import net.deechael.elements.api.ElementSourceType
import net.deechael.elements.api.reaction.ElementReaction

interface ReactionSource : ApplicationSource {

    override fun getElementSourceType(): ElementSourceType {
        return ElementSourceType.REACTION
    }

    fun getReaction(): ElementReaction

}