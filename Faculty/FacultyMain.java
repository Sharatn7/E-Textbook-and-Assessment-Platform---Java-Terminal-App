import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class FacultyMain {
	public static String facultyId = "";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean applicationRunning = true;

        while (applicationRunning) {
            boolean isLoggedIn = false;

            // Home Page: Faculty login
            while (!isLoggedIn) {
                System.out.println("=== Faculty Login ===");
                System.out.print("Enter User ID: ");
                String userId = scanner.nextLine();

                System.out.print("Enter Password: ");
                String password = scanner.nextLine();

                // Validate the credentials with database
                facultyId = validateCredentials(userId, password);
                System.out.println("Login status."+ facultyId);

                if (facultyId != null) {
                    System.out.println("Login Successful!");
                    isLoggedIn = true;  // Set logged-in status to true
                } else {
                    System.out.println("Login Incorrect. Please try again.");
                }
            }

            // Faculty landing page
            if (isLoggedIn && facultyId != null) {
                FacultyLandingPage.facultyLandingMenu(scanner, facultyId);  // Call the landing menu with facultyId
            }
        }

        scanner.close();
    }

    // Function to validate user credentials from the database
    public static String validateCredentials(String userId, String password) {
        
        // Corrected SQL query with back ticks around column names that contain spaces
        String query = "SELECT user_id FROM Users WHERE user_id = ? AND password = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for the prepared statement
            stmt.setString(1, userId);
            stmt.setString(2, password);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Get the faculty ID if login is successful
               return rs.getString("user_id");
            }
            else
            {
            	System.out.println("Invalid Creds");
            	FacultyMain.main(new String[0]);
            	
            }

        } catch (SQLException e) {
        	
            System.out.println("Database connection or query failed.");
            e.printStackTrace();
        }

        // Improved output readability in the log
        System.out.println("Validate Credentials: " + (facultyId != null ? facultyId : "No ID found"));
        return facultyId;  // Return the faculty ID if login successful, otherwise null
    }

}