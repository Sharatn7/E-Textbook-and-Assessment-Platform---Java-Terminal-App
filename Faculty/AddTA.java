import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddTA {

    public static void addTA(Scanner scanner) {
        System.out.print("Enter TA First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter TA Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter TA Email: ");
        String email = scanner.nextLine();

        boolean inAddTA = true;
        while (inAddTA) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Save the TA information to the database
                    if (saveTAToDatabase(firstName, lastName, email)) {
                        System.out.println("TA added successfully!");
                    } else {
                        System.out.println("Failed to add TA.");
                    }
                    inAddTA = false;  // Exit after saving
                    break;
                case 2:
                    System.out.println("Cancelling and returning to previous menu...");
                    inAddTA = false;  // Exit without saving
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to save or 2 to cancel.");
            }
        }
    }

    private static boolean saveTAToDatabase(String firstName, String lastName, String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();
            if (conn != null) {
                String insertSQL = "INSERT INTO TA (first_name, last_name, email) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(insertSQL);
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, email);

                int rowsInserted = pstmt.executeUpdate();
                return rowsInserted > 0;
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
        return false;
    }
}
