package ru.buseso.dreamtime.bungeefriends.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import ru.buseso.dreamtime.bungeefriends.utils.FileUtils;
import ru.buseso.dreamtime.bungeefriends.utils.PartyMessageUtils;
import ru.buseso.dreamtime.bungeefriends.utils.Utils;

import java.util.Collection;
import java.util.HashMap;

public class Party {
    private ProxiedPlayer leader;
    public HashMap<ProxiedPlayer, PartyRank> members;

    public Party(ProxiedPlayer leader) {
        this.members = new HashMap<>();
        this.leader = leader;
        this.members.put(leader, PartyRank.LEADER);
    }

    public void addMember(ProxiedPlayer member) { this.members.put(member, PartyRank.MEMBER); }

    public void promote(ProxiedPlayer p) {
        if (this.members.get(p) == PartyRank.MEMBER) {
            this.members.remove(p);
            this.members.put(p, PartyRank.MOD);
        } else if (this.members.get(p) == PartyRank.MOD) {
            setLeader(p);
        }
    }

    public void demote(ProxiedPlayer p) {
        if (this.members.get(p) == PartyRank.MOD) {
            this.members.remove(p);
            this.members.put(p, PartyRank.MEMBER);
        }
    }

    public void setLeader(ProxiedPlayer p) {
        this.members.remove(this.leader);
        this.members.put(this.leader, PartyRank.MOD);
        this.members.remove(p);
        this.members.put(p, PartyRank.LEADER);
        this.leader = p;
    }

    public void leaveParty(ProxiedPlayer p) {
        Configuration cfg = FileUtils.getConfig();
        for (ProxiedPlayer member : getMembers()) {
            member.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Left").replace("%player%", p.getDisplayName())));
        }
        this.members.remove(p);
        if (this.leader == p) {
            for (ProxiedPlayer member : getMembers()) {
                member.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Deleted")));
            }

            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Deleted")));
            PartyManager.parties.remove(this);
        } else if(members.size() == 1) {
            p.sendMessage(Utils.getAsBaseComponent(String.valueOf(PartyMessageUtils.prefix) + cfg.getString("Messages.Party.Deleted")));
            PartyManager.parties.remove(this);
        }
    }

    public ProxiedPlayer getLeader() { return this.leader; }
    public Collection<ProxiedPlayer> getMembers() { return this.members.keySet(); }
    public boolean isMember(ProxiedPlayer p) { return getMembers().contains(p); }
}
