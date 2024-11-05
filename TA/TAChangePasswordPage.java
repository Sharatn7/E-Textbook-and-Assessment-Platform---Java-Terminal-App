package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAChangePasswordPage {

    private String taId;

    public TAChangePasswordPage() {
        this.taId = HomePage.currentTAId; // Retrieve TA_id from HomePage's global variable
    }

    public void showChangePasswordPage(Scanner scanner) {
        if (taId == null) {
            System.out.println("Error: TA ID is not set. Please log in as a TA.");
            return;
        }

        System.out.print("Enter Current Password: ");
        String inputCurrentPassword = scanner.nextLine();

        System.out.print("Enter New Password: ");
        String newPassword = scanner.nextLine();

        System.out.print("Confirm New Password: ");
        String confirmPassword = scanner.nextLine();

        boolean exit = false;

        while (!exit) {
            System.out.println("Menu:");
            System.out.println("1. Update Password");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    // Verify the current password before updating
                    if (isCurrentPasswordCorrect(inputCurrentPassword)) {
                        if (newPassword.equals(confirmPassword)) {
                            if (updatePassword(newPassword)) {
                                System.out.println("Password updated successfully.");
                                exit = true;
                            } else {
                                System.out.println("Failed to update password. Please try again.");
                            }
                        } else {
                            System.out.println("New password and confirmation do not match. Please try again.");
                        }
                    } else {
                        System.out.println("Current password is incorrect. Please try again.");
                    }
                }
                case 2 -> {
                    System.out.println("Returning to TA Landing Page...");
                    exit = true;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private boolean isCurrentPasswordCorrect(String inputCurrentPassword) {
        String sql = "SELECT password FROM Users WHERE user_id = ? AND role = 'TA'";
        try (Connection conn = DBConnect.getConnection()){
        	PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, taId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String currentPassword = resultSet.getString("password");
                    return currentPassword.equals(inputCurrentPassword); // Check if passwords match
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while verifying password: " + e.getMessage());
        }
        return false;
    }

    private boolean updatePassword(String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE user_id = ? AND role = 'TA'";
        try (Connection conn = DBConnect.getConnection()) {
        PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, newPassword);
            statement.setString(2, taId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the password was successfully updated
        } catch (SQLException e) {
            System.out.println("Database error while updating password: " + e.getMessage());
        }
        return false;
    }
}
