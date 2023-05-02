package lahmmp.budget;

import java.sql.*;

public class ConnectionSQL {

    public Connection connectToDatabase() {
        String url = "jdbc:mysql://localhost/db_planinator";
        String user = "root";
        String password = "Sonne";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}


