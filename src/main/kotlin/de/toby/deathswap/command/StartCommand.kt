package de.toby.deathswap.command

import de.toby.deathswap.game.Game
import de.toby.deathswap.game.implementation.Lobby
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import org.bukkit.ChatColor

object StartCommand {

    fun enable() {
        command("start") {
            requiresPermission("deathSwap.forceStart")
            runs {
                val current = Game.current

                if (current !is Lobby) player.sendMessage("${ChatColor.RED}The game already started")
                else if (!current.idle) player.sendMessage("${ChatColor.RED}The countdown already started")
                else current.idle = false
            }
        }
    }
}