import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAActiveCoursePage {

    private String taId;

    public TAActiveCoursePage(String taId) {
        this.taId = taId;
    }

    public void showActiveCoursePage(Scanner scanner) {
        System.out.println("You are now in the Active Course Page for TA: " + taId);

        boolean exit = false;
        while (!exit) {

            System.out.print("Enter Course ID: ");
            String courseId = scanner.nextLine();

            if (!isCourseAssignedToTA(courseId)) {
                System.out.println("Invalid Course ID or you are not assigned to this course. Please try again.");
                continue;
            }

            System.out.println("Menu:");
            System.out.println("1. View Students");
            System.out.println("2. Add New Chapter");
            System.out.println("3. Modify Chapters");
            System.out.println("4. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
	            case 1 -> {
	                System.out.println("Viewing Students for Course ID: " + courseId);
	                TAViewStudentsPage viewStudentsPage = new TAViewStudentsPage();
	                viewStudentsPage.showViewStudentsPage(scanner, courseId);
	                break;
	            }
	            case 2 -> {
	                System.out.println("Adding a New Chapter to Course ID: " + courseId);
	                TAAddNewChapterPage addNewChapterPage = new TAAddNewChapterPage();
	                addNewChapterPage.showAddNewChapterPage(scanner, courseId);
	                break;
	            }
	            case 3 -> {
	                System.out.println("Modifying Chapters in Course ID: " + courseId);
	                TAModifyChapterPage modifyChapterPage = new TAModifyChapterPage();
	                modifyChapterPage.showModifyChapterPage(scanner, courseId);
	                break;
	            }
                case 4 -> {
                    System.out.println("Returning to Landing Page...");
                    exit = true;
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private boolean isCourseAssignedToTA(String courseId) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                String sql = "SELECT status FROM TA WHERE ta_id = ? AND course_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, taId);
                pstmt.setString(2, courseId);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String taStatus = rs.getString("status");
                    if ("current".equalsIgnoreCase(taStatus)) {
                        return true;
                    } else {
                        System.out.println("You are no longer a TA for this course. Your status is: " + taStatus);
                        return false;
                    }
                } else {
                    System.out.println("No course found with Course ID: " + courseId + " for TA ID: " + taId);
                    return false;
                }
            } else {
                System.out.println("Database connection failed.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
