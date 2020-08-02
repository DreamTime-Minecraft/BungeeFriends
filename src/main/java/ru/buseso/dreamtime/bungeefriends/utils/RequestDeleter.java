package ru.buseso.dreamtime.bungeefriends.utils;

import net.md_5.bungee.api.ProxyServer;
import ru.buseso.dreamtime.bungeefriends.BungeeFriends;
import ru.buseso.dreamtime.bungeefriends.party.PartyManager;

public class RequestDeleter {

    public static void start() {
        ProxyServer.getInstance().getScheduler().runAsync(BungeeFriends.getInstance(), () -> {
            long now = System.currentTimeMillis();
            PartyManager.req.removeIf(requests -> requests.time*1000*60*3 < now); //По сути через 3 минуты после отправки должно убирать
        });
    }

}
