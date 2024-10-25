import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class FacultyLandingPage {

    public static void facultyLandingMenu(Scanner scanner, String facultyId) {
        boolean continueSession = true;
        while (continueSession) {
            // Display menu
            System.out.println("\n=== Faculty Landing Page ===");
            System.out.println("1. View Courses");
            System.out.println("2. Change Password");
            System.out.println("3. Logout");
            System.out.print("Enter choice (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Database query to view courses
                    try (Connection conn = DBConnect.getConnection();
                         Statement stmt = conn.createStatement()) {
                         
                        String query = "SELECT * FROM courses WHERE faculty_id = '" + facultyId + "'";
                        ResultSet rs = stmt.executeQuery(query);

                        System.out.println("\n=== Assigned Courses ===");
                        while (rs.next()) {
                            String courseName = rs.getString("course_name");
                            System.out.println(courseName);
                        }
                    } catch (SQLException e) {
                        System.out.println("Database operation failed.");
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    // Call changePassword method and pass the facultyId
                    ChangePassword.changePassword(scanner, facultyId);  
                    break;
                case 3:
                    System.out.println("Logging out... Returning to Home Page.");
                    continueSession = false;  // Break the landing page loop
                    break;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
    }
}
