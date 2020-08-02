package ru.buseso.dreamtime.bungeefriends.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import ru.buseso.dreamtime.bungeefriends.sql.FriendManager;
import ru.buseso.dreamtime.bungeefriends.sql.SettingsManager;
import ru.buseso.dreamtime.bungeefriends.utils.FileUtils;
import ru.buseso.dreamtime.bungeefriends.utils.FriendMessageUtils;
import ru.buseso.dreamtime.bungeefriends.utils.Utils;

public class FriendCommand extends Command {
    public FriendCommand(String name) { super(name); }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Configuration cfg = FileUtils.getConfig();

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer)sender;
            boolean showHelp = false;

            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    showHelp = true;
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (FriendManager.getFriends(p.getName()).size() >= 1) {
                        for (String uuid : FriendManager.getFriends(p.getName())) {
                            String status = (ProxyServer.getInstance().getPlayer(uuid) != null) ? "§aОнлайн" : "§cОффлайн";
                            p.sendMessage(Utils.getAsBaseComponent(FriendMessageUtils.prefix + "§e" + uuid + " §7(§r" + status + "§7)"));
                        }
                    } else {
                        p.sendMessage(Utils.getAsBaseComponent(FriendMessageUtils.prefix + cfg.getString("Messages.Friend.NoFriends")));
                    }
                } else if (args[0].equalsIgnoreCase("requests")) {
                    if (FriendManager.getRequests(p.getName()).size() >= 1) {
                        p.sendMessage(Utils.getAsBaseComponent(FriendMessageUtils.prefix + "§2Все Ваши запросы в друзья:"));
                        for (String uuid : FriendManager.getRequests(p.getName())) {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + "§e" + uuid));
                        }
                    } else {
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NoRequestOpen")));
                    }
                } else if (args[0].equalsIgnoreCase("acceptall")) {
                    if (FriendManager.getRequests(p.getName()).size() >= 1) {
                        for (String requested : FriendManager.getRequests(p.getName())) {
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(p, "friend accept " + requested);
                        }
                    } else {
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NoRequestOpen")));
                    }
                } else if (args[0].equalsIgnoreCase("denyall")) {
                    if (FriendManager.getRequests(p.getName()).size() >= 1) {
                        for (String requested : FriendManager.getRequests(p.getName())) {
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(p, "friend deny " + requested);
                        }
                    } else {
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NoRequestOpen")));
                    }
                } else if (args[0].equalsIgnoreCase("clear")) {
                    if (FriendManager.getFriends(p.getName()).size() >= 1) {
                        for (String uuid : FriendManager.getFriends(p.getName())) {
                            ProxyServer.getInstance().getPluginManager().dispatchCommand(p, "friend remove " + uuid);
                        }
                    } else {
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NoFriends")));
                    }
                } else if (args[0].equalsIgnoreCase("togglerequests")) {
                    if (SettingsManager.isGettingRequests(p.getName())) {
                        SettingsManager.setGetRequests(p.getName(), false);
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.DeactivatedRequests")));
                    } else {
                        SettingsManager.setGetRequests(p.getName(), true);
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.ActivatedRequests")));
                    }
                } else if (args[0].equalsIgnoreCase("togglenotify")) {
                    if (SettingsManager.isGettingNotified(p.getName())) {
                        SettingsManager.setGetNotified(p.getName(), false);
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.DeactivatedNotify")));
                    } else {
                        SettingsManager.setGetNotified(p.getName(), true);
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.ActivatedNotify")));
                    }
                } else if (args[0].equalsIgnoreCase("togglemessages")) {
                    if (SettingsManager.isUsingFriendChat(p.getName())) {
                        SettingsManager.setUseFriendChat(p.getName(), false);
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.DeactivatedFriendChat")));
                    } else {
                        SettingsManager.setUseFriendChat(p.getName(), true);
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.ActivatedFriendChat")));
                    }
                } else if (args[0].equalsIgnoreCase("togglejump")) {
                    if (SettingsManager.isAllowingServerJumping(p.getName())) {
                        SettingsManager.setServerJuming(p.getName(), false);
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.DeactivatedServerJumping")));
                    } else {
                        SettingsManager.setServerJuming(p.getName(), true);
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.ActivatedServerJumping")));
                    }

                } else if (args.length >= 2) {
                    ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[1]);

                    if (args[0].equalsIgnoreCase("add")) {
                        if (t != null) {
                            if (t != p) {
                                if (!FriendManager.isFriend(p.getName(), t.getName())) {
                                    if (!FriendManager.isRequestOpen(p.getName(), t.getName())) {
                                        if (SettingsManager.isGettingRequests(t.getName())) {
                                            if ((p.hasPermission("friend.100") && FriendManager.getFriends(p.getName()).size() < 100) || FriendManager.getFriends(p.getName()).size() < 50 || p.hasPermission("friend.unlimited")) {
                                                FriendManager.addRequest(p.getName(), t.getName());

                                                p.sendMessage(Utils.getAsBaseComponent(FriendMessageUtils.prefix + cfg.getString("Messages.Friend.MessageSent").replace("%player%", args[1])));

                                                TextComponent message = new TextComponent(FriendMessageUtils.prefix + "     ");

                                                TextComponent accept = new TextComponent("§8[§aПРИНЯТЬ");
                                                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + p.getName()));
                                                message.addExtra(accept);

                                                message.addExtra(new TextComponent(" §7| "));

                                                TextComponent deny = new TextComponent("§cОТКЛОНИТЬ§8]");
                                                deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + p.getName()));
                                                message.addExtra(deny);

                                                t.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.PlayerInvite").replace("%player%", p.getDisplayName())));
                                                t.sendMessage(message);
                                            } else {
                                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.TooManyFriends")));
                                            }
                                        } else {
                                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NotAllowingRequests").replace("%player%", t.getDisplayName())));
                                        }
                                    } else {
                                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.AlreadySentARequest")));
                                    }
                                } else {
                                    p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.AlreadyFriend").replace("%player%", t.getDisplayName())));
                                }
                            } else {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.CantInteractSelf")));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.PlayerNotFound").replace("%player%", args[1])));
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        String uuid = args[1];

                        if (FriendManager.isFriend(p.getName(), uuid)) {
                            FriendManager.removeFriend(p.getName(), uuid);
                            FriendManager.removeFriend(uuid, p.getName());

                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.Removed").replace("%player%", args[1])));

                            if (t != null) {
                                t.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NotFriendAnymore").replace("%player%", p.getName())));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NotYourFriend").replace("%player%", args[1])));
                        }
                    } else if (args[0].equalsIgnoreCase("accept")) {
                        String uuid;

                        if (t != null) {
                            uuid = t.getName();
                        } else {
                            return;
                        }

                        if (FriendManager.isRequestOpen(uuid, p.getName())) {
                            if ((p.hasPermission("friend.100") && FriendManager.getFriends(p.getName()).size() < 100) || FriendManager.getFriends(p.getName()).size() < 50 || p.hasPermission("friend.unlimited")) {
                                FriendManager.removeRequest(uuid, p.getName());
                                FriendManager.addFriend(p.getName(), uuid);
                                FriendManager.addFriend(uuid, p.getName());

                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.Accepted")));
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NowYourFriend").replace("%player%", args[1])));

                                if (t != null)
                                    t.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NowYourFriend").replace("%player%", p.getDisplayName())));
                            } else {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.TooManyFriends")));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NoRequestSent").replace("%player%", args[1])));
                        }
                    } else if (args[0].equalsIgnoreCase("deny")) {
                        String uuid;

                        if (t != null) {
                            uuid = t.getName();
                        } else {
                            return;
                        }

                        if (FriendManager.isRequestOpen(uuid, p.getName())) {
                            FriendManager.removeRequest(uuid, p.getName());

                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.Denied")));

                            if (t != null)
                                t.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.RequestDenied").replace("%player%", p.getDisplayName())));
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NoRequestSent").replace("%player%", args[1])));
                        }
                    } else if (args[0].equalsIgnoreCase("jump")) {
                        String uuid = args[1];

                        if (t != null) {
                            if (FriendManager.isFriend(p.getName(), uuid)) {
                                if (SettingsManager.isAllowingServerJumping(uuid)) {
                                    p.connect(t.getServer().getInfo());

                                    p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.Jumped").replace("%player%", t.getDisplayName())));
                                } else {
                                    p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NotAllowingServerJumping").replace("%player%", t.getDisplayName())));
                                }
                            } else {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.NotYourFriend").replace("%player%", t.getDisplayName())));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString("Messages.Friend.PlayerNotFound").replace("%player%", args[1])));
                        }
                    } else {
                        showHelp = true;
                    }
                } else {
                    showHelp = true;
                }
            } else {

                showHelp = true;
            }

            if (showHelp)
                for (int i = 1; i <= 35; ) {
                    String path = "Messages.Friend.Help.Line" + i;

                    if (cfg.contains(path)) {
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(FriendMessageUtils.prefix) + cfg.getString(path)));
                        i++;
                    } else { break; }
                }
        } else {
            Utils.sendConsole(String.valueOf(FriendMessageUtils.prefix) + FriendMessageUtils.notPlayer);
        }
    }
}
