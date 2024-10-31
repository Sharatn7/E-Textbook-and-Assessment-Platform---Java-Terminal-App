import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ViewCourses {

	// Method to view courses assigned to a faculty using DB integration
    public static void viewCourses(Scanner scanner, String facultyId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();  // Get DB connection
            if (conn != null) {
                String query = "SELECT course_id,title FROM Courses WHERE course_id IN (SELECT course_id from Faculty where faculty_id = ?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, facultyId);  // Use facultyId from the login session

                rs = pstmt.executeQuery();

                System.out.println("Assigned Courses");
                System.out.println("CourseId   CourseName");
                while (rs.next()) {
                    System.out.println(rs.getString("course_id")+" "+rs.getString("title"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving courses.");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
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

}
