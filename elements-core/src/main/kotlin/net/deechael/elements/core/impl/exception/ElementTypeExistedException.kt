package net.deechael.elements.core.impl.exception

import java.lang.RuntimeException

class ElementTypeExistedException(message: String): RuntimeException(message) {

    constructor(): this("The element existed") {
    }

}