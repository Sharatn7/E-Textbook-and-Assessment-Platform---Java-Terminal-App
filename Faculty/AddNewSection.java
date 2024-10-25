import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddNewSection {

    public static void addNewSection(Scanner scanner) {
        System.out.print("Enter Section Number: ");
        String sectionNumber = scanner.nextLine();

        System.out.print("Enter Section Title: ");
        String sectionTitle = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "INSERT INTO Sections (section_number, section_title) VALUES (?, ?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, sectionNumber);
                pstmt.setString(2, sectionTitle);

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
