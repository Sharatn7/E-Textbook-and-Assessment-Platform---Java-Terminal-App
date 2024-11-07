package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAModifyChapterPage {

    public void showModifyChapterPage(Scanner scanner) {
        boolean exit = false;

        // Use global courseId and textbookId
        String courseId = TAActiveCoursePage.currentCourseId;
        String textbookId = TAActiveCoursePage.currentTextbookId;

        if (courseId == null || textbookId == null) {
            System.out.println("Error: No course or textbook selected. Please select a course from the Active Course Page.");
            return;
        }

        while (!exit) {
            System.out.print("Enter Unique Chapter Number: ");
            String chapterNumber = scanner.nextLine();

            // Validate chapter number and retrieve chapter ID
            String chapterId = getChapterIdByNumber(chapterNumber, textbookId);
            if (chapterId == null) {
                System.out.println("Chapter not found for this course. Please try again.");
                continue;
            }

            System.out.println("Menu:");
            System.out.println("1. Add New Section");
            System.out.println("2. Modify Section");
            System.out.println("3. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Adding a New Section to Chapter ID: " + chapterId);
                    TAAddNewSectionPage addNewSectionPage = new TAAddNewSectionPage();
                    addNewSectionPage.showAddNewSectionPage(scanner, chapterId);
                }
                case 2 -> {
                    System.out.println("Modifying Section for Chapter ID: " + chapterId);
                    TAModifySectionPage modifySectionPage = new TAModifySectionPage();
                    modifySectionPage.showModifySectionPage(scanner);
                }
                case 3 -> {
                    System.out.println("Returning to Active Course Page...");
                    exit = true;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private String getChapterIdByNumber(String chapterNumber, String textbookId) {
        String sql = "SELECT chapter_id FROM Chapters WHERE chapter_number = ? AND textbook_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, chapterNumber);
            statement.setString(2, textbookId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("chapter_id"); // Retrieve and return the chapter_id
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while retrieving chapter ID: " + e.getMessage());
        }
        return null; // Return null if chapter not found or on error
    }
}
