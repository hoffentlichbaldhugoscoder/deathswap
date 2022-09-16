package de.toby.deathswap.listener

import de.toby.deathswap.user.UserState
import de.toby.deathswap.user.user
import de.toby.deathswap.util.PlayerHider
import de.toby.deathswap.util.PlayerHider.hide
import net.axay.kspigot.event.listen
import org.bukkit.GameMode
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

object Connection {

    fun enable() {
        listen<PlayerJoinEvent>(EventPriority.LOWEST) {
            PlayerHider.handleLogin()

            val player = it.player
            if (player.user().state == UserState.SPECTATING) {
                player.hide()
                player.gameMode = GameMode.SPECTATOR
            }
        }
    }
}
