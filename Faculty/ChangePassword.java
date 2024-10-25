import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ChangePassword {

    public static void changePassword(Scanner scanner,String facultyId) {
        boolean inChangePassword = true;
        while (inChangePassword) {
            System.out.println("=== Change Password ===");
            System.out.print("Enter current password: ");
            String currentPassword = scanner.nextLine();

            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();

            System.out.print("Confirm new password: ");
            String confirmPassword = scanner.nextLine();

            // Display options: 1. Update 2. Cancel
            System.out.println("\n1. Update");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Simulate saving to database and display success or failure message
                    if (currentPassword.equals("password")) { // Add real validation logic here
                        if (newPassword.equals(confirmPassword)) {
                            // Save the new password to the database
                            if (savePasswordToDatabase(newPassword)) {
                                System.out.println("Password updated successfully!");
                            } else {
                                System.out.println("Failed to update password.");
                            }
                            inChangePassword = false;  // Exit after update
                        } else {
                            System.out.println("New passwords do not match. Please try again.");
                        }
                    } else {
                        System.out.println("Current password is incorrect. Please try again.");
                    }
                    break;
                
                case 2:
                    // Go back to Faculty Landing Page
                    System.out.println("Returning to Faculty Landing Page...");
                    inChangePassword = false;  // Exit the change password process
                    break;
                
                default:
                    System.out.println("Invalid choice! Please select 1 to update or 2 to cancel.");
            }
        }
    }

    // Method to save the new password to the database
    private static boolean savePasswordToDatabase(String newPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Get the connection from DBConnect class
            conn = DBConnect.getConnection();

            if (conn != null) {
                String updateSQL = "UPDATE Faculty SET password = ? WHERE faculty_id = ?";  // Adjust the query to match your schema
                pstmt = conn.prepareStatement(updateSQL);

                pstmt.setString(1, newPassword);
                pstmt.setInt(2, 1); // Example faculty_id, adjust as necessary

                int rowsUpdated = pstmt.executeUpdate();
                return rowsUpdated > 0;  // Return true if the update was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
