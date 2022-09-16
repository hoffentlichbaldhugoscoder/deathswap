package de.toby.deathswap.config.implementation

import de.toby.deathswap.config.Properties

object Settings: Properties(name = "Settings") {
    var autoStart by value(true)
    var requiredPlayers by value(2)
    var offlineTime by value<Long>(60 * 5)
    var minTimeToSwap by value(60 * 5)
    var maxTimeToSwap by value(60 * 10)
    var announceSwap by value(true)
}