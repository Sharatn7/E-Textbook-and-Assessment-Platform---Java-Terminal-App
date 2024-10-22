import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    

            switch (choice) {
                case 1:
                    authenticateUser("ADMIN");
                    break;
                case 2:
                    authenticateUser("FACULTY");
                    break;
                case 3:
                    authenticateUser("TA");
                    break;
                case 4:
                    authenticateUser("STUDENT");
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
    }

    private static void authenticateUser(String role) {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        boolean isAuthenticated = checkCredentials(userId, password, role);

        if (isAuthenticated) {
        	// Redirect to appropriate home page based on role
            System.out.println("Login successful! Redirecting to " + role + " home page...");
        } else {
            System.out.println("Login incorrect. Please try again.");
        }
    }

    private static boolean checkCredentials(String userId, String password, String role) {

    	String sql = "SELECT COUNT(*) FROM User WHERE user_id = ? AND password = ? AND role = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            pstmt.setString(3, role);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count is greater than 0, credentials are valid
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if any exception occurs or no match is found
    }
    }
}