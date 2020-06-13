package ru.buseso.dreamtime.bungeefriends.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ru.buseso.dreamtime.bungeefriends.BungeeFriends;
import ru.buseso.dreamtime.bungeefriends.party.Party;
import ru.buseso.dreamtime.bungeefriends.party.PartyManager;
import ru.buseso.dreamtime.bungeefriends.utils.FileUtils;
import ru.buseso.dreamtime.bungeefriends.utils.PartyMessageUtils;
import ru.buseso.dreamtime.bungeefriends.utils.Utils;

import java.util.concurrent.TimeUnit;

public class PartyListener implements Listener {
    @EventHandler
    public void onServerSwitch(ServerSwitchEvent e) {
        ProxiedPlayer p = e.getPlayer();

        if (PartyManager.isInParty(p)) {
            Party party = PartyManager.getPlayerParty(p);

            if (party.getLeader() == p) {
                for (ProxiedPlayer member : party.getMembers()) {
                    if (member != p) {
                        member.connect(p.getServer().getInfo());
                    }

                    member.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + FileUtils.getConfig().getString("Messages.Party.ConnectToServer").replace("%serverName%", p.getServer().getInfo().getName())));
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        final ProxiedPlayer p = e.getPlayer();

        ProxyServer.getInstance().getScheduler().schedule(BungeeFriends.getInstance(), () -> {
            if ((p == null || !p.isConnected()) &&
                    PartyManager.isInParty(p)) {
                PartyManager.getPlayerParty(p).leaveParty(p);
            }
        }, 1L, TimeUnit.SECONDS);
    }
}
