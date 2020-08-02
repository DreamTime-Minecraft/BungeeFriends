package ru.buseso.dreamtime.bungeefriends.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.ArrayList;
import java.util.List;

public class PartyManager {
    public static ArrayList<Party> parties = new ArrayList();

    public static List<Requests> req = new ArrayList<>();
    public synchronized static boolean isReqOpen(ProxiedPlayer invite, ProxiedPlayer leader) {
        for(Requests reqs : req) {
            if(reqs.leader.getUniqueId().equals(leader.getUniqueId())) {
                if(reqs.invite.getUniqueId().equals(invite.getUniqueId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized static void reqRemove(ProxiedPlayer invite) {
        req.removeIf(reqs -> reqs.invite.getUniqueId().equals(invite.getUniqueId()));
    }

    public synchronized static boolean contains(ProxiedPlayer invite) {
        for(Requests reqs : req) {
            if(reqs.invite.getUniqueId().equals(invite.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    /*public static HashMap<ProxiedPlayer, ProxiedPlayer> requests = new HashMap();
    public static boolean isRequestOpen(ProxiedPlayer player, ProxiedPlayer requested)
    { return (requests.containsKey(player) && requests.get(player) == requested); }*/

    public static boolean isInParty(ProxiedPlayer p) {
        boolean b = false;
        for (Party party : parties) {
            if (party.isMember(p)) {
                b = true;
            }
        }
        return b;
    }

    public static Party getPlayerParty(ProxiedPlayer p) {
        if (isInParty(p)) {
            for (Party party : parties) {
                if (party.isMember(p)) {
                    return party;
                }
            }
        }
        return null;
    }
}
