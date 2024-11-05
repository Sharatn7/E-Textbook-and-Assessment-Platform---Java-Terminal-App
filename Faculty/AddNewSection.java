package Faculty;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AddNewSection {

    public static void addNewSection(Scanner scanner,String chapterId) {
        
        System.out.print("Enter Section Num: ");
        String sectionNum = scanner.nextLine();

        System.out.print("Enter Section Title: ");
        String sectionTitle = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection(); // Get connection from DBConnect

            if (conn != null) {
                // Query to insert into the Sections table
            	String insertSectionQuery = "INSERT INTO Sections (chapter_id,section_number,title,createdby) VALUES (?, ?, ?,?)";
            	PreparedStatement preparedStatement = conn.prepareStatement(insertSectionQuery);
                preparedStatement.setString(1, chapterId);
                preparedStatement.setString(2, sectionNum);
                preparedStatement.setString(3, sectionTitle);
                preparedStatement.setString(4, "Faculty");

                int rowsInserted = preparedStatement.executeUpdate();
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
            	String sectionId = getSectionId(chapterId,sectionNum);
                AddNewContentBlock.addNewContentBlock(scanner,sectionId);  // Navigate to Add New Content Block
                break;
            case 2:
                System.out.println("Cancelling and returning to Add New Chapter...");
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    public static String getSectionId(String chapter_id, String section_number) {
        String query = "SELECT section_id FROM Sections WHERE chapter_id = ? AND section_number = ?";
        String section_id = null;

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, chapter_id);
            statement.setString(2, section_number);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
             	   section_id = resultSet.getString("section_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return section_id;
    }
}
