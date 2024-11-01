package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAViewCoursesPage {

    private String taId;

    public TAViewCoursesPage() {
        this.taId = HomePage.currentTAId; // Retrieve TA_id from HomePage's global variable
    }

    public void showViewCoursesPage(Scanner scanner) {
        if (taId == null) {
            System.out.println("Error: TA ID is not set. Please log in as a TA.");
            return;
        }

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
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void displayAssignedCourses() {
        String sql = "SELECT Courses.course_id, Courses.title, Courses.start_date, Courses.end_date " +
                     "FROM Courses " +
                     "JOIN TA ON Courses.course_id = TA.course_id " +
                     "WHERE TA.ta_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, taId);
            try (ResultSet resultSet = statement.executeQuery()) {

                System.out.println("Assigned Courses:");
                while (resultSet.next()) {
                    String courseId = resultSet.getString("course_id");
                    String title = resultSet.getString("title");
                    String startDate = resultSet.getString("start_date");
                    String endDate = resultSet.getString("end_date");

                    System.out.printf("Course ID: %s, Title: %s, Start Date: %s, End Date: %s%n",
                            courseId, title, startDate, endDate);
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error while retrieving courses: " + e.getMessage());
        }
    }
}
