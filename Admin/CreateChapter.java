package Admin;
import java.sql.*;
import java.util.*;

public class CreateChapter {
	private static Scanner scanner = new Scanner(System.in);	
    public static void createNewChapter(String tb_id) {  
        
       
            // Display Menu after entering the details
            while (true) {
            	System.out.println("\n=== Chapter Management for "+tb_id+" ===");
                System.out.println("A. Enter Unique Chapter ID");
                System.out.println("B. Enter Chapter Title");

                // Take input for Chapter details
                System.out.print("Enter the Chapter Number: ");
                String chapter_number = scanner.next();
                scanner.nextLine(); 

                System.out.print("Enter the Chapter Title: ");
                String chapterTitle = scanner.nextLine();
                
                System.out.println("\n=== Menu ===");
                System.out.println("1. Add New Section");
                System.out.println("2. Go Back");
                System.out.println("3. Landing Page");
                System.out.print("Enter your choice (1-3): ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline

                switch (choice) {
                    case 1:
                        // Redirect to Add New Section page
                    	saveChapterToDatabase(tb_id,chapterTitle,chapter_number);

                    	String chapter_id = getChapterId(tb_id,chapter_number);
                    	CreateSection.createNewSection(tb_id,chapter_id);
                        break;

                    case 2:
                        // Discard the input and go back to the previous page
                        System.out.println("Going back to the previous page...");
                        return;  // Exit current flow, go back to the previous page

                    case 3:
                        // Discard the input and go to the landing page
                        System.out.println("Going back to the landing page...");
                        AdminLandingPage.main(new String[0]);
                        break;  // Exit current flow, go to the landing page

                    default:
                        System.out.println("Invalid choice! Please select a valid option.");
                        break;
                }
            }
        }
    
    
    private static boolean saveChapterToDatabase(String textbookId, String chapterTitle, String chapter_number) {
    	Connection conn = null;
        String insertQuery = "INSERT INTO Chapters (textbook_id, chapter_number, title) VALUES (?, ?, ?)";
        try {
            conn = DBConnect.getConnection();  // Assuming DBConnect class exists
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return false;
            }

            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            // Assuming chapter_id is generated by business logic, or you can use UUID/random string generator for it
            // You need to implement this logic

            preparedStatement.setString(1, textbookId);
            preparedStatement.setString(2, chapter_number);
            preparedStatement.setString(3, chapterTitle);
            
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
        	if(e.toString().contains("Duplicate"))
        	{
        		System.out.println("Chapter already exists in the textbook re enter the details");
        		createNewChapter(textbookId);
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
    public static String getChapterId(String textbookId, String chapterNumber) {
        String query = "SELECT chapter_id FROM Chapters WHERE textbook_id = ? AND chapter_number = ?";
        String chapterId = null;

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, textbookId);
            statement.setString(2, chapterNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    chapterId = resultSet.getString("chapter_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chapterId;
    }
    
 
}
