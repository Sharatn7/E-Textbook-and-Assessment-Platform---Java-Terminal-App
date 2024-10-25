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
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "UPDATE Enrollment SET status = 'Approved' WHERE student_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, studentId);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Enrollment approved for Student ID: " + studentId);
                } else {
                    System.out.println("Failed to approve enrollment.");
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
