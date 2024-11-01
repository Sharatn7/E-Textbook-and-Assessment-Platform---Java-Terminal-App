package Faculty;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ModifyChapters {

    public static void modifyChapters(Scanner scanner,String courseId) {
        System.out.print("Enter Chapter Num: ");
        String chapterNum = scanner.nextLine();
        
        int textbookId = AddNewChapter.getTextbookId(courseId);
        String chapterId = AddNewChapter.getChapterId(textbookId, chapterNum);
        
        if(chapterId==null) {
        	System.out.println("Enter an Existing ChapterId to Modify");
        	modifyChapters(scanner,courseId);
        }
        


        boolean inModifyChapters = true;
        while (inModifyChapters) {
            System.out.println("\n=== Modify Chapter ===");
            System.out.println("1. Hide Chapter");
            System.out.println("2. Delete Chapter");
            System.out.println("3. Add New Section");
            System.out.println("4. Modify Section");
            System.out.println("5. Go Back");
            System.out.print("Enter choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    hideChapter(scanner, chapterId);  // Call hideChapter
                    break;
                case 2:
                    deleteChapter(scanner, chapterId);  // Call deleteChapter
                    break;
                case 3:
                	AddNewSection.addNewSection(scanner,chapterId);  // Reuse AddNewSection
                    break;
                case 4:
                    ModifySection.modifySection(scanner,chapterId);  // Call modifySection
                    break;
                case 5:
                    System.out.println("Returning to previous menu...");
                    inModifyChapters = false;  // Exit loop
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    public static void hideChapter(Scanner scanner, String chapterId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "UPDATE Chapters SET Hidden = 1 WHERE chapter_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, chapterId);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Chapter '" + chapterId + "' has been hidden.");
                } else {
                    System.out.println("Failed to hide chapter.");
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

    public static void deleteChapter(Scanner scanner, String chapterId) {
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
                    System.out.println("Chapter '" + chapterId + "' has been deleted.");
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
