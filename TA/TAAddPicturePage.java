package TA;
import java.sql.Connection;
import homepage.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAAddPicturePage {

    public void showAddPicturePage(Scanner scanner, String contentBlockNumber, String sectionId) {
        boolean exit = false;

        // Use global variables for courseId and textbookId
        String courseId = TAActiveCoursePage.currentCourseId;
        String textbookId = TAActiveCoursePage.currentTextbookId;

        if (courseId == null || textbookId == null) {
            System.out.println("Error: No course or textbook selected. Please select a course from the Active Course Page.");
            return;
        }

        while (!exit) {
            System.out.print("Enter Picture Details for Content Block Number: " + contentBlockNumber + ": ");
            String pictureDetails = scanner.nextLine();

            System.out.println("Menu:");
            System.out.println("1. Add");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    String contentBlockId = addPictureIfNotExist(contentBlockNumber, sectionId, pictureDetails);
                    if (contentBlockId != null) {
                        System.out.println("Picture details saved successfully to Content Block ID: " + contentBlockId);
                    } else {
                        System.out.println("Failed to save picture details. Content Block ID may already exist. Please try again.");
                    }
                    exit = true;
                }
                case 2 -> {
                    System.out.println("Returning to Add New Content Block Page...");
                    exit = true;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private String addPictureIfNotExist(String contentBlockNumber, String sectionId, String pictureDetails) {
        // Check if the content block already exists
        if (checkContentBlockExists(contentBlockNumber, sectionId)) {
            System.out.println("Content Block Number already exists for this section. Please enter a unique content block number.");
            return null;
        }

        String sqlInsert = "INSERT INTO ContentBlocks (section_id, content_block_number, content_type, content_data, createdby) VALUES (?, ?, 'picture', ?, 'TA')";
        String sqlRetrieve = "SELECT content_block_id FROM ContentBlocks WHERE section_id = ? AND content_block_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(sqlInsert);
             PreparedStatement retrieveStatement = conn.prepareStatement(sqlRetrieve)) {

            // Insert the new picture content block
            insertStatement.setString(1, sectionId);
            insertStatement.setString(2, contentBlockNumber);
            insertStatement.setString(3, pictureDetails);
            int rowsAffected = insertStatement.executeUpdate();

            // If insertion was successful, retrieve the content_block_id
            if (rowsAffected > 0) {
                retrieveStatement.setString(1, sectionId);
                retrieveStatement.setString(2, contentBlockNumber);

                try (ResultSet resultSet = retrieveStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("content_block_id"); // Return the content_block_id from the database
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while adding picture details: " + e.getMessage());
        }
        return null; // Return null if insertion or retrieval failed
    }

    private boolean checkContentBlockExists(String contentBlockNumber, String sectionId) {
        String sql = "SELECT content_block_id FROM ContentBlocks WHERE section_id = ? AND content_block_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, sectionId);
            statement.setString(2, contentBlockNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Return true if a matching content block is found
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking content block number: " + e.getMessage());
        }
        return false; // Return false if no matching content block was found or on error
    }
}
