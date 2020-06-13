package ru.buseso.dreamtime.bungeefriends;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import ru.buseso.dreamtime.bungeefriends.commands.FriendCommand;
import ru.buseso.dreamtime.bungeefriends.commands.PartyChatCommand;
import ru.buseso.dreamtime.bungeefriends.commands.PartyCommand;
import ru.buseso.dreamtime.bungeefriends.commands.PartyManagerCommand;
import ru.buseso.dreamtime.bungeefriends.listeners.ChannelListener;
import ru.buseso.dreamtime.bungeefriends.listeners.FriendListener;
import ru.buseso.dreamtime.bungeefriends.listeners.PartyListener;
import ru.buseso.dreamtime.bungeefriends.sql.MySQL;
import ru.buseso.dreamtime.bungeefriends.utils.FileUtils;
import ru.buseso.dreamtime.bungeefriends.utils.Utils;

public final class BungeeFriends extends Plugin {

    private static BungeeFriends instance;
    public static MySQL mysql;

    public static BungeeFriends getInstance() { return instance; }

    @Override
    public void onEnable() {
        instance = this;
        FileUtils.setupFiles();
        Configuration cfg = FileUtils.getConfig();
        FileUtils.saveFile(cfg);
        mysql = new MySQL(cfg.getString("MySQL.host"), cfg.getInt("MySQL.port"), cfg.getString("MySQL.database"), cfg.getString("MySQL.user"), cfg.getString("MySQL.password"));
        PluginManager pm = ProxyServer.getInstance().getPluginManager();

        if (FileUtils.getConfig().getBoolean("Settings.UseFriendFeatures")) {
            pm.registerCommand(this, new FriendCommand("friend"));
            pm.registerCommand(this, new FriendCommand("f"));

            pm.registerListener(this, new FriendListener());
        }

        if (FileUtils.getConfig().getBoolean("Settings.UsePartyFeatures")) {
            pm.registerCommand(this, new PartyCommand("party"));
            pm.registerCommand(this, new PartyCommand("p"));
            pm.registerCommand(this, new PartyChatCommand("pc"));
            pm.registerCommand(this, new PartyManagerCommand("partymanager"));
            pm.registerCommand(this, new PartyManagerCommand("pman"));

            pm.registerListener(this, new PartyListener());
        }

        pm.registerListener(this, new ChannelListener());
        ProxyServer.getInstance().registerChannel("BungeeCommands");
        Utils.sendConsole("§8[§aBungeeFriends§8] §7Плагин§8: §aВКЛЮЧЁН");
    }

    @Override
    public void onDisable() {
        mysql.disconnect();
        Utils.sendConsole("§8[§aBungeeFriends§8] §7Плагин§8: §cВЫКЛЮЧЕН");
    }
}
