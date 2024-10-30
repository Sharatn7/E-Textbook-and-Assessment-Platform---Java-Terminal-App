import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
public class CreateActiveCourse {
	 private static Scanner scanner = new Scanner(System.in);

	    public static void createNewActiveCourse() {
	        System.out.println("\n=== Create New Active Course ===");

	        // Step 1: Collect Course details from the user
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

	        System.out.print("Enter Unique Token: ");
	        String uniqueToken = scanner.nextLine();

	        System.out.print("Enter Course Capacity: ");
	        int capacity = scanner.nextInt();
	        scanner.nextLine(); // Consume newline after integer input

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
	                    if (saveActiveCourseToDatabase(courseId, courseName, etextbookId, facultyId, startDate, endDate, uniqueToken, capacity)) {
	                        System.out.println("Active course successfully created.");
	                    } else {
	                        System.out.println("Failed to create active course. Please try again.");
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

	    private static boolean saveActiveCourseToDatabase(String courseId, String courseName, int etextbookId, String facultyId,
	                                                      String startDate, String endDate, String uniqueToken, int capacity) {
	        Connection conn = null;
	        String insertQuery = "INSERT INTO Courses (`Unique Course ID`, `Course Name`, `Unique ID of the E-textbook`, " +
	                             "`Course Type`, `Faculty Member ID`, `Course Start date`, `Course End date`, `Unique Token`, `Course Capacity`) " +
	                             "VALUES (?, ?, ?, 'Active', ?, ?, ?, ?, ?)";

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
	            preparedStatement.setString(4, facultyId);
	            preparedStatement.setString(5, startDate);
	            preparedStatement.setString(6, endDate);
	            preparedStatement.setString(7, uniqueToken);
	            preparedStatement.setInt(8, capacity);

	            int rowsInserted = preparedStatement.executeUpdate();
	            return rowsInserted > 0;

	        } catch (SQLException e) {
	            if (e.toString().contains("Duplicate")) {
	                System.out.println("Course already exists with the provided ID or Unique Token. Please enter a unique Course ID and Token.");
	                createNewActiveCourse(); // Restart the course creation process if there’s a duplicate
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


