package Admin;
import java.sql.*;
import java.util.*;

public class CreateTextbook {

    private static Scanner scanner = new Scanner(System.in);

    		
    public static void createTextbook() {  
    	
        
     
        while (true) {
        	// Menu after entering the E-textbook details
        	System.out.println("\n=== E-Textbook Management ===");
            System.out.println("A. Enter the title of the new E-textbook");
            System.out.println("B. Enter a unique E-textbook ID");

           
            
            System.out.print("Enter a unique E-textbook ID (integer): ");
            String tb_id = scanner.nextLine();
            
            System.out.print("Enter the title: ");
            String title = scanner.nextLine();
            
            System.out.println("\n=== Menu ===");
            System.out.println("1. Add New Chapter");
            System.out.println("2. Go Back");
            System.out.print("Enter your choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                	
                    saveETextbookToDatabase(tb_id,title);
                    CreateChapter.createNewChapter(tb_id);
                    break;

                case 2:
                    // Discard the input and go back to the Admin Landing Page
                    System.out.println("Going back to the Admin Landing Page...");
                    AdminLandingPage.main(new String[0]);
                    break;

                default:
                    System.out.println("Invalid choice! Please select a valid option.");
                    break;
            }
        }
    	
    }
    		

    // Method to save E-textbook to the database
    public static boolean saveETextbookToDatabase(String tb_id, String title) {
        Connection conn = null;
        String insertQuery = "INSERT INTO Textbooks (textbook_id, title) VALUES (?, ?)";

        try {
            conn = DBConnect.getConnection(); // Assuming DBConnect class has been implemented for DB connection
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return false;
            }
//            System.out.println("Textbook"+title);
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, tb_id);
            preparedStatement.setString(2, title);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
        	if(e.toString().contains("Duplicate"))
        	{
        		System.out.println("Textbook id already exists re enter the details");
        		createTextbook();
        	}
        	else
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
