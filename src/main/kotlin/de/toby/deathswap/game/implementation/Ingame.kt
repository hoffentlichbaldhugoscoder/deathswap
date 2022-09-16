package de.toby.deathswap.game.implementation

import de.toby.deathswap.Manager
import de.toby.deathswap.config.implementation.Settings
import de.toby.deathswap.game.Game
import de.toby.deathswap.game.Phase
import de.toby.deathswap.user.UserState
import de.toby.deathswap.user.user
import de.toby.deathswap.util.PlayerHider.hide
import de.toby.deathswap.util.eliminate
import de.toby.deathswap.util.reset
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.task
import net.axay.kspigot.sound.sound
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class Ingame : Phase() {

    private var countdown = (Settings.minTimeToSwap..Settings.maxTimeToSwap).random()

    init {
        Manager.world.run {
            time = 1000
            setStorm(false)
            isThundering = false
        }

        onlinePlayers.forEach { player ->
            player.reset()
            player.teleport(Manager.world.spawnLocation)
        }

        onlinePlayers.filter { it.user().state == UserState.SPECTATING }.forEach {
            it.hide()
            it.gameMode = GameMode.SPECTATOR
        }

        event<PlayerJoinEvent> {
            it.player.user().task?.cancel()
            if (it.player.user().state == null) it.player.user().state = UserState.SPECTATING
        }

        event<PlayerQuitEvent> { event ->
            val player = event.player
            if (player.user().state != UserState.PLAYING) return@event

            player.user().task = task(period = 20, howOften = Settings.offlineTime) { runnable ->
                if (runnable.counterDownToZero != 0L) return@task
                if (playerAlive().size == 1) Game.current =
                    End(onlinePlayers.first { it.user().state == UserState.PLAYING })

                player.eliminate()
                runnable.cancel()
                broadcast("${ChatColor.YELLOW}${player.displayName} was offline for to long and got eliminated trying to rejoin")
            }
        }

        event<EntityDamageByEntityEvent> {
            if (it.damager !is Player) return@event
            if (it.entity !is Player) return@event

            it.isCancelled = true
        }

        event<EntityDeathEvent> { event ->
            if (event.entity is Player) {
                (event.entity as Player).eliminate()
                if (playerAlive().size == 1) Game.current = End(onlinePlayers.first { it.user().state == UserState.PLAYING })
            } else if(event.entity is EnderDragon) {
                Game.current = End(event.entity.killer ?: return@event)
            }
        }
    }

    override fun run() {
        countdown--
        if (Settings.announceSwap) {
            when (countdown) {
                in 2..10, 20, 30, 60 -> broadcast("${ChatColor.LIGHT_PURPLE}The next swap happens in $countdown seconds")
                1 -> broadcast("${ChatColor.LIGHT_PURPLE}The next swap happens in one second")
            }
        }

        if (countdown == 0) {
            swap()
            countdown = (Settings.minTimeToSwap..Settings.maxTimeToSwap).random()
        }
    }

    private fun swap() {
        val players = LinkedList<Player>().also { it.addAll(playerAlive()) }
        if (players.size <= 1) return

        val participants = players.toArray().copyOfRange(0, players.size / 2).toList()
        val targets = players.toArray().copyOfRange(players.size / 2, players.size).toList()

        for (i in 0..(players.size - 1) / 2) {
            val participant = participants.random() as Player
            val target = targets.random() as Player

            val location = target.location
            target.run {
                teleport(participant.location)
                sendMessage("${ChatColor.LIGHT_PURPLE}You got swapped with ${participant.displayName}")
                sound(Sound.ENTITY_ENDERMAN_TELEPORT)
            }
            participant.run {
                teleport(location)
                sendMessage("${ChatColor.LIGHT_PURPLE}You got swapped with ${target.displayName}")
                sound(Sound.ENTITY_ENDERMAN_TELEPORT)
            }
        }
    }

    private fun playerAlive() = onlinePlayers.filter { it.user().state == UserState.PLAYING }.toList()
}