package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAHideContentBlockPage {

    public void showHideContentBlockPage(Scanner scanner, String sectionId) {
        boolean exit = false;

        while (!exit) {
            System.out.print("Enter Content Block Number: ");
            String contentBlockNumber = scanner.nextLine();

            // Retrieve the unique `contentBlockId` if it exists and was created by a TA
            String contentBlockId = getContentBlockId(contentBlockNumber, sectionId);
            if (contentBlockId == null) {
                System.out.println("Content Block not found or not created by a TA. Please try again.");
                continue;
            }

            System.out.println("Menu:");
            System.out.println("1. Hide");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    if (hideContentBlock(contentBlockId)) {
                        System.out.println("Content Block ID: " + contentBlockId + " successfully hidden.");
                    } else {
                        System.out.println("Failed to hide Content Block ID: " + contentBlockId);
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

    private boolean hideContentBlock(String contentBlockId) {
        String sql = "UPDATE ContentBlocks SET hidden = '1' WHERE content_block_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, contentBlockId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the update was successful
        } catch (SQLException e) {
            System.out.println("Database error while hiding content block: " + e.getMessage());
        }
        return false; // Return false if update failed
    }
}
