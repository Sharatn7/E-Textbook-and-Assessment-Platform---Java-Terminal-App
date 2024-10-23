import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAModifyContentBlockPage {

    public void showModifyContentBlockPage(Scanner scanner, String sectionNumber, String chapterId, String courseId) {
        boolean exit = false;

        while (!exit) {

            System.out.print("Enter Content Block ID: ");
            String contentBlockId = scanner.nextLine();
            
            if (!verifyContentBlockExists(contentBlockId)) {
                System.out.println("Content Block ID not found in the database. Please try again.");
                continue;
            }

            System.out.println("Menu:");
            System.out.println("1. Add Text");
            System.out.println("2. Add Picture");
            System.out.println("3. Add Activity");
            System.out.println("4. Go Back");
            System.out.println("5. Landing Page");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Adding Text to Content Block ID: " + contentBlockId);
                    TAAddTextPage addTextPage = new TAAddTextPage();
                    addTextPage.showAddTextPage(scanner, contentBlockId, sectionNumber, chapterId, courseId);
                    break;
                }
                case 2 -> {
                    System.out.println("Adding Picture to Content Block ID: " + contentBlockId);
                    TAAddPicturePage addPicturePage = new TAAddPicturePage();
                    addPicturePage.showAddPicturePage(scanner, contentBlockId, sectionNumber, chapterId, courseId);
                    break;
                }
                case 3 -> {
                    System.out.println("Adding Activity to Content Block ID: " + contentBlockId);
                    TAAddActivityPage addActivityPage = new TAAddActivityPage();
                    addActivityPage.showAddActivityPage(scanner, chapterId, courseId);
                    break;
                }
                case 4 -> {
                    System.out.println("Returning to Modify Section Page...");
                    exit = true;
                    TAModifySectionPage modifySectionPage = new TAModifySectionPage();
                    modifySectionPage.showModifySectionPage(scanner, chapterId, courseId);
                    break;
                }
                case 5 -> {
                    System.out.println("Returning to Landing Page...");
                    exit = true;
                    TALandingPage landingPage = new TALandingPage();
                    landingPage.showLandingPage(scanner);
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }
    private boolean verifyContentBlockExists(String contentBlockId) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                String sql = "SELECT block_id FROM ContentBlock WHERE block_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, contentBlockId);

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
}
