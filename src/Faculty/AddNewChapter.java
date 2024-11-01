package Faculty;
import java.sql.*;
import homepage.*;
import java.util.Scanner;

public class AddNewChapter {

    public static void addNewChapter(Scanner scanner,String courseId) {
        
    	int textbookId = getTextbookId(courseId);

        System.out.print("Enter Chapter Num: ");
        String chapterNum = scanner.nextLine();

        System.out.print("Enter Chapter Title: ");
        String chapterTitle = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection(); // Get the connection from DBConnect

            if (conn != null) {
                // Updated query to include textbook_id, chapter_id, and title
                String query = "INSERT INTO Chapters (textbook_id, chapter_number, title,createdby) VALUES (?, ?, ?,?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, textbookId);    // Set textbook_id
                pstmt.setString(2, chapterNum);  // Set chapter_id
                pstmt.setString(3, chapterTitle); // Set title
                pstmt.setString(4, "Faculty");

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
            	String chapterId = getChapterId(textbookId,chapterNum);
                AddNewSection.addNewSection(scanner,chapterId);  // Navigate to AddNewSection
                break;
            case 2:
                System.out.println("Cancelling and returning to Active Course Menu...");
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    public static int getTextbookId(String courseId) {
        String query = "SELECT textbook_id FROM Courses WHERE course_id = ?";
        int textbook_id = -1;

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, courseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                	textbook_id = resultSet.getInt("textbook_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return textbook_id;
    }
    public static String getChapterId(int textbookId, String chapterNumber) {
        String query = "SELECT chapter_id FROM Chapters WHERE textbook_id = ? AND chapter_number = ?";
        String chapterId = null;

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, textbookId);
            statement.setString(2, chapterNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    chapterId = resultSet.getString("chapter_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chapterId;
    }
}
