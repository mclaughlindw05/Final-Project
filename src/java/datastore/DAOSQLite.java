package datastore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.User;

/**
 * DAOSQLite Data Access Object for an SQLite database
 *
 * @author John Phillips
 * @version 0.3 on 2015-11-03
 */
public class DAOSQLite {

    protected final static String DRIVER = "org.sqlite.JDBC";
    protected final static String JDBC = "jdbc:sqlite";

    /**
     * Inserts an record into the database table. Note the use of a
     * parameterized query to prevent SQL Injection attacks.
     *
     * @param employee the object to insert
     * @param dbPath the path to the SQLite database
     */
    public static void createRecord(User user, String dbPath) {
        String q = "insert into user (id, player, level, role, character, race, alignment) "
                + "values (null, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnectionDAO(dbPath);
                PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setString(1, user.getPlayer());
            ps.setInt(2, user.getLevel());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getCharacter());
            ps.setString(5, user.getRace());
            ps.setString(6, user.getAlignment());

            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOSQLite.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

//   /**
//     * Retrieve a record given an empId.
//     *
//     * @param empId the empId of the record to retrieve
//     * @param dbPath the path to the SQLite database
//     * @return User object
//     */
//    public static User retrieveRecordById(int empId, String dbPath) {
//        String q = "select empId, lastName, firstName, homePhone, salary from employee where empId = ?";
//        User employee = null;
//        try (Connection conn = getConnectionDAO(dbPath);
//                PreparedStatement ps = conn.prepareStatement(q)) {
//            ps.setInt(1, empId);
//            employee = myQuery(conn, ps).get(0);
//        } catch (SQLException ex) {
//            Logger.getLogger(DAOSQLite.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
//        return employee;
//    } 
    /**
     * Retrieve all of the records in the database as a list sorted by player.
     *
     * @param dbPath the path to the SQLite database
     * @return list of objects
     */
    public static List<User> retrieveAllRecordsByName(String dbPath) {
        String q = "select * from user order by player";
        List<User> list = null;
        try (Connection conn = getConnectionDAO(dbPath);
                PreparedStatement ps = conn.prepareStatement(q)) {
            list = myQuery(conn, ps);
        } catch (SQLException ex) {
            Logger.getLogger(DAOSQLite.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Update a record from the database given an employee object. Note the use
     * of a parameterized query to prevent SQL Injection attacks.
     *
     * @param employee the employee record to update
     * @param dbPath the path to the SQLite database
     */
//    public static void updateRecord(User user, String dbPath) {
//        String q = "update employee set lastName=?, firstName=?, homePhone=?, salary=? where empId = ?";
//        try (Connection conn = getConnectionDAO(dbPath);
//                PreparedStatement ps = conn.prepareStatement(q)) {
//            ps.setString(1, user.getPlayer());
//            ps.setInt(2, user.getBloodSugar());
//            ps.setString(3, user.getDate());
//            ps.setString(4, user.getEvent());
//            ps.setString(5, user.getHealth());
//            ps.setString(6, user.getNotes());
//            ps.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(DAOSQLite.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    /**
     * Delete a record from the database given its id. Note the use of a
     * parameterized query to prevent SQL Injection attacks.
     *
     * @param id the id of the record to delete
     * @param dbPath the path to the SQLite database
     */
    public static void deleteRecord(int id, String dbPath) {
        String q = "delete from user where id = ?";
        try (Connection conn = getConnectionDAO(dbPath);
                PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOSQLite.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a new employee table.
     *
     * @param dbPath the path to the SQLite database
     */
    public static void createTable(String dbPath) {
        String q = "create table user ("
                + "id integer not null primary key autoincrement, "
                + "player varchar(20) not null, "
                + "level integer not null, "
                + "role character(10) not null, "
                + "character varchar(20) not null, "
                + "race varchar(20) null, "
                + "alignment varchar(20) null)";
        try (Connection conn = getConnectionDAO(dbPath);
                PreparedStatement ps = conn.prepareStatement(q)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOSQLite.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Drops the employee table erasing all of the data.
     *
     * @param dbPath the path to the SQLite database
     */
    public static void dropTable(String dbPath) {
        final String q = "drop table if exists user";
        try (Connection conn = getConnectionDAO(dbPath);
                PreparedStatement ps = conn.prepareStatement(q)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOSQLite.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Populates the table with sample data records.
     *
     * @param dbPath the path to the SQLite database
     */
    public static void populateTable(String dbPath) {
        User p, q, r, s;
        p = new User(0, "David", 1, "Wizard", "Character1", "Elf", "Chaotic Good");
        q = new User(0, "David", 5, "Fighter", "Character2", "Human", "Lawful Neutral");
        r = new User(0, "Ryan", 3, "Rogue", "Character3", "Gnome", "Chaotic Evil");
        s = new User(0, "Taylor", 2, "Cleric", "Character4", "Dwarf", "Lawful Good");
        DAOSQLite.createRecord(p, dbPath);
    }

    /**
     * A helper method that executes a prepared statement and returns the result
     * set as a list of objects.
     *
     * @param conn a connection to the database
     * @param ps a prepared statement
     * @return list of objects from the result set
     */
    protected static List<User> myQuery(Connection conn, PreparedStatement ps) {
        List<User> list = new ArrayList();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String player = rs.getString("player");
                int level = rs.getInt("level");
                String role = rs.getString("role");
                String character = rs.getString("character");
                String race = rs.getString("race");
                String alignment = rs.getString("alignment");
                User p = new User(id, player, level, role, character, race, alignment);
                list.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOSQLite.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Creates a connection to the SQLite database.
     *
     * @param dbPath the path to the SQLite database
     * @return connection to the database
     */
    protected static Connection getConnectionDAO(String dbPath) {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(JDBC + ":" + dbPath);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DAOSQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
}
