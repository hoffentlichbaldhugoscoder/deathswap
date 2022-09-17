package de.toby.deathswap.ui

import de.toby.deathswap.config.implementation.Settings
import de.toby.deathswap.game.Game
import de.toby.deathswap.game.implementation.Lobby
import net.axay.kspigot.chat.literalText
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import net.axay.kspigot.sound.sound
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.player.PlayerInteractEvent

object SettingsUI {

    val item = itemStack(Material.COMPARATOR) {
        meta { name = literalText("${ChatColor.AQUA}Settings") }
    }

    fun enable() {
        listen<PlayerInteractEvent> {
            if (it.item?.isSimilar(item) == true) {
                it.player.performCommand("settings")
                it.isCancelled = true
            }
        }
    }

    fun ui() = kSpigotGUI(GUIType.THREE_BY_NINE) {
        defaultPage = 0
        title = literalText("Settings")
        page(0) {
            placeholder(Slots.All, itemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE) {
                meta { name = literalText() }
            })

            button(Slots.RowTwoSlotTwo, autoStart()) {
                Settings.autoStart = !Settings.autoStart
                val lobby = Game.current as? Lobby ?: return@button
                lobby.idle = !(lobby.playerSize() >= Settings.requiredPlayers && Settings.autoStart)
                it.player.sound(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE)
                it.bukkitEvent.currentItem = autoStart()
            }

            button(Slots.RowTwoSlotThree, requiredPlayers()) {
                if (it.bukkitEvent.isLeftClick) Settings.requiredPlayers += 1
                else if (it.bukkitEvent.isRightClick && Settings.requiredPlayers > 0) Settings.requiredPlayers -= 1
                val lobby = Game.current as? Lobby ?: return@button
                lobby.idle = !(lobby.playerSize() >= Settings.requiredPlayers && Settings.autoStart)
                it.player.sound(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE)
                it.bukkitEvent.currentItem = requiredPlayers()
            }

            button(Slots.RowTwoSlotFour, offlineTime()) {
                if (it.bukkitEvent.isLeftClick) Settings.offlineTime += 10
                else if (it.bukkitEvent.isRightClick && Settings.offlineTime > 0) Settings.offlineTime -= 10
                it.player.sound(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE)
                it.bukkitEvent.currentItem = offlineTime()
            }

            button(Slots.RowTwoSlotSix, announceSwap()) {
                Settings.announceSwap = !Settings.announceSwap
                it.player.sound(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE)
                it.bukkitEvent.currentItem = announceSwap()
            }

            button(Slots.RowTwoSlotSeven, minTimeToSwap()) {
                if (it.bukkitEvent.isLeftClick) Settings.minTimeToSwap += 10
                else if (it.bukkitEvent.isRightClick && Settings.minTimeToSwap > 0) Settings.minTimeToSwap -= 10
                it.player.sound(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE)
                it.bukkitEvent.currentItem = minTimeToSwap()
            }

            button(Slots.RowTwoSlotEight, maxTimeToSwap()) {
                if (it.bukkitEvent.isLeftClick) Settings.maxTimeToSwap += 10
                else if (it.bukkitEvent.isRightClick && Settings.maxTimeToSwap > 0) Settings.maxTimeToSwap -= 10
                it.player.sound(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE)
                it.bukkitEvent.currentItem = maxTimeToSwap()
            }
        }
    }

    private fun autoStart() = itemStack(Material.CLOCK) {
        meta {
            name = literalText("${ChatColor.AQUA}Automatic Countdown")
            setLore {
                +"${ChatColor.GRAY}If enabled the countdown will start"
                +"${ChatColor.GRAY}as soon as enough players are online"
                +""
                +"${ChatColor.GRAY}Current value: ${ChatColor.YELLOW}${Settings.autoStart}"
                +""
                +"${ChatColor.GRAY}Normal Click: ${ChatColor.GOLD}${!Settings.autoStart}"
            }
        }
    }

    private fun requiredPlayers() = itemStack(Material.WHITE_BED) {
        meta {
            name = literalText("${ChatColor.AQUA}Required Players")
            setLore {
                +"${ChatColor.GRAY}The amount of players required"
                +"${ChatColor.GRAY}for the game to start"
                +""
                +"${ChatColor.GRAY}Current value: ${ChatColor.YELLOW}${Settings.requiredPlayers}"
                +""
                +"${ChatColor.GRAY}Click: ${ChatColor.GOLD}+1"
                +"${ChatColor.GRAY}Right Click: ${ChatColor.GOLD}-1"
            }
        }
    }

    private fun offlineTime() = itemStack(Material.COMPASS) {
        meta {
            name = literalText("${ChatColor.AQUA}Offline Time")
            setLore {
                +"${ChatColor.GRAY}The time you are able"
                +"${ChatColor.GRAY}to reconnect in"
                +""
                +"${ChatColor.GRAY}Current time: ${ChatColor.YELLOW}${Settings.offlineTime} seconds"
                +""
                +"${ChatColor.GRAY}Left Click: ${ChatColor.GOLD}+10"
                +"${ChatColor.GRAY}Right Click: ${ChatColor.GOLD}-10"
            }
        }
    }

    private fun announceSwap() = itemStack(Material.KNOWLEDGE_BOOK) {
        meta {
            name = literalText("${ChatColor.AQUA}Announce Swap")
            setLore {
                +"${ChatColor.GRAY}If enabled you will be notified"
                +"${ChatColor.GRAY}about your next swap"
                +""
                +"${ChatColor.GRAY}Current value: ${ChatColor.YELLOW}${Settings.announceSwap}"
                +""
                +"${ChatColor.GRAY}Normal Click: ${ChatColor.GOLD}${!Settings.announceSwap}"
            }
        }
    }

    private fun minTimeToSwap() = itemStack(Material.GUNPOWDER) {
        meta {
            name = literalText("${ChatColor.AQUA}Minimum time to swap")
            setLore {
                +"${ChatColor.GRAY}The time that must pass"
                +"${ChatColor.GRAY}until the next swap"
                +""
                +"${ChatColor.GRAY}Current time: ${ChatColor.YELLOW}${Settings.minTimeToSwap}"
                +""
                +"${ChatColor.GRAY}Left Click: ${ChatColor.GOLD}+10"
                +"${ChatColor.GRAY}Right Click: ${ChatColor.GOLD}-10"
            }
        }
    }

    private fun maxTimeToSwap() = itemStack(Material.REDSTONE) {
        meta {
            name = literalText("${ChatColor.AQUA}Maximum time to swap")
            setLore {
                +"${ChatColor.GRAY}The time that could potentially pass"
                +"${ChatColor.GRAY}until the next swap"
                +""
                +"${ChatColor.GRAY}Current time: ${ChatColor.YELLOW}${Settings.maxTimeToSwap}"
                +""
                +"${ChatColor.GRAY}Left Click: ${ChatColor.GOLD}+10"
                +"${ChatColor.GRAY}Right Click: ${ChatColor.GOLD}-10"
            }
        }
    }
}