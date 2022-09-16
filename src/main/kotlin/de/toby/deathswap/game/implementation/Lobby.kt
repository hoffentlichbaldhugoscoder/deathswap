package de.toby.deathswap.game.implementation

import de.toby.deathswap.config.implementation.Settings
import de.toby.deathswap.game.Game
import de.toby.deathswap.game.Phase
import de.toby.deathswap.ui.SettingsUI
import de.toby.deathswap.user.UserState
import de.toby.deathswap.user.user
import de.toby.deathswap.util.PlayerHider.show
import de.toby.deathswap.util.reset
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.actionBar
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.extensions.pluginManager
import org.bukkit.*
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class Lobby : Phase() {

    init {
        val world = Bukkit.getWorld("lobby_map") ?: WorldCreator("lobby_map").generateStructures(false).type(WorldType.FLAT).createWorld()!!
        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)

        event<PlayerJoinEvent> {
            val player = it.player

            player.reset()
            player.show()
            player.user().state = UserState.PLAYING
            player.inventory.setItem(8, SettingsUI.item)
            player.teleport(Location(world, 0.0, world.getHighestBlockAt(0, 0).y.toDouble() + 1, 0.0))

            idle = !(playerSize() >= Settings.requiredPlayers && Settings.autoStart)
        }

        event<PlayerQuitEvent> {
            it.player.user().state = null

            idle = !(playerSize() >= Settings.requiredPlayers && Settings.autoStart)
        }

        event<PlayerDropItemEvent> {
            it.isCancelled = true
        }

        event<PlayerInteractEvent> {
            it.isCancelled = true
        }

        event<EntityPickupItemEvent> {
            it.isCancelled = true
        }

        event<EntitySpawnEvent> {
            it.isCancelled = true
        }

        event<EntityDamageEvent> {
            it.isCancelled = true
        }

        event<FoodLevelChangeEvent> {
            it.isCancelled = true
        }

        event<EntityTargetEvent> {
            it.isCancelled = true
        }

        event<InventoryClickEvent> {
            it.isCancelled = true
        }

        onlinePlayers.forEach {
            pluginManager.callEvent(PlayerJoinEvent(it, null))
        }

        idle = !(playerSize() >= Settings.requiredPlayers && Settings.autoStart)
    }

    override fun run() {
        if (!idle) {
            when (countdown()) {
                2, 3, 4, 5, 10, 30 -> broadcast("${ChatColor.YELLOW}The game starts in ${countdown()} seconds")
                1 -> broadcast("${ChatColor.YELLOW}The game starts in one second")
                0 -> {
                    broadcast("${ChatColor.YELLOW}The game has started")

                    Game.current = Ingame()
                }
            }
        } else {
            val text = if (playerSize() < Settings.requiredPlayers) "Not enough players are online"
            else if (!Settings.autoStart) "The start is only done via /start" else null

            if (text == null) return
            onlinePlayers.forEach { it.actionBar("${ChatColor.RED}$text") }
        }
    }

    fun playerSize() = onlinePlayers.filter { it.user().state == UserState.PLAYING }.size

    private fun countdown() = 30 - time
}