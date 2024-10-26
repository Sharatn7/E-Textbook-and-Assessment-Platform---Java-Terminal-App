import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteContentBlock {

    public static void deleteContentBlock(Scanner scanner, String contentBlockId) {
        System.out.print("Enter Content Block ID to Delete: ");
        contentBlockId = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "DELETE FROM ContentBlocks WHERE content_block_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, contentBlockId);

                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Content Block '" + contentBlockId + "' deleted successfully!");
                } else {
                    System.out.println("Failed to delete content block.");
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
    }
}
