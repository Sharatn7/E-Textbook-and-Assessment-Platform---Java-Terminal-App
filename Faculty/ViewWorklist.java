package Faculty;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ViewWorklist {

    public static void viewWorklist(Scanner scanner,String courseId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection(); // Get connection from DBConnect

            if (conn != null) {
                // Update the query to retrieve both Student ID and Status for a given Course ID
                String query = "SELECT student_id FROM Enrollments WHERE course_id = ?";
                pstmt = conn.prepareStatement(query);            

                pstmt.setString(1, courseId); // Set course ID for the prepared statement

                rs = pstmt.executeQuery();

                System.out.println("\n=== Worklist for Course ID: " + courseId + " ===");
                while (rs.next()) {
                    // Print both the Student ID and Status for each entry
                    String studentId = rs.getString("student_id");
                    
                    System.out.println(studentId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
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
            return;
        }
    }
}
