package net.deechael.elements.core.impl.application.source

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementType
import net.deechael.elements.api.application.source.ApplicationSource

abstract class ApplicationSourceImpl(
    private val elementType: ElementType,
    private val gauge: ElementGauge
) : ApplicationSource {

    override fun getElementType(): ElementType {
        return this.elementType
    }

    override fun getElementGauge(): ElementGauge {
        return this.gauge
    }

}