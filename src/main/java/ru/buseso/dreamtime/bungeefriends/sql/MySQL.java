package ru.buseso.dreamtime.bungeefriends.sql;

import java.sql.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MySQL {
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private Connection con;

    public MySQL(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;

        connect();
        update("CREATE TABLE IF NOT EXISTS Friends (UUID varchar(64), FriendUUIDs TEXT(32000), FriendRequests TEXT(3200), OnlineStatus boolean)");
        update("CREATE TABLE IF NOT EXISTS FriendSettings (UUID varchar(64), Requests boolean, Notify boolean, FriendChat boolean, ServerJumping boolean, PartyInvites boolean)");
    }

    public void connect() {
        try {
            this.con = DriverManager.getConnection(
                    "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "" +
                            "?autoReconnect=true", this.user, this.password);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("[MySQL] Could not connect");
        }
    }

    public void disconnect() {
        try {
            this.con.close();
            System.out.println("[MySQL] Disconnected from database");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("[MySQL] Could not disconnect");
        }
    }

    public synchronized void update(String qry) {
        connect();
        if (isConnected()) {
            (new FutureTask(new Runnable() {
                PreparedStatement ps;

                public void run() {
                    try {
                        this.ps = MySQL.this.con.prepareStatement(qry);

                        this.ps.executeUpdate();
                        this.ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }, Integer.valueOf(1))).run();
        } else {
            connect();
        }
    }

    public synchronized void updateWithBoolean(String qry, boolean value) {
        connect();
        if (isConnected()) {
            (new FutureTask(new Runnable() {
                PreparedStatement ps;

                public void run() {
                    try {
                        this.ps = MySQL.this.con.prepareStatement(qry);
                        this.ps.setBoolean(1, value);

                        this.ps.executeUpdate();
                        this.ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }, Integer.valueOf(1))).run();
        } else {
            connect();
        }
    }

    public synchronized ResultSet getResult(String qry) {
        connect();
        if (isConnected()) {
            try {
                FutureTask<ResultSet> task = new FutureTask<ResultSet>(new Callable<ResultSet>() {
                    PreparedStatement ps;
                    public ResultSet call() throws Exception {
                        this.ps = MySQL.this.con.prepareStatement(qry);
                        return this.ps.executeQuery();
                    }
                });
                task.run();
                return task.get();
            } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            connect();
        }
        return null;
    }

    public boolean isConnected() { return (this.con != null); }
}
