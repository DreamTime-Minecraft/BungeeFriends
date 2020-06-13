package ru.buseso.dreamtime.bungeefriends.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ru.buseso.dreamtime.bungeefriends.BungeeFriends;
import ru.buseso.dreamtime.bungeefriends.sql.FriendManager;
import ru.buseso.dreamtime.bungeefriends.sql.SettingsManager;
import ru.buseso.dreamtime.bungeefriends.utils.FileUtils;
import ru.buseso.dreamtime.bungeefriends.utils.FriendMessageUtils;
import ru.buseso.dreamtime.bungeefriends.utils.Utils;

import java.util.concurrent.TimeUnit;

public class FriendListener implements Listener {
    @EventHandler
    public void onJoin(ServerConnectEvent e) {
        ProxiedPlayer p = e.getPlayer();

        if (!e.isCancelled() && e.getReason() == ServerConnectEvent.Reason.JOIN_PROXY &&
                !FriendManager.isOnline(p.getName())) {
            FriendManager.registerIfNeeded(p.getName());

            FriendManager.setOnline(p.getName(), true);

            for (String uuid : FriendManager.getFriends(p.getName())) {
                ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(uuid);

                if (friend != null &&
                        SettingsManager.isGettingNotified(uuid)) {
                    friend.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + FileUtils.getConfig().getString("Messages.Friend.FriendOnline").replace("%player%", p.getDisplayName())));
                }
            }
        }
    }


    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        final ProxiedPlayer p = e.getPlayer();
        final String displayName = p.getDisplayName();

        ProxyServer.getInstance().getScheduler().schedule(BungeeFriends.getInstance(), () -> {
            if (p == null || !p.isConnected()) {
                FriendManager.setOnline(p.getName(), false);

                for (String uuid : FriendManager.getFriends(p.getName())) {
                    ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(uuid);

                    if (friend != null &&
                            SettingsManager.isGettingNotified(uuid)) {
                        friend.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + FileUtils.getConfig().getString("Messages.Friend.FriendOffline").replace("%player%", displayName)));
                    }
                }
            }
        }, 2L, TimeUnit.SECONDS);
    }
}
