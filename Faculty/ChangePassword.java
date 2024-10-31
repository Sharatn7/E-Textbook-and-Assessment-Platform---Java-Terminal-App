import java.sql.*;
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
                	if (verifyCurrentPassword(facultyId, currentPassword)) {
                        if (newPassword.equals(confirmPassword)) {
                            updatePassword(facultyId, newPassword);
                            System.out.println("Password updated successfully!");
                            inChangePassword = false;
                        } else {
                            System.out.println("New password and confirm password do not match.");
                        }
                    } else {
                        System.out.println("Current password is incorrect.");
                    }
                    
                    break;
                
                case 2:
                    // Go back to Faculty Landing Page
                    System.out.println("Returning to Faculty Landing Page...");
                    return;
                default:
                    System.out.println("Invalid choice! Please select 1 to update or 2 to cancel.");
            }
        }
    }
    private static boolean verifyCurrentPassword(String facultyId, String currentPassword) {
        String query = "SELECT password FROM Users WHERE user_id = ?";
        
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, facultyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    return storedPassword.equals(currentPassword); // Return true if passwords match
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false; // Return false if password doesn't match or an error occurs
    }

    private static void updatePassword(String facultyId, String newPassword) {
        String updateQuery = "UPDATE Users SET password = ? WHERE user_id = ?";
        
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setString(1, newPassword);
            statement.setString(2, facultyId);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Password updated in the database.");
            } else {
                System.out.println("Failed to update password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
