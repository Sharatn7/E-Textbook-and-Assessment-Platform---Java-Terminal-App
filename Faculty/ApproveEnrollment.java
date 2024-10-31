import java.sql.*;
import java.util.Scanner;

public class ApproveEnrollment {

    public static void displayMenu(Scanner scanner, String courseId) {
        System.out.println("Enter the following details:");
        
        // Step A: Ask for Student ID
        System.out.print("A: Student ID: ");
        String studentId = scanner.nextLine();
        
        // Display options
        System.out.println("Menu:");
        System.out.println("1. Save");
        System.out.println("2. Cancel");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                // Attempt to approve enrollment
                boolean success = approveEnrollment(studentId, courseId);
                if (success) {
                    System.out.println("Success: Enrollment approved.");
                } else {
                    System.out.println("Fail: Could not approve enrollment.");
                }
                // Go back to previous page or menu
               return;

            case 2:
                // Discard input and go back to previous page
                System.out.println("Input discarded. Returning to previous page.");
                return;

            default:
                System.out.println("Invalid option. Please try again.");
                displayMenu(scanner, courseId); // Redisplay menu for correct input
                break;
        }
    }

    private static boolean approveEnrollment(String studentId, String courseId) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                conn.setAutoCommit(false); // Start transaction

                // Fetch current enrollment and capacity
                String fetchCourseQuery = "SELECT enrollmentCount, capacity FROM activeCourse WHERE course_id = ?";
                try (PreparedStatement fetchStmt = conn.prepareStatement(fetchCourseQuery)) {
                    fetchStmt.setString(1, courseId);
                    ResultSet rs = fetchStmt.executeQuery();

                    if (rs.next()) {
                        int enrollmentCount = rs.getInt("enrollmentCount");
                        int capacity = rs.getInt("capacity");

                        if (enrollmentCount < capacity) {
                            // Process enrollment approval steps here, such as updating the role, inserting records, and updating enrollment count
                            conn.commit(); // Commit transaction on success
                            return true; // Indicate success
                        } else {
                            // Notify capacity reached or handle as per logic
                            conn.rollback(); // Rollback transaction on failure
                            return false; // Indicate failure
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

