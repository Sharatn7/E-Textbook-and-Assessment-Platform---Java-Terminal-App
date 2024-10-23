import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAHideContentBlockPage {

    public void showHideContentBlockPage(Scanner scanner, String sectionId, String chapterId, String courseId) {
        boolean exit = false;

        while (!exit) {
            System.out.print("Enter Content Block ID: ");
            String contentBlockId = scanner.nextLine();

            if (!verifyContentBlockExists(contentBlockId, sectionId)) {
                System.out.println("Content Block ID not found in the database for this section. Please try again.");
                continue;
            }

            System.out.println("Menu:");
            System.out.println("1. Hide/Unhide Content Block");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {

                    if (toggleContentBlockHiddenStatus(contentBlockId)) {
                        System.out.println("Content Block ID: " + contentBlockId + " successfully updated.");
                    } else {
                        System.out.println("Failed to update Content Block ID: " + contentBlockId);
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


    private boolean toggleContentBlockHiddenStatus(String contentBlockId) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {

                String selectSql = "SELECT hidden FROM ContentBlock WHERE block_id = ?";
                PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                selectStmt.setString(1, contentBlockId);

                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    boolean isHidden = rs.getBoolean("hidden");


                    String updateSql = "UPDATE ContentBlock SET hidden = ? WHERE block_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setBoolean(1, !isHidden);
                    updateStmt.setString(2, contentBlockId);

                    int rowsAffected = updateStmt.executeUpdate();
                    return rowsAffected > 0;
                } else {
                    System.out.println("Content Block not found.");
                    return false;
                }
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
