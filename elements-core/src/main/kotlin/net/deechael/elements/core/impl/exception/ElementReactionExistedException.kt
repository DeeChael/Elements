package net.deechael.elements.core.impl.exception

import java.lang.RuntimeException

class ElementReactionExistedException(message: String): RuntimeException(message) {

    constructor(): this("The reaction existed") {
    }

}