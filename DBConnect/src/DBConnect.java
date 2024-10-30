import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnect {

    private static final String URL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/bsuryad";
    private static final String USER = "bsuryad";
    private static final String PASSWORD = "200535557";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Successfully connected to DB");
        } catch (SQLException e) {
            System.out.println("Failed to connect to DB");
            e.printStackTrace();
        }
        return conn;
    }

//    public static void main(String[] args) {
//        Connection connection = getConnection();
//        if (connection != null) {
//            try {
//                connection.close();
//                System.out.println("Connection closed");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}