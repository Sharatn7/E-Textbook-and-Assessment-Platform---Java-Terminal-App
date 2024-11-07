package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAViewStudentsPage {

    public void showViewStudentsPage(Scanner scanner) {
        boolean exit = false;

        while (!exit) {
            // Use the global variable for courseId from TAActiveCoursePage
            String courseId = TAActiveCoursePage.currentCourseId;
            
            // Check if the global variable is set
            if (courseId == null) {
                System.out.println("Error: No course selected. Please select a course from the Active Course Page.");
                return;
            }

            displayEnrolledStudents();

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

    private void displayEnrolledStudents() {
        String sql = "SELECT Users.user_id, Users.first_name, Users.last_name, Users.email " +
                     "FROM Student " +
                     "JOIN Users ON Student.student_id = Users.user_id " +
                     "WHERE Student.course_id = ? AND Users.role = 'Student'";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
             
            // Use the global variable from TAActiveCoursePage
            statement.setString(1, TAActiveCoursePage.currentCourseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("Enrolled Students:");
                while (resultSet.next()) {
                    String studentId = resultSet.getString("user_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");

                    System.out.printf("Student ID: %s, Name: %s %s, Email: %s%n", 
                                      studentId, firstName, lastName, email);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while retrieving students: " + e.getMessage());
        }
    }
}
