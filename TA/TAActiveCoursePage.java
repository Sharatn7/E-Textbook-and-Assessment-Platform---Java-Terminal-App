package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAActiveCoursePage {

    private String taId;
    public static String currentCourseId;     // Global variable to store the current Course ID
    public static String currentTextbookId;   // Global variable to store the associated Textbook ID

    public TAActiveCoursePage() {
        this.taId = HomePage.currentTAId; // Retrieve TA_id from HomePage's global variable
    }

    public void showActiveCoursePage(Scanner scanner) {
        if (taId == null) {
            System.out.println("Error: TA ID is not set. Please log in as a TA.");
            return;
        }

        System.out.println("You are now in the Active Course Page");

        boolean exit = false;
        while (!exit) {

            System.out.print("Enter Course ID: ");
            String courseId = scanner.nextLine();

            if (!isCourseAssignedToTA(courseId)) {
                System.out.println("Invalid Course ID or you are not assigned to this course. Please try again.");
                continue;
            }

            // Set the global variable for the current Course ID
            currentCourseId = courseId;

            // Fetch and set the global variable for the associated Textbook ID
            currentTextbookId = fetchTextbookId(courseId);

            System.out.println("Menu:");
            System.out.println("1. View Students");
            System.out.println("2. Add New Chapter");
            System.out.println("3. Modify Chapters");
            System.out.println("4. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Viewing Students for Course ID: " + courseId);
                    TAViewStudentsPage viewStudentsPage = new TAViewStudentsPage();
                    viewStudentsPage.showViewStudentsPage(scanner);
                }
                case 2 -> {
                    System.out.println("Adding a New Chapter to Course ID: " + courseId);
                    TAAddNewChapterPage addNewChapterPage = new TAAddNewChapterPage();
                    addNewChapterPage.showAddNewChapterPage(scanner);
                }
                case 3 -> {
                    System.out.println("Modifying Chapters in Course ID: " + courseId);
                    TAModifyChapterPage modifyChapterPage = new TAModifyChapterPage();
                    modifyChapterPage.showModifyChapterPage(scanner);
                }
                case 4 -> {
                    System.out.println("Returning to Landing Page...");
                    exit = true;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private boolean isCourseAssignedToTA(String courseId) {
        String sql = "SELECT course_id FROM TA WHERE ta_id = ? AND course_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
             
            statement.setString(1, taId);
            statement.setString(2, courseId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Returns true if course is assigned to TA
            }
        } catch (SQLException e) {
            System.out.println("Database error while verifying course assignment: " + e.getMessage());
        }
        return false;
    }

    private String fetchTextbookId(String courseId) {
        String sql = "SELECT textbook_id FROM Courses WHERE course_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
             
            statement.setString(1, courseId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("textbook_id"); // Return textbook ID associated with the course
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while fetching textbook ID: " + e.getMessage());
        }
        return null;
    }
}
