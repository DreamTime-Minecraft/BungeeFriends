package ru.buseso.dreamtime.bungeefriends.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import ru.buseso.dreamtime.bungeefriends.party.PartyManager;
import ru.buseso.dreamtime.bungeefriends.utils.FileUtils;
import ru.buseso.dreamtime.bungeefriends.utils.PartyMessageUtils;
import ru.buseso.dreamtime.bungeefriends.utils.Utils;

public class PartyManagerCommand extends Command {
    public PartyManagerCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("bungeefriends.partymanager")) {
            Configuration cfg = FileUtils.getConfig();
            if(args.length == 0) {
                TextComponent text = new TextComponent("§0" +
                        "\n§7 > /pman send <игрок> <сообщение> - отправить сообщение в пати игрока" +
                        "\n§7 > /pman join <игрок> - присоединиться в пати к игроку");
                sender.sendMessage(text);
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("join")) {
                    String player = args[1];
                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);
                    if(p == null) {
                        TextComponent text = new TextComponent("§cИгрок не найден!");
                        sender.sendMessage(text);
                    } else {
                        if(!PartyManager.isInParty(p)) {
                            TextComponent text = new TextComponent("§cИгрок не в пати!");
                            sender.sendMessage(text);
                        } else {
                            PartyManager.getPlayerParty(p).addMember((ProxiedPlayer)sender);
                            TextComponent text = new TextComponent("§aВы успешно вошли в пати игрока "+p.getName()+".");
                        }
                    }
                } else {
                    TextComponent text = new TextComponent("§0" +
                            "\n§7 > /pman send <игрок> <сообщение> - отправить сообщение в пати игрока" +
                            "\n§7 > /pman join <игрок> - присоединиться в пати к игроку");
                    sender.sendMessage(text);
                }
            } else if(args.length >= 3) {
                if(args[0].equalsIgnoreCase("send")) {
                    String player = args[1];
                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);
                    if(p == null) {
                        TextComponent text = new TextComponent("§cИгрок не найден!");
                        sender.sendMessage(text);
                    } else {
                        if(!PartyManager.isInParty(p)) {
                            TextComponent text = new TextComponent("§cИгрок не в пати!");
                            sender.sendMessage(text);
                        } else {
                            StringBuilder sb = new StringBuilder();
                            for(int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String msg = sb.toString();

                            for (ProxiedPlayer member : PartyManager.getPlayerParty(p).getMembers()) {
                                member.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.PartyChat").replace("%player%", sender.getName()).replace("%message%", msg)));
                            }
                            TextComponent text = new TextComponent("§aСообщение \""+msg+"\" было отправлено в пати игроку "+p.getName());
                        }
                    }
                }
            } else {
                TextComponent text = new TextComponent("§0" +
                        "\n§7 > /pman send <игрок> <сообщение> - отправить сообщение в пати игрока" +
                        "\n§7 > /pman join <игрок> - присоединиться в пати к игроку");
                sender.sendMessage(text);
            }
        }
    }
}
