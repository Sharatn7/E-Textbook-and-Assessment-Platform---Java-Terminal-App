import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ViewWorklist {

    public static void viewWorklist(Scanner scanner) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection(); // Get connection from DBConnect

            if (conn != null) {
                String query = "SELECT student_name FROM Worklist WHERE course_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, "courseId"); // Set course ID

                rs = pstmt.executeQuery();

                System.out.println("\n=== Worklist ===");
                while (rs.next()) {
                    System.out.println(rs.getString("student_name"));
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
