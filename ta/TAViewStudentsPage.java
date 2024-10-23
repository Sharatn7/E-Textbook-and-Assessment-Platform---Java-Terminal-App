import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAViewStudentsPage {

    public void showViewStudentsPage(Scanner scanner, String courseId) {
        boolean exit = false;

        while (!exit) {

            displayEnrolledStudents(courseId);

            System.out.println("Menu:");
            System.out.println("1. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                System.out.println("Returning to Active Course Page...");
                exit = true;
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }
    }


    private void displayEnrolledStudents(String courseId) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {

                String sql = "SELECT s.first_name, s.last_name, s.status " +
                             "FROM Student s " +
                             "WHERE s.course_id = ? AND s.status = 'current'";
                
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, courseId);

                ResultSet rs = pstmt.executeQuery();

                System.out.println("Students currently enrolled in Course ID: " + courseId);
                while (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    System.out.println("- " + firstName + " " + lastName + " (Status: current)");
                }

            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
