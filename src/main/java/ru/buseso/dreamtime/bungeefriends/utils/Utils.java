package ru.buseso.dreamtime.bungeefriends.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {
    public static void sendConsole(String msg) {
        ProxyServer.getInstance().getConsole().sendMessage(getAsBaseComponent(msg));
    }

    public static TextComponent getAsBaseComponent(String s) {
        return new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s)));
    }
}
