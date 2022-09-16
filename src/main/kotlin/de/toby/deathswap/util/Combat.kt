package de.toby.deathswap.util

import de.toby.deathswap.user.UserState
import de.toby.deathswap.user.user
import de.toby.deathswap.util.PlayerHider.hide
import org.bukkit.GameMode
import org.bukkit.entity.Player

fun Player.eliminate() {
    user().state = UserState.ELIMINATED
    hide()
    gameMode = GameMode.SPECTATOR
}
