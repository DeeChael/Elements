package net.deechael.elements.api.application.source

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementSourceType
import net.deechael.elements.api.ElementType

interface ApplicationSource {

    fun getElementType(): ElementType

    fun getElementGauge(): ElementGauge

    fun getElementSourceType(): ElementSourceType

}