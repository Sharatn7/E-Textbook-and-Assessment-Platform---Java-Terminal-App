package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAAddNewChapterPage {

    public void showAddNewChapterPage(Scanner scanner) {
        boolean exit = false;

        // Check if the global course ID and textbook ID are set
        String courseId = TAActiveCoursePage.currentCourseId;
        String textbookId = TAActiveCoursePage.currentTextbookId;

        if (courseId == null || textbookId == null) {
            System.out.println("Error: No course or textbook selected. Please select a course from the Active Course Page.");
            return;
        }

        while (!exit) {
            System.out.print("Enter Unique Chapter Number: ");
            String chapterNumber = scanner.nextLine();

            // Check if the chapter number already exists for the given textbook
            if (checkChapterNumberExists(chapterNumber, textbookId)) {
                System.out.println("Chapter Number already exists for this textbook. Please enter a unique chapter number.");
                continue;
            }

            System.out.print("Enter Chapter Title: ");
            String chapterTitle = scanner.nextLine();

            System.out.println("Menu:");
            System.out.println("1. Add New Section");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    String chapterId = addNewChapterToDatabase(chapterNumber, chapterTitle, textbookId);
                    if (chapterId != null) {
                        System.out.println("Successfully added new chapter with ID: " + chapterId + " and Title: " + chapterTitle);
                        System.out.println("Redirecting to Add New Section...");
                        TAAddNewSectionPage addNewSectionPage = new TAAddNewSectionPage();
                        addNewSectionPage.showAddNewSectionPage(scanner, chapterId);
                    } else {
                        System.out.println("Failed to add new chapter. Please try again.");
                    }
                }
                case 2 -> {
                    System.out.println("Returning to Active Course Page...");
                    exit = true;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private boolean checkChapterNumberExists(String chapterNumber, String textbookId) {
        String sql = "SELECT chapter_id FROM Chapters WHERE textbook_id = ? AND chapter_number = ?";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
             
            statement.setString(1, textbookId);
            statement.setString(2, chapterNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Return true if a matching chapter is found
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking chapter number: " + e.getMessage());
        }
        return false; // Return false if no matching chapter was found or on error
    }

    private String addNewChapterToDatabase(String chapterNumber, String chapterTitle, String textbookId) {
        String sqlInsert = "INSERT INTO Chapters (textbook_id, chapter_number, title, createdby) VALUES (?, ?, ?, 'TA')";
        String sqlRetrieve = "SELECT chapter_id FROM Chapters WHERE textbook_id = ? AND chapter_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(sqlInsert);
             PreparedStatement retrieveStatement = conn.prepareStatement(sqlRetrieve)) {

            // Insert the new chapter
            insertStatement.setString(1, textbookId);
            insertStatement.setString(2, chapterNumber);
            insertStatement.setString(3, chapterTitle);
            int rowsAffected = insertStatement.executeUpdate();

            // If insertion was successful, retrieve the chapter_id
            if (rowsAffected > 0) {
                retrieveStatement.setString(1, textbookId);
                retrieveStatement.setString(2, chapterNumber);

                try (ResultSet resultSet = retrieveStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("chapter_id"); // Return the chapter_id from the database
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while adding chapter: " + e.getMessage());
        }
        return null; // Return null if insertion or retrieval failed
    }
}
