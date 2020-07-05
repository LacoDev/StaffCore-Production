package de.lacodev.rsystem.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import de.lacodev.rsystem.Main;

public class MySQL {
	
	   
    // Defines Login credentials for MySQL Database
    private static String host = Main.getInstance().host;
    private static String port = Main.getInstance().port;
    private static String username = Main.getInstance().username;
    private static String password = Main.getInstance().password;
    private static String database = Main.getInstance().database;
    
    private static Connection con;
    
    private static String mysql = "§cSystem §8(§7MySQL§8) §8- ";
    
    // Opens MySQL Connection
    public static void connect() {
        Bukkit.getConsoleSender().sendMessage(mysql + "§7Hooking §cDatabase Services§7...");
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setCon(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoreconnect=true&useSSL=false", username, password));
            Bukkit.getConsoleSender().sendMessage(mysql + "§aSuccessfully §7hooked §aDatabase " + database);
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(mysql + "§cFailed §7to hook §aDatabase Services");
            Bukkit.getConsoleSender().sendMessage(mysql + "§7May the service is in maintenance...");
            Bukkit.getConsoleSender().sendMessage(mysql + "§7Please check your database host!");
        } catch (ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage(mysql + "§cFailed §7to hook §aDatabase Services");
            Bukkit.getConsoleSender().sendMessage(mysql + "§7May the service is in maintenance...");
            Bukkit.getConsoleSender().sendMessage(mysql + "§7Please check your database host!");
        }
    }
    
    // Returns boolean if MySQL isConnected
    public static boolean isConnected() {
        return (getCon() == null ? false : true);
    }
    
    public static void disconnect() {
        if(isConnected()) {
            Bukkit.getConsoleSender().sendMessage(mysql + "§7Detaching from §cDatabase Services§7...");
            try {
                getCon().close();
                Bukkit.getConsoleSender().sendMessage(mysql + "§aSuccessfully §7detached from §aDatabase " + database);
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage(mysql + "§cFailed §7to detach from §aDatabase Services");
                Bukkit.getConsoleSender().sendMessage(mysql + "§7May the service is not connected...");
                Bukkit.getConsoleSender().sendMessage(mysql + "§7Please check your database host!");
            }
        }
    }
    
    // Creates MySQL Table
    public static void createTable() {
        if(isConnected()) {
        	try {
            	PreparedStatement st1 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_playerdb(id INT(6) AUTO_INCREMENT UNIQUE, UUID VARCHAR(255), PLAYERNAME VARCHAR(255), BANS INT(6), MUTES INT(6), REPORTS INT(6), LAST_KNOWN_IP VARCHAR(255))");
            	st1.executeUpdate();
            	PreparedStatement st2 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_reportsdb(id INT(6) AUTO_INCREMENT UNIQUE, REPORTER_UUID VARCHAR(255), REPORTED_UUID VARCHAR(255), SERVERNAME VARCHAR(255), REASON VARCHAR(255), TEAM_UUID VARCHAR(255), STATUS INT(6))");
            	st2.executeUpdate();
            	PreparedStatement st3 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_bansdb(id INT(6) AUTO_INCREMENT UNIQUE, BANNED_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255), BAN_END LONG)");
            	st3.executeUpdate();
            	PreparedStatement st4 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_mutesdb(id INT(6) AUTO_INCREMENT UNIQUE, MUTED_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255), MUTE_END LONG)");
            	st4.executeUpdate();
            	PreparedStatement st5 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_actionsdb(id INT(6) AUTO_INCREMENT UNIQUE, ACTION VARCHAR(255), EXECUTOR_UUID VARCHAR(255), DESCRIPTION VARCHAR(255))");
            	st5.executeUpdate();
            	PreparedStatement st6 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_reasonsdb(id INT(6) AUTO_INCREMENT UNIQUE, TYPE VARCHAR(255), NAME VARCHAR(255), BAN_LENGTH LONG, REPORT_ITEM VARCHAR(255))");
            	st6.executeUpdate();
            	PreparedStatement st7 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_webaccounts(id INT(6) AUTO_INCREMENT UNIQUE, UUID VARCHAR(255), PLAYERNAME VARCHAR(255), EMAIL VARCHAR(255), PASSWORD VARCHAR(255), USERRANK INT(6), VerifyToken VARCHAR(255), LAST_LOGIN LONG)");
            	st7.executeUpdate();
            	PreparedStatement st8 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_appeals(id INT(6) AUTO_INCREMENT UNIQUE, BAN_ID INT(11), UUID VARCHAR(255), TEAM_UUID VARCHAR(255), TYPE VARCHAR(255), REASON VARCHAR(255), STATUS INT(6), Message VARCHAR(5000), reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
            	st8.executeUpdate();
            	PreparedStatement st9 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_chatlogs(id INT(6) AUTO_INCREMENT UNIQUE, CHATLOG_ID VARCHAR(25), REPORTED_UUID VARCHAR(255), MESSAGE VARCHAR(256), STATUS INT(6), reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
            	st9.executeUpdate();
            	PreparedStatement st10 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_ipbans(id INT(6) AUTO_INCREMENT UNIQUE, IP_ADDRESS VARCHAR(255), END LONG)");
            	st10.executeUpdate();
            	PreparedStatement st11 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_banhistory(id INT(6) AUTO_INCREMENT UNIQUE, BANNED_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255), BAN_START LONG, BAN_END LONG)");
            	st11.executeUpdate();
                Bukkit.getConsoleSender().sendMessage(mysql + "§aSuccessfully §7created/loaded §aMySQL-Table");
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage(mysql + "§cERROR while creating/loading MySQL-Table!");
            }
        }
    }
    public static void update(String qry) {
        if(isConnected()) {
            try {
                getCon().createStatement().executeUpdate(qry);
                
            } catch (SQLException e) {
            	Bukkit.getConsoleSender().sendMessage(mysql + "§cERROR while updating MySQL-Table");
            }
        }
    }
    public static ResultSet getResult(String qry) {
        if(isConnected()) {
            try {
                if(getCon().createStatement().executeQuery(qry) != null) {
                	return getCon().createStatement().executeQuery(qry);
                }
            } catch (SQLException e) {
            	Bukkit.getConsoleSender().sendMessage(mysql + "§cERROR while collecting data from MySQL-Table");
            }
        }
        return null;
        
        
    }
 
    public static Connection getCon() {
        return con;
    }
 
    public static void setCon(Connection con) {
        MySQL.con = con;
    }
    
 

}
