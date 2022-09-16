package de.toby.deathswap.game

object Game {
    var current: Phase? = null
    set(value) {
        field?.stop()
        field = value
    }
}