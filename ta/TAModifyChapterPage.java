import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAModifyChapterPage {
    

    public void showModifyChapterPage(Scanner scanner, String courseId) {
        boolean exit = false;

        while (!exit) {

            System.out.print("Enter Unique Chapter Number: ");
            String chapterNumber = scanner.nextLine();

            String chapterId = getChapterIdByNumber(chapterNumber, courseId);

            if (chapterId == null) {
                System.out.println("Chapter not found. Please try again.");
                continue;
            }

            System.out.println("Menu:");
            System.out.println("1. Add New Section");
            System.out.println("2. Modify Section");
            System.out.println("3. Add New Activity");
            System.out.println("4. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Adding a New Section to Chapter ID: " + chapterId + " in Course ID: " + courseId);
                    TAAddNewSectionPage addNewSectionPage = new TAAddNewSectionPage();
                    addNewSectionPage.showAddNewSectionPage(scanner, chapterId, courseId, "ModifyChapter");
                    break; 
                }
                case 2 -> {
                    System.out.println("Modifying Section in Chapter ID: " + chapterId + " in Course ID: " + courseId);
                    TAModifySectionPage modifySectionPage = new TAModifySectionPage();
                    modifySectionPage.showModifySectionPage(scanner, chapterId, courseId);
                    break;
                }
                case 3 -> {
                    System.out.println("Adding a New Activity to Chapter ID: " + chapterId + " in Course ID: " + courseId);
                    TAAddActivityPage addActivityPage = new TAAddActivityPage();
                    addActivityPage.showAddActivityPage(scanner, chapterId, courseId);
                    break;
                }
                case 4 -> {
                    System.out.println("Returning to Active Course Page...");
                    exit = true;
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private String getChapterIdByNumber(String chapterNumber, String courseId) {
        String chapterId = null;
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                String sql = "SELECT chapter_id FROM Chapter WHERE chapter_num = ? AND tb_id = (SELECT tb_id FROM Course WHERE course_id = ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, chapterNumber);
                pstmt.setString(2, courseId);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    chapterId = rs.getString("chapter_id");
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chapterId;
    }
}
