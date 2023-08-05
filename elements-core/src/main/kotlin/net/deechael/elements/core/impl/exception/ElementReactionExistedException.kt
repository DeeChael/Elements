package net.deechael.elements.core.impl.exception

class ElementReactionExistedException(message: String) : RuntimeException(message) {

    constructor() : this("The reaction existed")

}