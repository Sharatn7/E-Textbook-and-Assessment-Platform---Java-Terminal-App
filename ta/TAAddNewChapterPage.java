import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAAddNewChapterPage {

    public void showAddNewChapterPage(Scanner scanner, String courseId) {
        boolean exit = false;

        while (!exit) {
        	System.out.print("Enter Chapter Number: ");
            String chapterNumber = scanner.nextLine();

            System.out.print("Enter Chapter Title: ");
            String chapterTitle = scanner.nextLine();

            int chapterId = addNewChapterToDatabase(chapterNumber, chapterTitle, courseId);
            if (chapterId != -1) {
                System.out.println("Successfully added new chapter with ID: " + chapterId + " and title: " + chapterTitle);
            } else {
                System.out.println("Failed to add new chapter. Please try again.");
                continue;
            }

            System.out.println("Menu:");
            System.out.println("1. Add New Section");
            System.out.println("2. Modify Section");
            System.out.println("3. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Redirecting to Add New Section...");
                    TAAddNewSectionPage addNewSectionPage = new TAAddNewSectionPage();
                    addNewSectionPage.showAddNewSectionPage(scanner, String.valueOf(chapterId), courseId, "AddNewChapter");
                    break;
                }
                case 2 -> {
                    System.out.println("Modifying Section in Chapter ID: " + chapterId);
                    TAModifySectionPage modifySectionPage = new TAModifySectionPage();
                    modifySectionPage.showModifySectionPage(scanner, String.valueOf(chapterId), courseId);
                    break;
                }
                case 3 -> {
                    System.out.println("Returning to Active Course Page...");
                    exit = true;
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private int addNewChapterToDatabase(String chapterNumber, String chapterTitle, String courseId) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {

                String sql = "INSERT INTO Chapter (chapter_num, tb_id, title, created_by) " +
                             "VALUES (?, (SELECT tb_id FROM Course WHERE course_id = ?), ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, chapterNumber);
                pstmt.setString(2, courseId);
                pstmt.setString(3, chapterTitle);
                pstmt.setString(4, "TA");

                int rowsAffected = pstmt.executeUpdate();
                

                if (rowsAffected > 0) {
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
