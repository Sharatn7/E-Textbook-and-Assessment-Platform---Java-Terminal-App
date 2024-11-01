package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAModifyContentBlockPage {

    public void showModifyContentBlockPage(Scanner scanner, String sectionId) {
        boolean exit = false;

        // Use global variables for courseId and textbookId
        String courseId = TAActiveCoursePage.currentCourseId;
        String textbookId = TAActiveCoursePage.currentTextbookId;

        if (courseId == null || textbookId == null) {
            System.out.println("Error: No course or textbook selected. Please select a course from the Active Course Page.");
            return;
        }

        while (!exit) {
            System.out.print("Enter Content Block Number: ");
            String contentBlockNumber = scanner.nextLine();

            // Check if the content block already exists and was created by a TA
            String contentBlockId = getContentBlockId(contentBlockNumber, sectionId);
            if (contentBlockId == null) {
                System.out.println("Content Block not found for the specified section or not created by a TA. Please try again.");
                continue;
            }

            System.out.println("Content Block with this number already exists and will be overwritten.");

            // Delete existing content block entry if necessary
            if (!deleteExistingContentBlock(contentBlockId)) {
                System.out.println("Failed to delete existing content block. Please try again.");
                continue;
            }

            // Display menu options for modifying content block
            System.out.println("Menu:");
            System.out.println("1. Add Text");
            System.out.println("2. Add Picture");
            System.out.println("3. Add Activity");
            System.out.println("4. Go Back");
            System.out.println("5. Landing Page");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Adding Text to Content Block Number: " + contentBlockNumber);
                    TAAddTextPage addTextPage = new TAAddTextPage();
                    addTextPage.showAddTextPage(scanner, contentBlockNumber, sectionId);
                }
                case 2 -> {
                    System.out.println("Adding Picture to Content Block Number: " + contentBlockNumber);
                    TAAddPicturePage addPicturePage = new TAAddPicturePage();
                    addPicturePage.showAddPicturePage(scanner, contentBlockNumber, sectionId);
                }
                case 3 -> {
                    System.out.println("Adding Activity to Content Block Number: " + contentBlockNumber);
                    TAAddActivityPage addActivityPage = new TAAddActivityPage();
                    addActivityPage.showAddActivityPage(scanner, contentBlockNumber, sectionId);
                }
                case 4 -> {
                    System.out.println("Returning to Modify Section Page...");
                    exit = true;
                }
                case 5 -> {
                    System.out.println("Returning to Landing Page...");
                    exit = true;
                    TALandingPage landingPage = new TALandingPage();
                    landingPage.showLandingPage(scanner);
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
                    return resultSet.getString("content_block_id"); // Return the existing content_block_id if created by TA
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while retrieving content block ID: " + e.getMessage());
        }
        return null; // Return null if content block not found, not created by TA, or on error
    }

    private boolean deleteExistingContentBlock(String contentBlockId) {
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
}
