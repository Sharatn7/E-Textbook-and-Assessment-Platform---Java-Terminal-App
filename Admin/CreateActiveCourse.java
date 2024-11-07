package Admin;
import java.sql.Connection;
import java.sql.*;
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
	        
	        
	        String tb_id = getNewTextbookInstanceId(etextbookId);
	        
	        if(tb_id==null) {
	        	System.out.println("Give an existing textbook id");
	        	createNewActiveCourse();
	        	
	        }
	        System.out.println(tb_id); 

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
	                	
	                    if (createTextbook(tb_id,etextbookId) && saveActiveCourseToDatabase(courseId, courseName, tb_id, facultyId, startDate, endDate, uniqueToken, capacity)) {
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
	                    break;

	                default:
	                    System.out.println("Invalid choice! Please select a valid option.");
	                    break;
	            }
	        }
	    }
	    
	    public static boolean createTextbook(String tb_id, int originalTextbookId) {
	    	Connection conn = null;
	        String fetchTitleQuery = "SELECT title FROM Textbooks WHERE textbook_id = ?";
	        String insertQuery = "INSERT INTO Textbooks (textbook_id, title) VALUES (?, ?)";
	        try {
	            conn = DBConnect.getConnection(); // Assuming DBConnect class exists for database connection
	            if (conn == null) {
	                System.out.println("Failed to establish a database connection.");
	                return false;
	            }


	            PreparedStatement fetchTitleStatement = conn.prepareStatement(fetchTitleQuery);
	            
	             PreparedStatement insertStatement = conn.prepareStatement(insertQuery);

	            // Fetch the title of the original textbook
	            fetchTitleStatement.setInt(1, originalTextbookId);
	            ResultSet titleResultSet = fetchTitleStatement.executeQuery();
	            
	            if (!titleResultSet.next()) {
	                System.out.println("Original textbook ID not found in the Textbooks table.");
	                return false;
	            }
	            
	            String title = titleResultSet.getString("title");

	           
	            // Insert the new textbook with the fetched title
	            insertStatement.setString(1, tb_id);
	            insertStatement.setString(2, title);
	       
	            
	            int rowsInserted = insertStatement.executeUpdate();
	            return rowsInserted > 0;

	        } catch (SQLException e) {e.printStackTrace();
	        return false;
	        }
	    }
	         


	    public static boolean saveActiveCourseToDatabase(String courseId, String courseName, String etextbookId, String facultyId,
	                                                      String startDate, String endDate, String uniqueToken, int capacity) {
	        Connection conn = null;
	        String insertQuery = "INSERT INTO Courses (course_id,title,textbook_id,start_date,end_date,course_type) VALUES (?, ?, ?, ?, ?, ?)";
	        String insertQuery1 = "INSERT INTO activeCourse (course_id,unique_token,capacity) VALUES (?,?,?)";
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
	            preparedStatement.setString(3, etextbookId);
	            preparedStatement.setString(4, startDate);
	            preparedStatement.setString(5, endDate);
	            preparedStatement.setString(6, "Active");
	            int rowsInserted = preparedStatement.executeUpdate();
	            
	            preparedStatement = conn.prepareStatement(insertQuery1);
	            preparedStatement.setString(1, courseId);
	            preparedStatement.setString(2, uniqueToken);
	            preparedStatement.setInt(3, capacity);
	            int rowsInserted1 = preparedStatement.executeUpdate();
	            
	            preparedStatement = conn.prepareStatement(insertQuery2);
	            preparedStatement.setString(1, courseId);
	            preparedStatement.setString(2, facultyId);
	            int rowsInserted2 = preparedStatement.executeUpdate();
	                 
	            
	            return rowsInserted1 > 0 && rowsInserted>0 && rowsInserted2>0;

	        } catch (SQLException e) {
	            if (e.toString().contains("Duplicate")) {
	                System.out.println("Course already exists with the provided ID or Unique Token or Faculty. Please enter a unique Course ID and Token or Faculty.");
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
	    
	    public static String getNewTextbookInstanceId(int textbookId) {
	        Connection conn = null;
	        String latestTextbookInstanceId = null;

	        try {
	            conn = DBConnect.getConnection();
	            if (conn == null) {
	                System.out.println("Failed to establish a database connection.");
	                return null;
	            }

	            // Query to check if textbook exists in Textbooks table
	            String textbookCheckQuery = "SELECT textbook_id FROM Textbooks WHERE textbook_id = ?";
	            PreparedStatement preparedStatement = conn.prepareStatement(textbookCheckQuery);
	            preparedStatement.setInt(1, textbookId);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (!resultSet.next()) {
	                System.out.println("Textbook ID not found in the Textbooks table.");
	                return null;
	            }

	         // Query to count the instances of the specified textbook in the Courses table
	            String instanceQuery = "SELECT COUNT(*) AS instance_count FROM Courses WHERE textbook_id LIKE ?";
	            preparedStatement = conn.prepareStatement(instanceQuery);
	            preparedStatement.setString(1, "i%t" + textbookId); // Matching the pattern "i%t<textbookId>"
	            resultSet = preparedStatement.executeQuery();

	            int maxInstanceNumber = 0;
	            if (resultSet.next()) {
	                maxInstanceNumber = resultSet.getInt("instance_count");
	            }

	            // Increment to get the new instance number
	            int newInstanceNumber = maxInstanceNumber + 1;
	            latestTextbookInstanceId = "i" + newInstanceNumber + "t" + textbookId;
	        } catch (SQLException e) {
	            e.printStackTrace();
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
	        return latestTextbookInstanceId;
	    }
	    
	    
	    
	    
	    
}


