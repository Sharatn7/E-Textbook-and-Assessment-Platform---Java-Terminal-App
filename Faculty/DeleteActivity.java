import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteActivity {

    public static void deleteActivity(Scanner scanner, String contentBlockId) {
        System.out.print("Enter Activity ID to Delete: ");
        String activityId = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "DELETE FROM Activities WHERE activity_id = ? AND content_block_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, activityId);
                pstmt.setString(2, contentBlockId);

                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Activity '" + activityId + "' deleted from Content Block '" + contentBlockId + "' successfully!");
                } else {
                    System.out.println("Failed to delete activity.");
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
