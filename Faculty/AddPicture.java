import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddPicture {

    public static void addPicture(Scanner scanner, String contentBlockId) {
        System.out.print("Enter Picture URL: ");
        String url = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "INSERT INTO Pictures (content_block_id, picture_url) VALUES (?, ?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, contentBlockId);
                pstmt.setString(2, url);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Picture added to Content Block '" + contentBlockId + "' successfully!");
                } else {
                    System.out.println("Failed to add picture.");
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

        boolean inAddPicture = true;
        while (inAddPicture) {
            System.out.println("\n1. ADD");
            System.out.println("2. Go Back");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    if (!url.isEmpty()) {
                        System.out.println("Picture added to Content Block '" + contentBlockId + "' successfully!");
                    } else {
                        System.out.println("Failed to add picture.");
                    }
                    inAddPicture = false;
                    break;
                case 2:
                    System.out.println("Discarding input and going back...");
                    inAddPicture = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to add or 2 to go back.");
            }
        }
    }
}
