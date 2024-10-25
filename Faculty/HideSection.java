import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class HideSection {

    public static void hideSection(Scanner scanner, String sectionNumber) {
        boolean inHideSection = true;
        while (inHideSection) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Hide the section in the database
                    hideSectionInDB(sectionNumber);
                    inHideSection = false;
                    break;
                case 2:
                    System.out.println("Cancelling hide operation and returning to Modify Section menu...");
                    inHideSection = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Method to hide section in the database
    public static void hideSectionInDB(String sectionNumber) {
        String query = "UPDATE sections SET is_hidden = true WHERE section_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sectionNumber);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Section '" + sectionNumber + "' hidden successfully.");
            } else {
                System.out.println("Failed to hide section.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to update section status in the database.");
            e.printStackTrace();
        }
    }
}
