package Faculty;
import homepage.*;
import java.sql.*;
import java.util.Scanner;

public class ViewStudents {

    public static void displayMenu(Scanner scanner, String courseId) {
        System.out.println("List of Students enrolled in Course ID: " + courseId);
        
        // Display the list of students
        viewStudents(courseId);

        // Display menu options
        System.out.println("\nMenu:");
        System.out.println("1. Go back");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            System.out.println("Returning to Active Course Menu...");
            return;
        }else {
            System.out.println("Invalid option. Please try again.");
            displayMenu(scanner, courseId); // Redisplay menu for correct input
        }
    }

    private static void viewStudents(String courseId) {
        String query = "SELECT S.student_id, U.first_name, U.last_name " +
                       "FROM Student S JOIN Users U ON S.student_id = U.user_id " +
                       "WHERE S.course_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            System.out.printf("%-10s %-15s %-15s%n", "Student ID", "First Name", "Last Name");
            System.out.println("----------------------------------------");

            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                System.out.printf("%-10s %-15s %-15s%n", studentId, firstName, lastName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving students list.");
        }
    }
}


