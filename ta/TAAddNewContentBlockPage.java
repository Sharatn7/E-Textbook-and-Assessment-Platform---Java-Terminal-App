import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class TAAddNewContentBlockPage {

    public void showAddNewContentBlockPage(Scanner scanner, String sectionId, String chapterId, String courseId, String previousPage) {
        boolean exit = false;

        while (!exit) {

            System.out.print("Enter Content Block ID: ");
            String contentBlockId = scanner.nextLine();

            System.out.println("Menu:");
            System.out.println("1. Add Text");
            System.out.println("2. Add Picture");
            System.out.println("3. Add Activity");
            System.out.println("4. Hide Activity");
            System.out.println("5. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Adding Text to Content Block ID: " + contentBlockId);
                    TAAddTextPage addTextPage = new TAAddTextPage();
                    addTextPage.showAddTextPage(scanner, contentBlockId, sectionId, chapterId, courseId);
                    break;
                }
                case 2 -> {
                    System.out.println("Adding Picture to Content Block ID: " + contentBlockId);
                    TAAddPicturePage addPicturePage = new TAAddPicturePage();
                    addPicturePage.showAddPicturePage(scanner, contentBlockId, sectionId, chapterId, courseId);
                    break;
                }
                case 3 -> {
                    System.out.println("Adding Activity to Content Block ID: " + contentBlockId);
                    TAAddActivityPage addActivityPage = new TAAddActivityPage();
                    addActivityPage.showAddActivityPage(scanner, chapterId, courseId);
                    break;
                }
                case 4 -> {
                    toggleActivityVisibility(contentBlockId);
                    break;
                }
                case 5 -> {
                    System.out.println("Returning to Previous Page...");
                    exit = true;
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void toggleActivityVisibility(String contentBlockId) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {

                String sqlSelect = "SELECT hidden FROM ContentBlock WHERE block_id = ?";
                PreparedStatement selectStmt = conn.prepareStatement(sqlSelect);
                selectStmt.setString(1, contentBlockId);
                
                boolean isCurrentlyHidden = selectStmt.executeQuery().next() && selectStmt.getResultSet().getBoolean("hidden");

                String sqlUpdate = "UPDATE ContentBlock SET hidden = ? WHERE block_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate);
                updateStmt.setBoolean(1, !isCurrentlyHidden);
                updateStmt.setString(2, contentBlockId);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println((isCurrentlyHidden ? "Showing" : "Hiding") + " activity in Content Block ID: " + contentBlockId);
                } else {
                    System.out.println("Failed to update activity visibility.");
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
