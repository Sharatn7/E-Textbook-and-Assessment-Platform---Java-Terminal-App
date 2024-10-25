import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddNewChapter {

    public static void addNewChapter(Scanner scanner) {
        System.out.print("Enter Chapter ID: ");
        String chapterId = scanner.nextLine();

        System.out.print("Enter Chapter Title: ");
        String chapterTitle = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection(); // Get the connection from DBConnect

            if (conn != null) {
                String query = "INSERT INTO Chapters (chapter_id, chapter_title) VALUES (?, ?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, chapterId);
                pstmt.setString(2, chapterTitle);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Chapter '" + chapterTitle + "' added successfully.");
                } else {
                    System.out.println("Failed to add chapter.");
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

        System.out.println("\n1. Add New Section");
        System.out.println("2. Go Back");
        System.out.print("Enter choice (1 to Add New Section, 2 to Go Back): ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                AddNewSection.addNewSection(scanner);  // Navigate to AddNewSection
                break;
            case 2:
                System.out.println("Cancelling and returning to Active Course Menu...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
