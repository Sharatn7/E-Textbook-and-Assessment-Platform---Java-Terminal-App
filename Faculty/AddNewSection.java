import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddNewSection {

    public static void addNewSection(Scanner scanner) {
        System.out.print("Enter Textbook ID: ");
        int textbookId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter Chapter ID: ");
        String chapterId = scanner.nextLine();

        System.out.print("Enter Section ID: ");
        String sectionId = scanner.nextLine();

        System.out.print("Enter Section Title: ");
        String sectionTitle = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection(); // Get connection from DBConnect

            if (conn != null) {
                // Query to insert into the Sections table
                String query = "INSERT INTO Sections (textbook_id, chapter_id, Section_id, title) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, textbookId);      // Set textbook_id
                pstmt.setString(2, chapterId);    // Set chapter_id
                pstmt.setString(3, sectionId);    // Set Section_id
                pstmt.setString(4, sectionTitle); // Set title

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Section '" + sectionTitle + "' added successfully.");
                } else {
                    System.out.println("Failed to add section.");
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

        System.out.println("\n1. Add New Content Block");
        System.out.println("2. Go Back");
        System.out.print("Enter choice (1 to Add New Content Block, 2 to Go Back): ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                AddNewContentBlock.addNewContentBlock(scanner);  // Navigate to Add New Content Block
                break;
            case 2:
                System.out.println("Cancelling and returning to Add New Chapter...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
