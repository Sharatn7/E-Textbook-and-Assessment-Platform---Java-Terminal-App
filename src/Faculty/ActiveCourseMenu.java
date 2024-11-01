package Faculty;
import homepage.*;
import java.util.Scanner;
import java.sql.*;
import java.sql.*;

public class ActiveCourseMenu {

    public static void activeCourseMenu(Scanner scanner) {
        System.out.println("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        if(checkIfFacultyCourseExists(HomePage.facultyId,courseId)==false) {
        	System.out.println("Enter your Course Id correctly that is assigned to you");
        	activeCourseMenu(scanner);
        }

        boolean inActiveCourse = true;
        while (inActiveCourse) {
            System.out.println("\n=== Active Course Menu ===");
            System.out.println("1. View Worklist");
            System.out.println("2. Approve Enrollment");
            System.out.println("3. View Students");
            System.out.println("4. Add New Chapter");
            System.out.println("5. Modify Chapters");
            System.out.println("6. Add TA");
            System.out.println("7. Go Back");
            System.out.print("Enter choice (1-7): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    ViewWorklist.viewWorklist(scanner,courseId);  // Now connected to the database
                    break;
                case 2:
                    ApproveEnrollment.displayMenu(scanner,courseId);  // Now connected to the database
                    break;
                case 3:
                    ViewStudents.displayMenu(scanner,courseId);  // Now connected to the database
                    break;
                case 4:
                    AddNewChapter.addNewChapter(scanner,courseId);
                    break;
                case 5:
                    ModifyChapters.modifyChapters(scanner,courseId);
                    break;
                case 6:
                    AddTA.addTA(scanner,courseId);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    public static boolean checkIfFacultyCourseExists(String facultyId, String courseId) {
        String query = "SELECT * FROM Faculty WHERE faculty_id = ? AND course_id = ?";
        
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, facultyId);
            statement.setString(2, courseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Returns true if a matching row exists
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false; // Returns false if no match found or if an error occurs
    }
}
