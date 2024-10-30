import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
public class CreateEvaluationCourse {
	private static Scanner scanner = new Scanner(System.in);

    public static void createNewEvaluationCourse() {
        System.out.println("\n=== Create New Evaluation Course ===");

        // Step 1: Collect Evaluation Course details from the user
        System.out.print("Enter Unique Course ID: ");
        String courseId = scanner.nextLine();

        System.out.print("Enter Course Name: ");
        String courseName = scanner.nextLine();

        System.out.print("Enter Unique ID of the E-textbook: ");
        int etextbookId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter Faculty Member ID: ");
        String facultyId = scanner.nextLine();

        System.out.print("Enter Course Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();

        System.out.print("Enter Course End Date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        // Display Menu after entering course details
        while (true) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Save");
            System.out.println("2. Cancel");
            System.out.println("3. Landing Page");
            System.out.print("Enter your choice (1-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Save course to the database and return to the previous page
                    if (saveEvaluationCourseToDatabase(courseId, courseName, etextbookId, facultyId, startDate, endDate)) {
                        System.out.println("Evaluation course successfully created.");
                    } else {
                        System.out.println("Failed to create evaluation course. Please try again.");
                    }
                    return;

                case 2:
                    // Discard the input and go back to the previous page
                    System.out.println("Action canceled. Returning to the previous page...");
                    return;

                case 3:
                    // Discard the input and go to the landing page
                    System.out.println("Returning to the landing page...");
                    AdminLandingPage.main(new String[0]);
                    return;

                default:
                    System.out.println("Invalid choice! Please select a valid option.");
                    break;
            }
        }
    }

    private static boolean saveEvaluationCourseToDatabase(String courseId, String courseName, int etextbookId, String facultyId,
                                                          String startDate, String endDate) {
    	Connection conn = null;
        String insertQuery = "INSERT INTO Courses (course_id,title,textbook_id,start_date,end_date,course_type) VALUES (?, ?, ?, ?, ?, ?)";
        String insertQuery2 = "INSERT INTO Faculty (course_id,faculty_id) VALUES (?,?)";
        try {
            conn = DBConnect.getConnection(); // Assuming DBConnect class exists for database connection
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return false;
            }

            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, courseName);
            preparedStatement.setInt(3, etextbookId);
            preparedStatement.setString(4, startDate);
            preparedStatement.setString(5, endDate);
            preparedStatement.setString(6, "Evaluation");
            int rowsInserted = preparedStatement.executeUpdate();
            
            preparedStatement = conn.prepareStatement(insertQuery2);
            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, facultyId);
            int rowsInserted2 = preparedStatement.executeUpdate();
            
            return rowsInserted > 0 && rowsInserted2>0;

        } catch (SQLException e) {
            if (e.toString().contains("Duplicate")) {
                System.out.println("Course already exists with the provided ID. Please enter a unique Course ID.");
                createNewEvaluationCourse(); // Restart the evaluation course creation process if there’s a duplicate
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
