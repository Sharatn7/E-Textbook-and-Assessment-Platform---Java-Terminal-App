package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TADeleteContentBlockPage {

    public void showDeleteContentBlockPage(Scanner scanner, String sectionId) {
        boolean exit = false;

        // Use the global textbook ID for validation
        
        
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        
        String textbookId = fetchTextbookId(courseId);
        if (textbookId == null) {
            System.out.println("Error: No textbook selected. Please select a course from the Active Course Page.");
            return;
        }
        
        System.out.print("Enter Course Number: ");
        String chapterNumber = scanner.nextLine();
        String chapterId = getChapterIdByNumber(chapterNumber, textbookId);
        
        System.out.print("Enter Section Number: ");
        String sectionNumber = scanner.nextLine();
        String sectionId1 = getSectionIdByNumber(sectionNumber, chapterId);

        while (!exit) {
            System.out.print("Enter Content Block Number: ");
            String contentBlockNumber = scanner.nextLine();

            // Retrieve the unique `contentBlockId` if it exists and was created by a TA
            String contentBlockId = getContentBlockId(contentBlockNumber, sectionId1);
            if (contentBlockId == null) {
                System.out.println("Content Block not found or not created by a TA. Please try again.");
                continue;
            }

            System.out.println("Menu:");
            System.out.println("1. Delete");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    if (deleteContentBlock(contentBlockId)) {
                        System.out.println("Content Block ID: " + contentBlockId + " successfully deleted from Section " + sectionId);
                    } else {
                        System.out.println("Failed to delete Content Block ID: " + contentBlockId);
                    }
                    exit = true;
                }
                case 2 -> {
                    System.out.println("Returning to Modify Section Page...");
                    exit = true;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private String getContentBlockId(String contentBlockNumber, String sectionId) {
        String sql = "SELECT content_block_id FROM ContentBlocks WHERE content_block_number = ? AND section_id = ? AND createdby = 'TA'";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, contentBlockNumber);
            statement.setString(2, sectionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("content_block_id"); // Return the content_block_id if found and created by TA
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while retrieving content block ID: " + e.getMessage());
        }
        return null; // Return null if content block not found, not created by TA, or on error
    }

    private boolean deleteContentBlock(String contentBlockId) {
        String sql = "DELETE FROM ContentBlocks WHERE content_block_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, contentBlockId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if deletion was successful
        } catch (SQLException e) {
            System.out.println("Database error while deleting content block: " + e.getMessage());
        }
        return false; // Return false if deletion failed
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
