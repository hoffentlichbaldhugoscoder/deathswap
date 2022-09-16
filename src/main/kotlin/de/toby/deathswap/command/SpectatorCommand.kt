package de.toby.deathswap.command

import de.toby.deathswap.game.Game
import de.toby.deathswap.game.implementation.Lobby
import de.toby.deathswap.user.UserState
import de.toby.deathswap.user.user
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import org.bukkit.ChatColor

object SpectatorCommand {

    fun enable() {
        command("spectator") {
            runs {
                if (Game.current !is Lobby) player.sendMessage("${ChatColor.RED}The game already started")
                else {
                    val user = player.user()
                    if (user.state == UserState.SPECTATING) {
                        user.state = UserState.PLAYING
                        player.sendMessage("${ChatColor.WHITE}You are no longer spectating")
                    } else {
                        user.state = UserState.SPECTATING
                        player.sendMessage("${ChatColor.WHITE}You are now spectating")
                    }
                }
            }
        }
    }
}