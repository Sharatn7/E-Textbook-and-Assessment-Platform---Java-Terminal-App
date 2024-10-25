import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class HideChapter {

    public static void hideChapter(Scanner scanner, String chapterId) {
        boolean inHideChapter = true;
        while (inHideChapter) {
            System.out.println("\n=== Hide Chapter ===");
            System.out.println("1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Hide the chapter in the database
                    hideChapterInDB(chapterId);
                    inHideChapter = false;
                    break;
                case 2:
                    System.out.println("Cancelling hide operation and returning to Modify Chapters menu...");
                    inHideChapter = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Method to hide chapter in the database
    public static void hideChapterInDB(String chapterId) {
        String query = "UPDATE chapters SET is_hidden = true WHERE chapter_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, chapterId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Chapter '" + chapterId + "' hidden successfully.");
            } else {
                System.out.println("Failed to hide chapter.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to update chapter status in the database.");
            e.printStackTrace();
        }
    }
}
