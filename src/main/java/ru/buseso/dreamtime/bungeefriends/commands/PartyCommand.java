package ru.buseso.dreamtime.bungeefriends.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import ru.buseso.dreamtime.bungeefriends.party.Party;
import ru.buseso.dreamtime.bungeefriends.party.PartyManager;
import ru.buseso.dreamtime.bungeefriends.party.PartyRank;
import ru.buseso.dreamtime.bungeefriends.party.Requests;
import ru.buseso.dreamtime.bungeefriends.sql.SettingsManager;
import ru.buseso.dreamtime.bungeefriends.utils.FileUtils;
import ru.buseso.dreamtime.bungeefriends.utils.PartyMessageUtils;
import ru.buseso.dreamtime.bungeefriends.utils.Utils;

public class PartyCommand extends Command {
    public PartyCommand(String name) { super(name); }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Configuration cfg = FileUtils.getConfig();

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer)sender;
            boolean showHelp = false;

            if (args.length >= 1) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        showHelp = true;
                    } else if (args[0].equalsIgnoreCase("list")) {
                        if (PartyManager.isInParty(p)) {
                            Party party = PartyManager.getPlayerParty(p);

                            p.sendMessage(Utils.getAsBaseComponent(""));

                            for (ProxiedPlayer member : party.getMembers()) {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + "§e" + member.getDisplayName() + " §7(§r" + party.members.get(member).name() + "§7)"));
                            }

                            p.sendMessage(Utils.getAsBaseComponent(""));
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NotInParty")));
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        if (PartyManager.isInParty(p)) {
                            PartyManager.getPlayerParty(p).leaveParty(p);
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NotInParty")));
                        }
                    } else if (args[0].equalsIgnoreCase("toggleinvites")) {
                        if (SettingsManager.isGettigPartyInvites(p.getName())) {
                            SettingsManager.setGetPartyInvites(p.getName(), false);
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.DeactivatedInvites")));
                        } else {
                            SettingsManager.setGetPartyInvites(p.getName(), true);
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.ActivatedInvites")));
                        }
                    } else {
                        showHelp = true;
                    }
                } else {
                    ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[1]);

                    if (args[0].equalsIgnoreCase("accept")) {
                        if (t != null) {
                            if(PartyManager.isReqOpen(t, p)) {
                                PartyManager.reqRemove(t);

                                PartyManager.getPlayerParty(t).addMember(p);

                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Accepted")));

                                for (ProxiedPlayer member : PartyManager.getPlayerParty(p).getMembers()) {
                                    member.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Joined").replace("%player%", p.getDisplayName())));
                                }
                            } else {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NoRequestSent").replace("%player%", t.getDisplayName())));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerNotFound").replace("%player%", args[1])));
                        }
                    } else if (args[0].equalsIgnoreCase("deny")) {
                        if (t != null) {
                            if (PartyManager.isReqOpen(t, p)) {
                                PartyManager.reqRemove(t);

                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Denied")));
                                t.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.RequestDenied").replace("%player%", p.getDisplayName())));
                            } else {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NoRequestSent").replace("%player%", t.getDisplayName())));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerNotFound").replace("%player%", args[1])));
                        }

                    } else if (args[0].equalsIgnoreCase("kick")) {
                        if (PartyManager.isInParty(p)) {
                            if (t != null) {
                                if (PartyManager.getPlayerParty(p).isMember(t)) {
                                    if ((PartyManager.getPlayerParty(p)).members.get(p) == PartyRank.MOD || (PartyManager.getPlayerParty(p)).members.get(p) == PartyRank.LEADER) {
                                        PartyManager.getPlayerParty(p).leaveParty(t);

                                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Kick").replace("%player%", t.getDisplayName())));
                                        t.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Kicked")));
                                    } else {
                                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NotLeader")));
                                    }
                                } else {
                                    p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerNotInParty").replace("%player%", t.getDisplayName())));
                                }
                            } else {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerNotFound").replace("%player%", args[1])));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NotInParty")));
                        }
                    } else if (args[0].equalsIgnoreCase("promote")) {
                        if (PartyManager.isInParty(p)) {
                            if (t != null) {
                                if (PartyManager.getPlayerParty(p).isMember(t)) {
                                    if ((PartyManager.getPlayerParty(p)).members.get(p) == PartyRank.LEADER) {
                                        PartyManager.getPlayerParty(p).promote(t);

                                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Promoted").replace("%player%", t.getDisplayName()).replace("%newRank%", ((PartyRank)(PartyManager.getPlayerParty(p)).members.get(t)).name())));
                                    } else {
                                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NotLeader")));
                                    }
                                } else {
                                    p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerNotInParty").replace("%player%", t.getDisplayName())));
                                }
                            } else {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerNotFound").replace("%player%", args[1])));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NotInParty")));
                        }
                    } else if (args[0].equalsIgnoreCase("demote")) {
                        if (PartyManager.isInParty(p)) {
                            if (t != null) {
                                if (PartyManager.getPlayerParty(p).isMember(t)) {
                                    if ((PartyManager.getPlayerParty(p)).members.get(p) == PartyRank.LEADER) {
                                        PartyManager.getPlayerParty(p).demote(t);

                                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Demoted").replace("%player%", t.getDisplayName()).replace("%newRank%", ((PartyRank)(PartyManager.getPlayerParty(p)).members.get(t)).name())));
                                    } else {
                                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NotLeader")));
                                    }
                                } else {
                                    p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerNotInParty").replace("%player%", t.getDisplayName())));
                                }
                            } else {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerNotFound").replace("%player%", args[1])));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NotInParty")));
                        }
                    } else if (!PartyManager.isInParty(p) &&
                            (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("i"))) {
                        if (t != null) {
                            if (t != p) {
                                if (!PartyManager.isInParty(t)) {
                                    if (!PartyManager.contains(t)) {
                                        if (SettingsManager.isGettigPartyInvites(t.getName())) {
                                            Requests req = new Requests();
                                            req.invite = t;
                                            req.leader = p;
                                            req.time = System.currentTimeMillis();
                                            PartyManager.req.add(req);

                                            PartyManager.parties.add(new Party(p));

                                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Invited").replace("%player%", args[1])));

                                            TextComponent message = new TextComponent(String.valueOf(PartyMessageUtils.prefix) + "     ");

                                            TextComponent accept = new TextComponent("§8[§aПРИНЯТЬ");
                                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + p.getName()));
                                            message.addExtra(accept);

                                            message.addExtra(new TextComponent(" §7| "));

                                            TextComponent deny = new TextComponent("§cОТКЛОНИТЬ§8]");
                                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + p.getName()));
                                            message.addExtra(deny);

                                            t.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerInvite").replace("%player%", p.getDisplayName())));
                                            t.sendMessage(message);
                                        } else {
                                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NotAllowingInvites")));
                                        }
                                    } else {
                                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.AlreadySentARequest")));
                                    }
                                } else {
                                    p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.AlreadyInParty").replace("%player%", t.getDisplayName())));
                                }
                            } else {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.CantInteractSelf")));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerNotFound").replace("%player%", args[1])));
                        }
                    } else if (PartyManager.isInParty(p) &&
                            (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("i"))) {
                        if (t != null) {
                            if (t != p) {
                                if ((PartyManager.getPlayerParty(p)).members.get(p) == PartyRank.MOD || (PartyManager.getPlayerParty(p)).members.get(p) == PartyRank.LEADER) {
                                    if (!PartyManager.isInParty(t)) {
                                        if (!PartyManager.contains(p)) {
                                            Requests req = new Requests();
                                            req.leader = p;
                                            req.invite = t;
                                            req.time = System.currentTimeMillis();
                                            PartyManager.req.add(req);

                                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Invited").replace("%player%", args[1])));

                                            TextComponent accept = new TextComponent(String.valueOf(PartyMessageUtils.prefix) + "§aПРИНЯТЬ");
                                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + p.getName()));

                                            TextComponent deny = new TextComponent(String.valueOf(PartyMessageUtils.prefix) + "§cОТКЛОНИТЬ");
                                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + p.getName()));

                                            t.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerInvite").replace("%player%", p.getDisplayName())));
                                            t.sendMessage(accept);
                                            t.sendMessage(deny);
                                        } else {
                                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.AlreadySentARequest")));
                                        }
                                    } else {
                                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.AlreadyInParty").replace("%player%", t.getDisplayName())));
                                    }
                                } else {
                                    p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.NotLeader")));
                                }
                            } else {
                                p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.CantInteractSelf")));
                            }
                        } else {
                            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PlayerNotFound").replace("%player%", args[1])));
                        }
                    } else {
                        showHelp = true;
                    }
                }
            } else {

                showHelp = true;
            }

            if (showHelp)
                for (int i = 1; i <= 35; ) {
                    String path = "Messages.Party.Help.Line" + i;

                    if (cfg.contains(path)) {
                        p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString(path)));
                        i++;
                    } else { break; }
                }
        } else {
            Utils.sendConsole(String.valueOf(PartyMessageUtils.prefix) + PartyMessageUtils.notPlayer);
        }
    }
}
