package ru.buseso.dreamtime.bungeefriends.sql;

import ru.buseso.dreamtime.bungeefriends.BungeeFriends;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FriendManager {
    public static boolean isRequestOpen(String uuid, String requested) { return getRequests(requested).contains(uuid); }


    public static ArrayList<String> getRequests(String uuid) {
        ArrayList<String> list = new ArrayList<>();

        if (isRegistered(uuid)) {
            ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM Friends WHERE UUID = '" + uuid.toString() + "'");

            if (rs != null) {
                try {
                    if (rs.next()) {
                        String s = rs.getString("FriendRequests");

                        if (s != null && !s.equalsIgnoreCase("[]")) {
                            byte b; 
                            int i;
                            String[] arrayOfString = s.replace("[", "").replace("]", "").split(", ");
                            for (i = arrayOfString.length, b = 0; b < i; ) {
                                String string = arrayOfString[b];
                                list.add(string.trim());
                                b++; 
                            }

                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return list;
            }
        }

        return list;
    }

    public static void addRequest(String uuid, String requested) {
        ArrayList<String> friends = getFriends(requested);
        ArrayList<String> requests = getRequests(requested);

        if (!requests.contains(uuid)) {
            requests.add(uuid);
        }
        BungeeFriends.mysql.update("DELETE FROM Friends WHERE UUID = '" + requested + "'");
        BungeeFriends.mysql.updateWithBoolean("INSERT INTO Friends (UUID, FriendUUIDs, FriendRequests, OnlineStatus) VALUES ('" + requested + "', '" + friends.toString() + "', '" + requests.toString() + "', ?)", true);
    }

    public static void removeRequest(String uuid, String requested) {
        ArrayList<String> friends = getFriends(requested);
        ArrayList<String> requests = getRequests(requested);

        if (requests.contains(uuid)) {
            requests.remove(uuid);
        }
        BungeeFriends.mysql.update("DELETE FROM Friends WHERE UUID = '" + requested + "'");
        BungeeFriends.mysql.updateWithBoolean("INSERT INTO Friends (UUID, FriendUUIDs, FriendRequests, OnlineStatus) VALUES ('" + requested + "', '" + friends.toString() + "', '" + requests.toString() + "', ?)", true);
    }

    public static void addFriend(String user, String friend) {
        if (!isFriend(user, friend)) {
            ArrayList<String> requests = getRequests(user);
            ArrayList<String> friends = getFriends(user);

            if (!friends.contains(friend)) {
                friends.add(friend);
            }
            BungeeFriends.mysql.update("DELETE FROM Friends WHERE UUID = '" + user + "'");
            BungeeFriends.mysql.updateWithBoolean("INSERT INTO Friends (UUID, FriendUUIDs, FriendRequests, OnlineStatus) VALUES ('" + user + "', '" + friends.toString() + "', '" + requests.toString() + "', ?)", true);
        }
    }

    public static void removeFriend(String user, String friend) {
        if (isFriend(user, friend)) {
            ArrayList<String> requests = getRequests(user);
            ArrayList<String> friends = getFriends(user);

            if (friends.contains(friend)) {
                friends.remove(friend);
            }
            BungeeFriends.mysql.update("DELETE FROM Friends WHERE UUID = '" + user + "'");
            BungeeFriends.mysql.updateWithBoolean("INSERT INTO Friends (UUID, FriendUUIDs, FriendRequests, OnlineStatus) VALUES ('" + user + "', '" + friends.toString() + "', '" + requests.toString() + "', ?)", true);
        }
    }


    public static boolean isFriend(String user, String friend) { return getFriends(user).contains(friend); }


    public static ArrayList<String> getFriends(String user) {
        ArrayList<String> list = new ArrayList<>();

        if (isRegistered(user)) {
            ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM Friends WHERE UUID = '" + user + "'");

            if (rs != null) {
                try {
                    if (rs.next()) {
                        String s = rs.getString("FriendUUIDs");

                        if (s != null &&
                                !s.equalsIgnoreCase("[]")) {
                            byte b; int i;
                            String[] arrayOfString = s.replace("[", "").replace("]", "").split(", ");
                            for (i = arrayOfString.length, b = 0; b < i; ) {
                                String string = arrayOfString[b];
                                list.add(string.trim());
                                b++; }

                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return list;
            }
        }

        return list;
    }

    public static void registerIfNeeded(String user) {
        if (!isRegistered(user))
            BungeeFriends.mysql.updateWithBoolean("INSERT INTO Friends (UUID, FriendUUIDs, OnlineStatus) VALUES ('" + user + "', '" + (new ArrayList()).toString() + "', ?)", true);
    }

    public static void setOnline(String user, boolean online) {
        registerIfNeeded(user);

        BungeeFriends.mysql.updateWithBoolean("UPDATE Friends SET OnlineStatus = ? WHERE UUID = '" + user + "'", online);
    }

    public static boolean isRegistered(String user) {
        ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM Friends WHERE UUID = '" + user + "'");

        try {
            if (rs != null &&
                    rs.next() &&
                    rs.getString("FriendUUIDs") != null) {
                return true;

            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isOnline(String user) {
        ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM Friends WHERE UUID = '" + user + "'");

        if (rs != null) {
            try {
                if (rs.next()) {
                    return rs.getBoolean("OnlineStatus");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
