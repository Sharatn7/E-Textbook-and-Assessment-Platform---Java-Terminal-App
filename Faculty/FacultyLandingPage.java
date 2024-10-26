import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class FacultyLandingPage {

    public static void facultyLandingMenu(Scanner scanner, String facultyId) {
        boolean continueSession = true;

        while (continueSession) {
            System.out.println("\n=== Faculty Landing Page ===");
            System.out.println("1. Go to Active Course");
            System.out.println("2. Go to Evaluation Course");
            System.out.println("3. View Courses");
            System.out.println("4. Change Password");
            System.out.println("5. Logout");
            System.out.print("Enter choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ActiveCourseMenu.activeCourseMenu(scanner);  // Call Active Course Menu
                    break;
                case 2:
                    EvaluationCourseMenu.evaluationCourseMenu(scanner);  // Call Evaluation Course Menu
                    break;
                case 3:
                    viewCourses(scanner, facultyId);  // Integrated DB to view courses
                    break;
                case 4:
                    ChangePassword.changePassword(scanner, facultyId);  // Integrated DB to change password
                    break;
                case 5:
                    System.out.println("Logging out...");
                    continueSession = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
    }

    // Method to view courses assigned to a faculty using DB integration
    public static void viewCourses(Scanner scanner, String facultyId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();  // Get DB connection
            if (conn != null) {
                String query = "SELECT course_name FROM courses WHERE faculty_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, facultyId);  // Use facultyId from the login session

                rs = pstmt.executeQuery();

                System.out.println("\n=== Assigned Courses ===");
                while (rs.next()) {
                    System.out.println(rs.getString("course_name"));
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
        }
    }
}
