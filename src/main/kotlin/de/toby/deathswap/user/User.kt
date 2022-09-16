package de.toby.deathswap.user

import net.axay.kspigot.runnables.KSpigotRunnable
import org.bukkit.entity.Player
import java.util.*

private val players = mutableMapOf<UUID, User>()

class User {
    var state: UserState? = null
    var task: KSpigotRunnable? = null
}

fun Player.user() = players.computeIfAbsent(uniqueId) { User() }