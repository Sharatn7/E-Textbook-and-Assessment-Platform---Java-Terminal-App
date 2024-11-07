package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAAddActivityPage {

    public void showAddActivityPage(Scanner scanner, String contentBlockNumber, String sectionId) {
        boolean exit = false;

        // Use global variables for courseId and textbookId
        String courseId = TAActiveCoursePage.currentCourseId;
        String textbookId = TAActiveCoursePage.currentTextbookId;

        if (courseId == null || textbookId == null) {
            System.out.println("Error: No course or textbook selected. Please select a course from the Active Course Page.");
            return;
        }

        while (!exit) {
            System.out.print("Enter Unique Activity Number: ");
            String activityNumber = scanner.nextLine();

            System.out.println("Menu:");
            System.out.println("1. Add Question");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    // First, ensure the content block exists and retrieve its ID
                    String contentBlockId = getOrCreateContentBlock(contentBlockNumber, activityNumber, sectionId);

                    if (contentBlockId != null) {
                        // Add activity if it does not already exist
                        String activityId = addActivityIfNotExist(activityNumber, contentBlockId);

                        if (activityId != null) {
                            System.out.println("Adding a Question to Activity ID: " + activityId);
                            TAAddQuestionPage addQuestionPage = new TAAddQuestionPage();
                            addQuestionPage.showAddQuestionPage(scanner, activityId);
                        } else {
                            System.out.println("Failed to save Activity ID: " + activityId + ". Please try again.");
                        }
                    } else {
                        System.out.println("Failed to retrieve or create Content Block ID. Please try again.");
                    }
                    break;
                }
                case 2 -> {
                    System.out.println("Returning to Modify Chapter Page...");
                    exit = true;
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private String getOrCreateContentBlock(String contentBlockNumber, String activityNumber, String sectionId) {
        // Check if the content block already exists and retrieve its ID
        String contentBlockId = retrieveContentBlockId(contentBlockNumber, sectionId);

        // If it doesn't exist, create a new content block entry
        if (contentBlockId == null) {
            contentBlockId = insertNewContentBlock(contentBlockNumber, activityNumber, sectionId);
        }

        return contentBlockId;
    }

    private String retrieveContentBlockId(String contentBlockNumber, String sectionId) {
        String sql = "SELECT content_block_id FROM ContentBlocks WHERE section_id = ? AND content_block_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, sectionId);
            statement.setString(2, contentBlockNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("content_block_id"); // Return the existing content_block_id
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while retrieving content block ID: " + e.getMessage());
        }
        return null; // Return null if no matching content block was found
    }

    private String insertNewContentBlock(String contentBlockNumber, String activityNumber, String sectionId) {
        String sqlInsert = "INSERT INTO ContentBlocks (section_id, content_block_number, content_type, content_data, createdby) VALUES (?, ?, 'activity', ?, 'TA')";
        String sqlRetrieve = "SELECT content_block_id FROM ContentBlocks WHERE section_id = ? AND content_block_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(sqlInsert);
             PreparedStatement retrieveStatement = conn.prepareStatement(sqlRetrieve)) {

            // Insert the new content block
            insertStatement.setString(1, sectionId);
            insertStatement.setString(2, contentBlockNumber);
            insertStatement.setString(3, activityNumber);
            int rowsAffected = insertStatement.executeUpdate();

            // If insertion was successful, retrieve the new content_block_id
            if (rowsAffected > 0) {
                retrieveStatement.setString(1, sectionId);
                retrieveStatement.setString(2, contentBlockNumber);

                try (ResultSet resultSet = retrieveStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("content_block_id"); // Return the new content_block_id
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while inserting content block: " + e.getMessage());
        }
        return null; // Return null if insertion or retrieval failed
    }

    private String addActivityIfNotExist(String activityNumber, String contentBlockId) {
        // Check if the activity already exists
        if (checkActivityExists(activityNumber, contentBlockId)) {
            System.out.println("Activity Number already exists for this content block. Please enter a unique activity number.");
            return null;
        }

        // Determine the visibility setting based on the global variable
        String visibility = TAAddNewContentBlockPage.isContentBlockVisible ? "0" : "1";

        String sqlInsert = "INSERT INTO Activity (content_block_id, activity_number, createdby, hidden) VALUES (?, ?, 'TA', ?)";
        String sqlRetrieve = "SELECT activity_id FROM Activity WHERE content_block_id = ? AND activity_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(sqlInsert);
             PreparedStatement retrieveStatement = conn.prepareStatement(sqlRetrieve)) {

            // Insert the new activity with the visibility setting
            insertStatement.setString(1, contentBlockId);
            insertStatement.setString(2, activityNumber);
            insertStatement.setString(3, visibility);
            int rowsAffected = insertStatement.executeUpdate();

            // If insertion was successful, retrieve the activity_id
            if (rowsAffected > 0) {
                retrieveStatement.setString(1, contentBlockId);
                retrieveStatement.setString(2, activityNumber);

                try (ResultSet resultSet = retrieveStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("activity_id"); // Return the activity_id from the database
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while adding activity: " + e.getMessage());
        }
        return null; // Return null if insertion or retrieval failed
    }

    private boolean checkActivityExists(String activityNumber, String contentBlockId) {
        String sql = "SELECT activity_id FROM Activity WHERE content_block_id = ? AND activity_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, contentBlockId);
            statement.setString(2, activityNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Return true if a matching activity is found
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking activity number: " + e.getMessage());
        }
        return false; // Return false if no matching activity was found or on error
    }
}
