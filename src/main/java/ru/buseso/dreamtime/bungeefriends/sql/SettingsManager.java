package ru.buseso.dreamtime.bungeefriends.sql;

import ru.buseso.dreamtime.bungeefriends.BungeeFriends;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsManager {
    public static void registerIfNeeded(String user) {
        if (!isRegistered(user))
            BungeeFriends.mysql.update("INSERT INTO FriendSettings (UUID, Requests, Notify, FriendChat, ServerJumping, PartyInvites) VALUES ('" + user + "', true, true, true, true, true)");
    }

    public static boolean isRegistered(String user) {
        ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user + "'");

        try {
            if (rs != null &&
                    rs.next()) {
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void setGetRequests(String user, boolean b) {
        registerIfNeeded(user);

        BungeeFriends.mysql.updateWithBoolean("UPDATE FriendSettings SET Requests = ? WHERE UUID = '" + user + "'", b);
    }

    public static boolean isGettingRequests(String user) {
        ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user + "'");

        if (rs != null) {
            try {
                if (rs.next()) {
                    return rs.getBoolean("Requests");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static void setGetNotified(String user, boolean b) {
        registerIfNeeded(user);

        BungeeFriends.mysql.updateWithBoolean("UPDATE FriendSettings SET Notify = ? WHERE UUID = '" + user + "'", b);
    }

    public static boolean isGettingNotified(String user) {
        ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user + "'");

        if (rs != null) {
            try {
                if (rs.next()) {
                    return rs.getBoolean("Notify");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static void setUseFriendChat(String user, boolean b) {
        registerIfNeeded(user);

        BungeeFriends.mysql.updateWithBoolean("UPDATE FriendSettings SET FriendChat = ? WHERE UUID = '" + user + "'", b);
    }

    public static boolean isUsingFriendChat(String user) {
        ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user + "'");

        if (rs != null) {
            try {
                if (rs.next()) {
                    return rs.getBoolean("FriendChat");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static void setServerJuming(String user, boolean b) {
        registerIfNeeded(user);

        BungeeFriends.mysql.updateWithBoolean("UPDATE FriendSettings SET ServerJumping = ? WHERE UUID = '" + user + "'", b);
    }

    public static boolean isAllowingServerJumping(String user) {
        ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user + "'");

        if (rs != null) {
            try {
                if (rs.next()) {
                    return rs.getBoolean("ServerJumping");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static void setGetPartyInvites(String user, boolean b) {
        registerIfNeeded(user);

        BungeeFriends.mysql.updateWithBoolean("UPDATE FriendSettings SET PartyInvites = ? WHERE UUID = '" + user + "'", b);
    }

    public static boolean isGettigPartyInvites(String user) {
        ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user + "'");

        if (rs != null) {
            try {
                if (rs.next()) {
                    return rs.getBoolean("PartyInvites");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
