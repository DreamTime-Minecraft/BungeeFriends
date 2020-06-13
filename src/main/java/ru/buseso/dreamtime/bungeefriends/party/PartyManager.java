package ru.buseso.dreamtime.bungeefriends.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.ArrayList;
import java.util.HashMap;

public class PartyManager {
    public static ArrayList<Party> parties = new ArrayList();
    public static HashMap<ProxiedPlayer, ProxiedPlayer> requests = new HashMap();
    public static boolean isRequestOpen(ProxiedPlayer player, ProxiedPlayer requested)
    { return (requests.containsKey(player) && requests.get(player) == requested); }
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
