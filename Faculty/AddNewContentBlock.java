import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddNewContentBlock {

    public static void addNewContentBlock(Scanner scanner) {
        System.out.print("Enter Textbook ID: ");
        int textbookId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter Chapter ID: ");
        String chapterId = scanner.nextLine();

        System.out.print("Enter Section Number: ");
        String sectionNumber = scanner.nextLine();

        System.out.print("Enter Block ID: ");
        String blockId = scanner.nextLine();

        System.out.print("Enter Content Block Type (Text/Picture/Activity): ");
        String contentType = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection(); // Get connection from DBConnect

            if (conn != null) {
                // Insert basic block info (without content, as content will be handled in specific methods)
                String query = "INSERT INTO Blocks (textbook_id, chapter_id, `section number`, block_id, type) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, textbookId);          // Set textbook_id
                pstmt.setString(2, chapterId);        // Set chapter_id
                pstmt.setString(3, sectionNumber);    // Set section_number
                pstmt.setString(4, blockId);          // Set block_id
                pstmt.setString(5, contentType);      // Set type (Text/Picture/Activity)

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Content Block metadata added successfully.");
                } else {
                    System.out.println("Failed to add content block metadata.");
                    return;
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

        // Automatically call the appropriate method based on the contentType
        if (contentType.equalsIgnoreCase("Text")) {
            AddText.addText(scanner, blockId);  // Call AddText method
        } else if (contentType.equalsIgnoreCase("Picture")) {
            AddPicture.addPicture(scanner, blockId);  // Call AddPicture method
        } else if (contentType.equalsIgnoreCase("Activity")) {
            AddActivity.addActivity(scanner, blockId);  // Call AddActivity method
        } else {
            System.out.println("Invalid content type! Please enter 'Text', 'Picture', or 'Activity'.");
        }
    }
}
