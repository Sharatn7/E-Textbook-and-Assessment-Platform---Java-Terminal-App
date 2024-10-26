import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ApproveEnrollment {

    public static void approveEnrollment(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection(); // Get connection from DBConnect

            if (conn != null) {
                // Update status in Enrollments table based on the provided Student ID
                String query = "UPDATE Enrollments SET status = 'Enrolled' WHERE `Student ID` = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, studentId);  // Set the Student ID in the prepared statement

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Enrollment approved for Student ID: " + studentId);
                } else {
                    System.out.println("Failed to approve enrollment. No matching record found.");
                }
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

        System.out.println("\n1. Go Back");
        System.out.print("Enter choice (1 to Go Back): ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (choice == 1) {
            System.out.println("Returning to Active Course Menu...");
        }
    }
}
