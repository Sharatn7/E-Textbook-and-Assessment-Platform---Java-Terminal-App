import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddPicture {

    public static void addPicture(Scanner scanner, String blockId) {
        System.out.print("Enter Picture URL: ");
        String url = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                // Update the content field in the Blocks table for the given block_id where type is 'picture'
                String query = "UPDATE Blocks SET content = ? WHERE block_id = ? AND type = 'picture'";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, url);         // Set the picture URL as the content
                pstmt.setString(2, blockId);      // Specify the block ID to update

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Picture added to Content Block '" + blockId + "' successfully!");
                } else {
                    System.out.println("Failed to add picture. Either the block doesn't exist, or it's not a picture block.");
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

        boolean inAddPicture = true;
        while (inAddPicture) {
            System.out.println("\n1. ADD MORE PICTURES");
            System.out.println("2. Go Back");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Optionally allow the user to add more pictures
                    System.out.print("Enter another Picture URL: ");
                    url = scanner.nextLine();

                    if (!url.isEmpty()) {
                        addPicture(scanner, blockId);  // Call the method recursively to add more pictures
                    } else {
                        System.out.println("No additional picture URL was entered.");
                    }
                    break;
                case 2:
                    System.out.println("Returning to previous menu...");
                    inAddPicture = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to add more pictures or 2 to go back.");
            }
        }
    }
}
