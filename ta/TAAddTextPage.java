import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class TAAddTextPage {


    public void showAddTextPage(Scanner scanner, String contentBlockId, String sectionId, String chapterId, String courseId) {
        boolean exit = false;

        while (!exit) {

            System.out.print("Enter the Text for Content Block ID: " + contentBlockId + ": ");
            String textContent = scanner.nextLine();

            System.out.println("Menu:");
            System.out.println("1. Add");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {

                    if (saveTextToContentBlock(contentBlockId, sectionId, textContent)) {
                        System.out.println("Text saved successfully to Content Block ID: " + contentBlockId);
                    } else {
                        System.out.println("Failed to save text. Please try again.");
                        continue;
                    }

                    exit = true;
                    break;
                }
                case 2 -> {
                    System.out.println("Returning to Add New Content Block Page...");
                    exit = true;
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private boolean saveTextToContentBlock(String contentBlockId, String sectionId, String textContent) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {

                String sql = "INSERT INTO ContentBlock (block_id, section_id, content, type, created_by) " +
                             "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, contentBlockId);
                pstmt.setString(2, sectionId);
                pstmt.setString(3, textContent);
                pstmt.setString(4, "text");
                pstmt.setString(5, "TA");

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } else {
                System.out.println("Failed to connect to the database.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
