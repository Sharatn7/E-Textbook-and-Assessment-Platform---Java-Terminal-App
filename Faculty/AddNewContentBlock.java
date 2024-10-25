import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddNewContentBlock {

    public static void addNewContentBlock(Scanner scanner) {
        System.out.print("Enter Content Block ID: ");
        String contentBlockId = scanner.nextLine();

        System.out.print("Enter Content Block Type (Text/Picture/Activity): ");
        String contentType = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "INSERT INTO ContentBlocks (content_block_id, content_type) VALUES (?, ?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, contentBlockId);
                pstmt.setString(2, contentType);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Content Block added successfully.");
                } else {
                    System.out.println("Failed to add content block.");
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

        System.out.println("\n1. Add Text");
        System.out.println("2. Add Picture");
        System.out.println("3. Add Activity");
        System.out.println("4. Go Back");
        System.out.print("Enter choice (1-4): ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                AddText.addText(scanner, contentBlockId);  // Add text functionality
                break;
            case 2:
                AddPicture.addPicture(scanner, contentBlockId);  // Add picture functionality
                break;
            case 3:
                AddActivity.addActivity(scanner, contentBlockId);  // Add activity functionality
                break;
            case 4:
                System.out.println("Returning to previous menu...");
                break;
            default:
                System.out.println("Invalid choice! Please select a valid option.");
        }
    }
}
