package com.crypto.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;

/**
 * Intentionally making a DbUtils class for database calls due to the nature of the queries.
 * The queries are not complex, and entities do not need to be represented by classes.
 */
public class DbUtils
{
    private String driver;
    private String connectionUrl;
    private String username;
    private String password;

    public DbUtils() {
        try {
            Properties props = new Properties();
            InputStream stream = DbUtils.class.getClassLoader().getResourceAsStream("database.properties");

            props.load(stream);
            stream.close();

            this.driver = props.getProperty("mysql.driver");
            this.connectionUrl = props.getProperty("mysql.connection.url");
            this.username = props.getProperty("mysql.connection.username");
            this.password = props.getProperty("mysql.connection.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Double getHighestEtherValue() {
        Double highestValue = null;

        try {
            // Create connection
            Class.forName(this.driver);
            Connection con = DriverManager.getConnection(this.connectionUrl, this.username, this.password);
            Statement stmt = con.createStatement();

            // Get current maximum increment (in hundreds)
            ResultSet rs = stmt.executeQuery("SELECT MAX(e.Increment) FROM EtherPriceIncrement e");
            if (rs.next()) {
                highestValue = rs.getDouble(1);
            }

            con.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return highestValue;
    }

    public void insertNewIncrement(Integer value) {
        try {
            Class.forName(this.driver);
            Connection con = DriverManager.getConnection(this.connectionUrl, this.username, this.password);

            // Template insert statement
            String query = "insert into EtherPriceIncrement (Increment, Timestamp)"
                    + " values (?, ?)";

            // Substitute variables
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, value);
            stmt.setDate(2, new Date(Calendar.getInstance().getTime().getTime()));

            // Execute statement and close the connection
            stmt.execute();
            con.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
