package ru.buseso.dreamtime.bungeefriends.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static File folder = new File("plugins/BungeeFriends/");
    public static File file = new File("plugins/BungeeFriends/setup.yml");

    public static void saveFile(Configuration cfg) {
        try {
            ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).save(cfg, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FriendMessageUtils.prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("Messages.Friend.Prefix"));
        FriendMessageUtils.notPlayer = ChatColor.translateAlternateColorCodes('&', cfg.getString("Messages.Friend.NotPlayer"));

        PartyMessageUtils.prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("Messages.Party.Prefix"));
        PartyMessageUtils.notPlayer = ChatColor.translateAlternateColorCodes('&', cfg.getString("Messages.Party.NotPlayer"));
    }

    public static void setupFiles() {
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Configuration cfg = getConfig();

        addDefault(cfg, "MySQL.host", "localhost");
        addDefault(cfg, "MySQL.port", Integer.valueOf(3306));
        addDefault(cfg, "MySQL.database", "private");
        addDefault(cfg, "MySQL.user", "root");
        addDefault(cfg, "MySQL.password", "password");

        addDefault(cfg, "Settings.UseFriendFeatures", Boolean.valueOf(true));
        addDefault(cfg, "Settings.UsePartyFeatures", Boolean.valueOf(true));
        addDefault(cfg, "Messages.Friend.Prefix", "&7[&4Freunde&7] &7");
        addDefault(cfg, "Messages.Friend.NotPlayer", "&7Nur Spieler k?nnen diesen Befehl ausf?hren");
        addDefault(cfg, "Messages.Friend.MSG.Self", "&3Me &7� &e%target%&8: &r%message%");
        addDefault(cfg, "Messages.Friend.MSG.Other", "&e%player% &7� &3Me&8: &r%message%");
        addDefault(cfg, "Messages.Friend.FriendOnline", "&e%player% &7ist nun &aonline");
        addDefault(cfg, "Messages.Friend.FriendOffline", "&e%player% &7ist nun &coffline");
        addDefault(cfg, "Messages.Friend.Accepted", "&7Du hast die Anfrage &aangenommen");
        addDefault(cfg, "Messages.Friend.Denied", "&7Du hast die Anfrage &cabgelehnt");
        addDefault(cfg, "Messages.Friend.MessageSent", "&7Du hast dem Spieler &e%player% &7eine Freundschafts-Anfrage gesendet");
        addDefault(cfg, "Messages.Friend.RequestDenied", "&7Der Spieler &e%player% &7hat die Freundschafts-Anfrage &cabgelehnt");
        addDefault(cfg, "Messages.Friend.NowYourFriend", "&e%player% &7ist nun dein Freund");
        addDefault(cfg, "Messages.Friend.NotFriendAnymore", "&e%player% &7hat die Freundschaft &caufgel?st");
        addDefault(cfg, "Messages.Friend.Removed", "&7Du hast den Spieler &e%player% &7aus deiner Freundes-Liste &centfernt");
        addDefault(cfg, "Messages.Friend.PlayerNotFound", "&7Der Spieler &e%player% &7wurde &cnicht gefunden");
        addDefault(cfg, "Messages.Friend.AlreadySentARequest", "&7Du hast einem Spieler &cbereits &7eine Anfrage gesendet");
        addDefault(cfg, "Messages.Friend.NoRequestSent", "&7Der Spieler &e%player% &7hat dir &ckeine &7Freundschafts-Anfrage gesendet");
        addDefault(cfg, "Messages.Friend.NotYourFriend", "&7Der Spieler &e%player% &7ist &cnicht &7dein Freund");
        addDefault(cfg, "Messages.Friend.PlayerInvite", "&7Der Spieler &e%player% &7hat dir eine Freundschafts-Anfrage gesendet");
        addDefault(cfg, "Messages.Friend.AlreadyFriend", "&7Der Spieler &e%player% &7ist &cbereits &7dein Freund");
        addDefault(cfg, "Messages.Friend.CantInteractSelf", "&7Du kannst &cnicht &7mit dir selbst inteagieren");
        addDefault(cfg, "Messages.Friend.NoFriends", "&7Du hast &ckeine &7Freunde...");
        addDefault(cfg, "Messages.Friend.NoRequestOpen", "&7Zurzeit hast du &ckeine &7offenen Anfragen");
        addDefault(cfg, "Messages.Friend.Jumped", "&7Du bist zu dem Spieler &e%player% &7gesprungen");
        addDefault(cfg, "Messages.Friend.NoMessageReceived", "&7Du hast &ckeine &7Nachricht erhalten");
        addDefault(cfg, "Messages.Friend.NotUsingFriendChat", "&7Der Spieler &e%player% &7hat den Freundes-Chat &cdeaktiviert");
        addDefault(cfg, "Messages.Friend.NotAllowingRequests", "&7Der Spieler &e%player% &7hat die Freundschaftsanfragen &cdeaktiviert");
        addDefault(cfg, "Messages.Friend.NotAllowingServerJumping", "&7Der Spieler &e%player% &7hat das Nachspringen &cdeaktiviert");
        addDefault(cfg, "Messages.Friend.ActivatedNotify", "&7Du &aerh?lst &7nun wieder Online-/Offline-Benachrichtigungen");
        addDefault(cfg, "Messages.Friend.DeactivatedNotify", "&7Du erh?lst nun &ckeine &7Online-/Offline-Benachrichtigungen mehr");
        addDefault(cfg, "Messages.Friend.ActivatedFriendChat", "&7Du &akannst &7nun wieder MSG-Nachrichten erhalten");
        addDefault(cfg, "Messages.Friend.DeactivatedFriendChat", "&7Du kannst nun &ckeine &7MSG-Nachrichten mehr erhalten");
        addDefault(cfg, "Messages.Friend.ActivatedRequests", "&7Du &akannst &7nun wieder Freundschaftsanfragen erhalten");
        addDefault(cfg, "Messages.Friend.DeactivatedRequests", "&7Du kannst nun &ckeine &7Freundschaftsanfragen mehr erhalten");
        addDefault(cfg, "Messages.Friend.ActivatedServerJumping", "&7Dir &akann &7nun wieder nachgesprungen werden");
        addDefault(cfg, "Messages.Friend.DeactivatedServerJumping", "&7Dir kann nun &cnicht &7mehr nachgesprungen werden");
        addDefault(cfg, "Messages.Friend.TooManyFriends", "&7Du hast &czu viele &7Freunde");
        addDefault(cfg, "Messages.Friend.Help.MSG", "&e/msg [player] [message]");
        addDefault(cfg, "Messages.Friend.Help.Reply", "&e/r [message]");
        addDefault(cfg, "Messages.Friend.Help.Line1", "&8���������� &7Friend help &8����������");
        addDefault(cfg, "Messages.Friend.Help.Line2", "&7/friend help &8- &eShow this help");
        addDefault(cfg, "Messages.Friend.Help.Line3", "&7/friend list &8- &eShow a list of all your friends");
        addDefault(cfg, "Messages.Friend.Help.Line4", "&7/friend clear &8- &eRemove all friends");
        addDefault(cfg, "Messages.Friend.Help.Line5", "&7/friend acceptall &8- &eAccept all friend request");
        addDefault(cfg, "Messages.Friend.Help.Line6", "&7/friend denyall &8- &eDeny all friend request");
        addDefault(cfg, "Messages.Friend.Help.Line7", "&7/friend requests &8- &eShow a list of all requests");
        addDefault(cfg, "Messages.Friend.Help.Line8", "&7/friend togglenotify &8- &eToggle receiving of online-/offline messages");
        addDefault(cfg, "Messages.Friend.Help.Line9", "&7/friend togglerequests &8- &eToggle getting requests");
        addDefault(cfg, "Messages.Friend.Help.Line10", "&7/friend togglemessages &8- &eToggle getting private messages");
        addDefault(cfg, "Messages.Friend.Help.Line11", "&7/friend togglejump &8- &eToggle beeing jumped after");
        addDefault(cfg, "Messages.Friend.Help.Line12", "&7/friend add [player] &8- &eAdd a friend");
        addDefault(cfg, "Messages.Friend.Help.Line13", "&7/friend remove [player] &8- &eRemove a friend");
        addDefault(cfg, "Messages.Friend.Help.Line14", "&7/friend accept [player] &8- &eAccept a friend request");
        addDefault(cfg, "Messages.Friend.Help.Line15", "&7/friend deny [player] &8- &eDeny a friend request");
        addDefault(cfg, "Messages.Friend.Help.Line16", "&7/friend jump [player] &8- &eJump to a friend");
        addDefault(cfg, "Messages.Friend.Help.Line17", "&7/msg [player] [message] &8- &eWrite a message to a friend");
        addDefault(cfg, "Messages.Friend.Help.Line18", "&7/r [message] &8- &eReply to a message");

        addDefault(cfg, "Messages.Party.Prefix", "&7[&5Party&7] &7");
        addDefault(cfg, "Messages.Party.NotPlayer", "&7Nur Spieler k?nnen diesen Befehl ausf?hren");
        addDefault(cfg, "Messages.Party.PartyChat", "&e%player%&8: &r%message%");
        addDefault(cfg, "Messages.Party.ConnectToServer", "&7Die Party betritt den Server &e%serverName%");
        addDefault(cfg, "Messages.Party.NotInParty", "&7Du bist in &ckeiner &7Party");
        addDefault(cfg, "Messages.Party.NotLeader", "&7Du bist &cnicht &7der Party-Leader");
        addDefault(cfg, "Messages.Party.Accepted", "&7Du hast die Einladung &aangenommen");
        addDefault(cfg, "Messages.Party.Denied", "&7Du hast die Einladung &cabgelehnt");
        addDefault(cfg, "Messages.Party.Invited", "&7Du hast den Spieler &e%player% &7in die Party eingeladen");
        addDefault(cfg, "Messages.Party.Joined", "&e%player% &7hat die Party &abetreten");
        addDefault(cfg, "Messages.Party.Left", "&e%player% &7hat die Party &cverlassen");
        addDefault(cfg, "Messages.Party.Kick", "&7Du hast den Spieler &e%player% &7aus der Party geworfen");
        addDefault(cfg, "Messages.Party.Kicked", "&7Du wurdest &caus der Party &7geworfen");
        addDefault(cfg, "Messages.Party.PlayerNotFound", "&7Der Spieler &e%player% &7wurde &cnicht gefunden");
        addDefault(cfg, "Messages.Party.PlayerInvite", "&7Der Spieler &e%player% &7hat dir eine Partyeinladung gesendet");
        addDefault(cfg, "Messages.Party.AlreadySentARequest", "&7Du hast einem Spieler &cbereits &7eine Einladung gesendet");
        addDefault(cfg, "Messages.Party.NoRequestSent", "&7Der Spieler &e%player% &7hat dir &ckeine &7Einladung gesendet");
        addDefault(cfg, "Messages.Party.Deleted", "&7Die Party wurde &caufgel?st");
        addDefault(cfg, "Messages.Party.AlreadyInParty", "&7Du bist &cbereits &7in einer Party");
        addDefault(cfg, "Messages.Party.PlayerNotInParty", "&7Der Spieler &e%player% &7ist &cnicht &7in deiner Party");
        addDefault(cfg, "Messages.Party.NotInParty", "&7Du bist in &ckeiner &7Party");
        addDefault(cfg, "Messages.Party.RequestDenied", "&7Der Spieler &e%player% &7hat die Einladung &cabgelehnt");
        addDefault(cfg, "Messages.Party.Promoted", "&7Der Spieler &e%player% &7ist nun &a%newRank%");
        addDefault(cfg, "Messages.Party.Demoted", "&7Der Spieler &e%player% &7ist nun &a%newRank%");
        addDefault(cfg, "Messages.Party.AlreadyInParty", "&7Der Spieler &e%player% &7ist &cbereits &7in einer Party");
        addDefault(cfg, "Messages.Party.CantInteractSelf", "&7Du kannst &cnicht &7mit dir selbst inteagieren");
        addDefault(cfg, "Messages.Party.NotAllowingInvites", "&7Der Spieler &e%player% &7hat die Partyeinladungen &cdeaktiviert");
        addDefault(cfg, "Messages.Party.ActivatedInvites", "&7Du &akannst &7nun wieder Partyeinladungen erhalten");
        addDefault(cfg, "Messages.Party.DeactivatedInvites", "&7Du kannst nun &ckeine &7Partyeinladungen mehr erhalten");
        addDefault(cfg, "Messages.Party.Help.PartyChat", "&e/msg [player] [message]");
        addDefault(cfg, "Messages.Party.Help.Line1", "&8���������� &7Party help &8����������");
        addDefault(cfg, "Messages.Party.Help.Line2", "&7/party help &8- &eShow this help");
        addDefault(cfg, "Messages.Party.Help.Line3", "&7/pc [message] &8- &eWrite a message in the party chat");
        addDefault(cfg, "Messages.Party.Help.Line4", "&7/party leave &8- &eLeave the party");
        addDefault(cfg, "Messages.Party.Help.Line5", "&7/party list &8- &eShow a list of all party members");
        addDefault(cfg, "Messages.Party.Help.Line6", "&7/party toggleinvites &8- &eToggle getting party invites");
        addDefault(cfg, "Messages.Party.Help.Line7", "&7/party invite [player] &8- &eInvite a player into party");
        addDefault(cfg, "Messages.Party.Help.Line8", "&7/party kick [player] &8- &eKick a player");
        addDefault(cfg, "Messages.Party.Help.Line9", "&7/party promote [player] &8- &ePromote a party member");
        addDefault(cfg, "Messages.Party.Help.Line10", "&7/party demote [player] &8- &eDemote a party member");
        addDefault(cfg, "Messages.Party.Help.Line11", "&7/party accept [player] &8- &eAccept a party invitation");
        addDefault(cfg, "Messages.Party.Help.Line12", "&7/party deny [player] &8- &eDeny a party invitation");
        saveFile(cfg);
    }

    public static void addDefault(Configuration cfg, String path, Object value) {
        if (!cfg.contains(path))
            cfg.set(path, value);
    }

    public static Configuration getConfig() {
        try {
            return ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();


            return null;
        }
    }
}
