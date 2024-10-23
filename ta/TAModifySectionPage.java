import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAModifySectionPage {

    public void showModifySectionPage(Scanner scanner, String courseId, String previousPage) {
        boolean exit = false;

        while (!exit) {

            System.out.print("Enter Section Number: ");
            String sectionNumber = scanner.nextLine();

            System.out.print("Enter Section Title: ");
            String sectionTitle = scanner.nextLine();

            System.out.print("Enter Chapter Number: ");
            String chapterNumber = scanner.nextLine();

            System.out.print("Enter Book: ");
            String book = scanner.nextLine();

            String chapterId = getChapterIdByNumber(chapterNumber, courseId);
            if (chapterId == null) {
            	System.out.println("Invalid Chapter Number or Chapter does not exist for this course. Please try again.");
            	continue;
            }

            String sectionId = getSectionIdByNumber(sectionNumber, chapterId);
            if (sectionId == null) {
                System.out.println("Invalid Section Number or Section does not exist for this chapter. Please try again.");
                continue;
            }

            System.out.println("Menu:");
            System.out.println("1. Add New Content Block");
            System.out.println("2. Modify Content Block");
            System.out.println("3. Delete Content Block");
            System.out.println("4. Hide Content Block");
            System.out.println("5. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Adding a New Content Block to Section ID: " + sectionId + " in Chapter ID: " + chapterId);
                    TAAddNewContentBlockPage addNewContentBlockPage = new TAAddNewContentBlockPage();
                    addNewContentBlockPage.showAddNewContentBlockPage(scanner, sectionId, chapterId, courseId, "ModifySection");
                    break;
                }
                case 2 -> {
                    System.out.println("Modifying Content Block in Section ID: " + sectionId + " in Chapter ID: " + chapterId);
                    TAModifyContentBlockPage modifyContentBlockPage = new TAModifyContentBlockPage();
                    modifyContentBlockPage.showModifyContentBlockPage(scanner, sectionId, chapterId, courseId);
                    break;
                }
                case 3 -> {
                    System.out.println("Deleting Content Block in Section ID: " + sectionId + " in Chapter ID: " + chapterId);
                    TADeleteContentBlockPage deleteContentBlockPage = new TADeleteContentBlockPage();
                    deleteContentBlockPage.showDeleteContentBlockPage(scanner, sectionId, chapterId, courseId);
                    break;
                }
                case 4 -> {
                    System.out.println("Hiding/Showing Content Block in Section ID: " + sectionId + " in Chapter ID: " + chapterId);
                    TAHideContentBlockPage hideContentBlockPage = new TAHideContentBlockPage();
                    hideContentBlockPage.showHideContentBlockPage(scanner, sectionId, chapterId, courseId);
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
                System.out.println("Database connection failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chapterId;
    }

    private String getSectionIdByNumber(String sectionNumber, String chapterId) {
        String sectionId = null;
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                String sql = "SELECT section_id FROM Section WHERE section_number = ? AND chapter_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, sectionNumber);
                pstmt.setString(2, chapterId);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    sectionId = rs.getString("section_id");
                }
            } else {
                System.out.println("Database connection failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sectionId;
    }
}
