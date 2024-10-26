import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteChapter {

    public static void deleteChapter(Scanner scanner, String chapterId) {
        System.out.print("Enter Chapter ID to Delete: ");
        chapterId = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "DELETE FROM Chapters WHERE chapter_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, chapterId);

                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Chapter '" + chapterId + "' deleted successfully!");
                } else {
                    System.out.println("Failed to delete chapter.");
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
