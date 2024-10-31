import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    private static final Scanner scanner = new Scanner(System.in);
    private static String authenticatedUserId = null;

    // Public method to authenticate a user
    public static boolean authenticateUser() {
        while (true) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            System.out.println("1. Sign-In");
            System.out.println("2. Go Back");
            System.out.print("Choose option (1-2): ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over by nextInt()

            if (option == 1) {
                // Proceed with checking the credentials
                if (checkCredentials(userId, password)) {
                    authenticatedUserId = userId;
                    System.out.println("Login successful.");
                    return true;
                } else {
                    System.out.println("Login Incorrect. Please try again.");
                }
            } else if (option == 2) {
                // Option to go back to the main menu
                System.out.println("Going back to main menu.");
                return false;
            } else {
                // Handle invalid input
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }

 // Helper method to check user credentials in the database
    private static boolean checkCredentials(String userId, String password) {
        // Updated table and column names according to new schema
        String sql = "SELECT COUNT(*) FROM Users WHERE user_id = ? AND password = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count is greater than 0, credentials are valid
            }
        } catch (SQLException e) {
            System.out.println("Database connection or query failed.");
            e.printStackTrace();
        }
        return false; // Return false if any exception occurs or no match is found
    }

    // Public method to get the currently authenticated user ID
    public static String getAuthenticatedUserId() {
        return authenticatedUserId;
    }
}
