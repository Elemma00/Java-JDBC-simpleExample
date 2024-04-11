package org.emma.java.jdbc.sqlconec.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static String url = "jdbc:mysql://localhost:3306/java_curso?serverTimezone=America/Santiago";
    private static String username = "root";
    private static String password = "sasa";

    public static Connection getInstance() throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }

}
