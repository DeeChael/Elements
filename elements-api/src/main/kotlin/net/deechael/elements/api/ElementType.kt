package net.deechael.elements.api

import net.deechael.elements.api.application.ApplicationTrigger
import java.awt.Color

interface ElementType {

    fun getId(): String

    fun getIcon(): String

    fun getColor(): Color

    fun isApplicable(): Boolean

    fun isReactable(): Boolean

    fun isReactionElement(): Boolean

    fun getTrigger(): ApplicationTrigger
}