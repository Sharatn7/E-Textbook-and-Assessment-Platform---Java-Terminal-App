import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAViewCoursesPage {

    private String taId;

    public TAViewCoursesPage(String taId) {
        this.taId = taId;
    }

    public void showViewCoursesPage(Scanner scanner) {
        boolean goBack = false;

        while (!goBack) {

            displayAssignedCourses();

            System.out.println("Menu:");
            System.out.println("1. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                System.out.println("Returning to TA Landing Page...");
                goBack = true;
                break;
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void displayAssignedCourses() {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
            	
                String sql = "SELECT c.course_id, c.title FROM Course c JOIN TA t ON c.course_id = t.course_id WHERE t.ta_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, taId);

                ResultSet rs = pstmt.executeQuery();

                System.out.println("Assigned Courses for TA ID: " + taId);
                while (rs.next()) {
                    String courseId = rs.getString("course_id");
                    String courseTitle = rs.getString("title");
                    System.out.println("Course ID: " + courseId + " | Title: " + courseTitle);
                }

            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
