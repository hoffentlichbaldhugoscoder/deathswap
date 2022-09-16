package de.toby.deathswap.command

import de.toby.deathswap.game.Game
import de.toby.deathswap.game.implementation.Lobby
import de.toby.deathswap.ui.SettingsUI
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.sound.sound
import org.bukkit.ChatColor
import org.bukkit.Sound

object SettingsCommand {

    fun enable() {
        command("settings") {
            requiresPermission("deathSwap.settings")
            runs {
                if (Game.current !is Lobby) player.sendMessage("${ChatColor.RED}The game already started")
                else {
                    player.openGUI(SettingsUI.ui())
                    player.sound(Sound.BLOCK_CHEST_OPEN)
                }
            }
        }
    }
}