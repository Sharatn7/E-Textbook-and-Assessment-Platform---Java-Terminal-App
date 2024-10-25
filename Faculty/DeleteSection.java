import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteSection {

    public static void deleteSection(Scanner scanner, String sectionNumber) {
        System.out.print("Enter Section Number to Delete: ");
        sectionNumber = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "DELETE FROM Sections WHERE section_number = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, sectionNumber);

                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Section '" + sectionNumber + "' deleted successfully!");
                } else {
                    System.out.println("Failed to delete section.");
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
