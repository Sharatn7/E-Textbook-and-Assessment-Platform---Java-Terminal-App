import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TADeleteContentBlockPage {

    public void showDeleteContentBlockPage(Scanner scanner, String sectionId, String chapterId, String courseId) {
        boolean exit = false;

        while (!exit) {

            System.out.print("Enter Content Block ID: ");
            String contentBlockId = scanner.nextLine();

            // Verify if the Content Block exists before deleting
            if (!verifyContentBlockExists(contentBlockId, sectionId)) {
                System.out.println("Content Block ID not found in the database for this section. Please try again.");
                continue;
            }

            System.out.println("Menu:");
            System.out.println("1. Delete");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    if (deleteContentBlock(contentBlockId)) {
                        System.out.println("Content Block ID: " + contentBlockId + " successfully deleted from Section " + sectionId);
                    } else {
                        System.out.println("Failed to delete Content Block ID: " + contentBlockId);
                    }
                    exit = true;
                    break;
                }
                case 2 -> {
                    System.out.println("Returning to Modify Section Page...");
                    exit = true;
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }


    private boolean verifyContentBlockExists(String contentBlockId, String sectionId) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                String sql = "SELECT block_id FROM ContentBlock WHERE block_id = ? AND section_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, contentBlockId);
                pstmt.setString(2, sectionId);

                ResultSet rs = pstmt.executeQuery();
                return rs.next();
            } else {
                System.out.println("Failed to connect to the database.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean deleteContentBlock(String contentBlockId) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                String sql = "DELETE FROM ContentBlock WHERE block_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, contentBlockId);

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
