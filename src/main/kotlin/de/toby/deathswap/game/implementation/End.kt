package de.toby.deathswap.game.implementation

import de.toby.deathswap.game.Phase
import de.toby.deathswap.user.UserState
import de.toby.deathswap.user.user
import net.axay.kspigot.chat.literalText
import net.axay.kspigot.extensions.bukkit.title
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.extensions.server
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.time.Duration

class End(private val winner: Player) : Phase() {

    init {
        event<PlayerJoinEvent> {
            val player = it.player
            if (player.user().state == null) player.user().state = UserState.SPECTATING
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
    }

    override fun run() {
        if (countdown() == 0) server.spigot().restart()

        onlinePlayers.forEach {
            it.title(
                literalText("${ChatColor.WHITE}Winner:"),
                literalText("${ChatColor.GREEN}${winner.name}"),
                Duration.ZERO,
                Duration.ofMillis(1050),
                Duration.ZERO
            )
        }
    }

    private fun countdown() = 30 - time
}