package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAModifySectionPage {

    public void showModifySectionPage(Scanner scanner) {
        boolean exit = false;

        // Use global variables for courseId and textbookId
        String courseId = TAActiveCoursePage.currentCourseId;
        String textbookId = TAActiveCoursePage.currentTextbookId;

        if (courseId == null || textbookId == null) {
            System.out.println("Error: No course or textbook selected. Please select a course from the Active Course Page.");
            return;
        }

        while (!exit) {
            // Prompt for section details in the specified order
            System.out.print("Enter Section Number: ");
            String sectionNumber = scanner.nextLine();

            System.out.print("Enter Section Title: ");
            String sectionTitle = scanner.nextLine();

            System.out.print("Enter Chapter Number: ");
            String chapterNumber = scanner.nextLine();

            System.out.print("Enter Book ID: ");
            String inputBookId = scanner.nextLine();

            // Validate the entered Book ID
            if (!inputBookId.equals(textbookId)) {
                System.out.println("Invalid Book ID. It does not match the assigned textbook. Please try again.");
                continue;
            }

            // Validate and retrieve `chapter_id`
            String chapterId = getChapterIdByNumber(chapterNumber, textbookId);
            if (chapterId == null) {
                System.out.println("Invalid Chapter Number for this textbook. Please try again.");
                continue;
            }

            // Validate and retrieve `section_id`
            String sectionId = getSectionIdByNumber(sectionNumber, chapterId);
            if (sectionId == null) {
                System.out.println("Invalid Section Number for this chapter. Please try again.");
                continue;
            }

            // Display the menu options
            System.out.println("Menu:");
            System.out.println("1. Add New Content Block");
            System.out.println("2. Modify Content Block");
            System.out.println("3. Delete Content Block");
            System.out.println("4. Hide Content Block");
            System.out.println("5. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (option) {
                case 1 -> {
                    System.out.println("Adding a New Content Block to Section ID: " + sectionId + " in Chapter ID: " + chapterId);
                    TAAddNewContentBlockPage addNewContentBlockPage = new TAAddNewContentBlockPage();
                    addNewContentBlockPage.showAddNewContentBlockPage(scanner, sectionId);
                }
                case 2 -> {
                    System.out.println("Modifying Content Block in Section ID: " + sectionId + " in Chapter ID: " + chapterId);
                    TAModifyContentBlockPage modifyContentBlockPage = new TAModifyContentBlockPage();
                    modifyContentBlockPage.showModifyContentBlockPage(scanner, sectionId);
                }
                case 3 -> {
                    System.out.println("Deleting Content Block in Section ID: " + sectionId + " in Chapter ID: " + chapterId);
                    TADeleteContentBlockPage deleteContentBlockPage = new TADeleteContentBlockPage();
                    deleteContentBlockPage.showDeleteContentBlockPage(scanner, sectionId);
                }
                case 4 -> {
                    System.out.println("Hiding Content Block in Section ID: " + sectionId + " in Chapter ID: " + chapterId);
                    TAHideContentBlockPage hideContentBlockPage = new TAHideContentBlockPage();
                    hideContentBlockPage.showHideContentBlockPage(scanner, sectionId);
                }
                case 5 -> {
                    System.out.println("Returning to Previous Page...");
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

    private String getSectionIdByNumber(String sectionNumber, String chapterId) {
        String sql = "SELECT section_id FROM Sections WHERE section_number = ? AND chapter_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, sectionNumber);
            statement.setString(2, chapterId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("section_id"); // Retrieve and return the section_id
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while retrieving section ID: " + e.getMessage());
        }
        return null; // Return null if section not found or on error
    }
}
