import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAChangePasswordPage {

    private String taId;

    public TAChangePasswordPage(String taId) {
        this.taId = taId;
    }

    public void showChangePasswordPage(Scanner scanner) {
        boolean passwordValidated = false;

        while (!passwordValidated) {
            System.out.print("Enter Current Password: ");
            String inputCurrentPassword = scanner.nextLine();

            if (isCurrentPasswordCorrect(inputCurrentPassword)) {
                System.out.println("Current password is correct.");
                passwordValidated = true;
            } else {
                System.out.println("Current password is incorrect. Please try again.");
            }
        }

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
                    System.out.print("Enter New Password: ");
                    String newPassword = scanner.nextLine();

                    System.out.print("Confirm New Password: ");
                    String confirmPassword = scanner.nextLine();

                    if (newPassword.equals(confirmPassword)) {
                        if (updatePassword(newPassword)) {
                            System.out.println("Password updated successfully.");
                            exit = true;
                            break;
                        } else {
                            System.out.println("Failed to update password. Please try again.");
                        }
                    } else {
                        System.out.println("New password and confirmation do not match. Please try again.");
                    }
                }
                case 2 -> {
                    System.out.println("Returning to TA Landing Page...");
                    exit = true;
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private boolean isCurrentPasswordCorrect(String inputPassword) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {

                String sql = "SELECT password FROM User WHERE user_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, taId);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String currentPassword = rs.getString("password");
                    return inputPassword.equals(currentPassword);
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updatePassword(String newPassword) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                String sql = "UPDATE User SET password = ? WHERE user_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, newPassword);
                pstmt.setString(2, taId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
