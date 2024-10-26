import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddText {

    public static void addText(Scanner scanner, String blockId) {
        System.out.print("Enter Text: ");
        String text = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                // Update the content field in the Blocks table for the given block_id
                String query = "UPDATE Blocks SET content = ? WHERE block_id = ? AND type = 'text'";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, text);         // Set the new text content
                pstmt.setString(2, blockId);      // Specify the block ID to update

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Text added to Content Block '" + blockId + "' successfully!");
                } else {
                    System.out.println("Failed to add text. Either the block doesn't exist, or it's not a text block.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        boolean inAddText = true;
        while (inAddText) {
            System.out.println("\n1. ADD MORE TEXT");
            System.out.println("2. Go Back");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Optionally allow the user to add more text
                    System.out.print("Enter additional Text: ");
                    text = scanner.nextLine();

                    if (!text.isEmpty()) {
                        addText(scanner, blockId);  // Call the method recursively to add more text
                    } else {
                        System.out.println("No additional text was entered.");
                    }
                    break;
                case 2:
                    System.out.println("Returning to previous menu...");
                    inAddText = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to add more text or 2 to go back.");
            }
        }
    }
}
