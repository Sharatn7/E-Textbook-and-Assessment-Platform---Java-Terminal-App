import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class HideContentBlock {

    public static void hideContentBlock(Scanner scanner, String contentBlockId) {
        boolean inHideContentBlock = true;
        while (inHideContentBlock) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Hide the content block in the database
                    hideContentBlockInDB(contentBlockId);
                    inHideContentBlock = false;
                    break;
                case 2:
                    System.out.println("Cancelling hide operation and returning to Modify Content Block menu...");
                    inHideContentBlock = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Method to hide content block in the database
    public static void hideContentBlockInDB(String contentBlockId) {
        String query = "UPDATE content_blocks SET is_hidden = true WHERE content_block_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, contentBlockId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Content Block '" + contentBlockId + "' hidden successfully.");
            } else {
                System.out.println("Failed to hide content block.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to update content block status in the database.");
            e.printStackTrace();
        }
    }
}
