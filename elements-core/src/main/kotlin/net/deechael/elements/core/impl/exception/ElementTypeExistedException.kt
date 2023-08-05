package net.deechael.elements.core.impl.exception

class ElementTypeExistedException(message: String) : RuntimeException(message) {

    constructor() : this("The element existed")

}