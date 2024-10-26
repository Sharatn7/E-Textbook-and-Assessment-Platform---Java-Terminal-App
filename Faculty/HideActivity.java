import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class HideActivity {

    public static void hideActivity(Scanner scanner, String contentBlockId) {
        System.out.print("Enter Activity ID to Hide: ");
        String activityId = scanner.nextLine();

        boolean inHideActivity = true;
        while (inHideActivity) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Hide the activity in the database
                    hideActivityInDB(activityId, contentBlockId);
                    inHideActivity = false;
                    break;
                case 2:
                    System.out.println("Cancelling hide operation and returning to Modify Content Block menu...");
                    inHideActivity = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Method to hide activity in the database
    public static void hideActivityInDB(String activityId, String contentBlockId) {
        String query = "UPDATE activities SET is_hidden = true WHERE activity_id = ? AND content_block_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, activityId);
            stmt.setString(2, contentBlockId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Activity '" + activityId + "' hidden successfully.");
            } else {
                System.out.println("Failed to hide activity.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to update activity status in the database.");
            e.printStackTrace();
        }
    }
}
