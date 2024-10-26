import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ViewStudents {

    public static void viewStudents(Scanner scanner) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection(); // Get connection from DBConnect

            if (conn != null) {
                // Update the query to select student names from Enrollments and Students based on course ID
                String query = "SELECT s.`Full Name`, e.Status " +
                               "FROM Enrollments e " +
                               "JOIN Students s ON e.`Student ID` = s.Student_id " +
                               "WHERE e.`Unique Course ID` = ? AND e.Status = 'Enrolled'";
                               
                pstmt = conn.prepareStatement(query);

                // Prompt the user to input the Course ID
                System.out.print("Enter the Course ID: ");
                String courseId = scanner.nextLine();  // Read the course ID from the user

                pstmt.setString(1, courseId);  // Set the Course ID in the query

                rs = pstmt.executeQuery();

                System.out.println("\n=== Students Enrolled in Course " + courseId + " ===");
                while (rs.next()) {
                    // Print the student full name and enrollment status
                    String fullName = rs.getString("Full Name");
                    String status = rs.getString("Status");
                    System.out.println("Student: " + fullName + " | Status: " + status);
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
        }
    }
}
