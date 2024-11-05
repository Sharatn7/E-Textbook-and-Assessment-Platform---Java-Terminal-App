package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAAddNewSectionPage {

    public void showAddNewSectionPage(Scanner scanner, String chapterId) {
        boolean exit = false;

        // Use global variables for courseId and textbookId
        String courseId = TAActiveCoursePage.currentCourseId;
        String textbookId = TAActiveCoursePage.currentTextbookId;

        if (courseId == null || textbookId == null) {
            System.out.println("Error: No course or textbook selected. Please select a course from the Active Course Page.");
            return;
        }

        while (!exit) {
            System.out.print("Enter Unique Section Number: ");
            String sectionNumber = scanner.nextLine();

            // Check if the section number already exists for the given chapter
            if (checkSectionNumberExists(sectionNumber, chapterId)) {
                System.out.println("Section Number already exists for this chapter. Please enter a unique section number.");
                continue;
            }

            System.out.print("Enter Section Title: ");
            String sectionTitle = scanner.nextLine();

            System.out.println("Menu:");
            System.out.println("1. Add New Content Block");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    String sectionId = addNewSectionToDatabase(sectionNumber, sectionTitle, chapterId);
                    if (sectionId != null) {
                        System.out.println("Successfully added new section with ID: " + sectionId + " and Title: " + sectionTitle);
                        System.out.println("Redirecting to Add New Content Block...");
                        TAAddNewContentBlockPage addNewContentBlockPage = new TAAddNewContentBlockPage();
                        addNewContentBlockPage.showAddNewContentBlockPage(scanner, sectionId);
                    } else {
                        System.out.println("Failed to add new section. Please try again.");
                    }
                }
                case 2 -> {
                    System.out.println("Returning to Previous Page...");
                    exit = true;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private boolean checkSectionNumberExists(String sectionNumber, String chapterId) {
        String sql = "SELECT section_id FROM Sections WHERE chapter_id = ? AND section_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, chapterId);
            statement.setString(2, sectionNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Return true if a matching section is found
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking section number: " + e.getMessage());
        }
        return false; // Return false if no matching section was found or on error
    }

    private String addNewSectionToDatabase(String sectionNumber, String sectionTitle, String chapterId) {
        String sqlInsert = "INSERT INTO Sections (chapter_id, section_number, title, createdby) VALUES (?, ?, ?, 'TA')";
        String sqlRetrieve = "SELECT section_id FROM Sections WHERE chapter_id = ? AND section_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(sqlInsert);
             PreparedStatement retrieveStatement = conn.prepareStatement(sqlRetrieve)) {

            // Insert the new section
            insertStatement.setString(1, chapterId);
            insertStatement.setString(2, sectionNumber);
            insertStatement.setString(3, sectionTitle);
            int rowsAffected = insertStatement.executeUpdate();

            // If insertion was successful, retrieve the section_id
            if (rowsAffected > 0) {
                retrieveStatement.setString(1, chapterId);
                retrieveStatement.setString(2, sectionNumber);

                try (ResultSet resultSet = retrieveStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("section_id"); // Return the section_id from the database
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while adding section: " + e.getMessage());
        }
        return null; // Return null if insertion or retrieval failed
    }
}
