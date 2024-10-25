import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddActivity {

    public static void addActivity(Scanner scanner, String contentBlockId) {
        System.out.print("Enter Activity ID: ");
        String activityId = scanner.nextLine();

        boolean inAddActivity = true;
        while (inAddActivity) {
            System.out.println("\n1. Add Question");
            System.out.println("2. Go Back");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    if (addActivityToDB(activityId, contentBlockId)) {
                        System.out.println("Activity added successfully.");
                        AddQuestion.addQuestion(scanner);  // Call the addQuestion method
                    } else {
                        System.out.println("Failed to add activity.");
                    }
                    inAddActivity = false;
                    break;
                case 2:
                    System.out.println("Going back...");
                    inAddActivity = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to add question or 2 to go back.");
            }
        }
    }

    // Method to add activity to the database
    public static boolean addActivityToDB(String activityId, String contentBlockId) {
        String query = "INSERT INTO activities (activity_id, content_block_id) VALUES (?, ?)";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, activityId);
            stmt.setString(2, contentBlockId);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;  // Return true if insertion was successful

        } catch (SQLException e) {
            System.out.println("Failed to add activity to the database.");
            e.printStackTrace();
            return false;  // Return false if an error occurred
        }
    }
}
