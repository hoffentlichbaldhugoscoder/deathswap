package de.toby.deathswap

import de.toby.deathswap.command.FreezeCommand
import de.toby.deathswap.command.SettingsCommand
import de.toby.deathswap.command.SpectatorCommand
import de.toby.deathswap.command.StartCommand
import de.toby.deathswap.game.Game
import de.toby.deathswap.game.implementation.Lobby
import de.toby.deathswap.listener.Connection
import de.toby.deathswap.ui.SettingsUI
import net.axay.kspigot.main.KSpigot
import org.bukkit.World

class DeathSwap : KSpigot() {

    lateinit var world: World

    companion object {
        lateinit var INSTANCE: DeathSwap; private set
    }

    override fun load() {
        INSTANCE = this
    }

    override fun startup() {
        world = server.getWorld("world") ?: return

        FreezeCommand.enable()
        SpectatorCommand.enable()
        StartCommand.enable()
        SettingsCommand.enable()

        Connection.enable()

        SettingsUI.enable()

        Game.current = Lobby()
    }
}

val Manager by lazy { DeathSwap.INSTANCE }