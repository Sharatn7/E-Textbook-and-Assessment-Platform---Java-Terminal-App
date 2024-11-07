package Admin;
import java.sql.*;
import java.util.*;
public class CreateSection {
	private static Scanner scanner = new Scanner(System.in);
	
	public static void createNewSection(String tb_id,String chapter_id) {  
    	
        
        

        // Step 2: Display menu after entering section details
        while (true) {
        	
        	System.out.println("Section management for "+chapter_id);
        	// Step 1: Take input for Section details
            System.out.print("Enter Section Number: ");
            String section_num = scanner.next();
            scanner.nextLine(); // Consume the newline
            System.out.print("Enter Section Title: ");
            String sectionTitle = scanner.nextLine();
            
            System.out.println("\n=== Menu ===");
            System.out.println("1. Add New Content Block");
            System.out.println("2. Go Back");
            System.out.println("3. Landing Page");
            System.out.print("Enter your choice (1-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                	saveSectionToDatabase(tb_id,chapter_id,section_num,sectionTitle);
                	String sectionId = getSectionId(chapter_id,section_num);
                    CreateContentBlock.createNewContentBlock(tb_id,chapter_id,sectionId);
                    break;
                case 2:
                    // Discard the input and go back to the previous page
                    System.out.println("Going back to the previous page...");
                    return; // Go back to the previous page or previous menu
                    
                case 3:
                    // Discard the input and go back to the landing page
                    System.out.println("Going back to the landing page...");
                    AdminLandingPage.main(new String[0]);
                    break; // Go back to the landing page
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
                    break;
            }
        }
        
       
    }
   public static boolean saveSectionToDatabase(String tb_id,String chapter_id,String section_num, String sectionTitle) {
        Connection conn = null;
        String insertSectionQuery = "INSERT INTO Sections (chapter_id,section_number,title) VALUES (?, ?, ?)";

        try {
            // Establish a connection to the database
            conn = DBConnect.getConnection();
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return false;
            }

            
            // Prepare the SQL statement
            PreparedStatement preparedStatement = conn.prepareStatement(insertSectionQuery);
            preparedStatement.setString(1, chapter_id);
            preparedStatement.setString(2, section_num);
            preparedStatement.setString(3, sectionTitle);

            // Execute the query and check if the row was inserted
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
        	if(e.toString().contains("Duplicate"))
        	{
        		System.out.println("Section already exists in the chapter re enter the details");
        		createNewSection(tb_id,chapter_id);
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
    
   public static String getSectionId(String chapter_id, String section_number) {
       String query = "SELECT section_id FROM Sections WHERE chapter_id = ? AND section_number = ?";
       String section_id = null;

       try (Connection connection = DBConnect.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

           statement.setString(1, chapter_id);
           statement.setString(2, section_number);

           try (ResultSet resultSet = statement.executeQuery()) {
               if (resultSet.next()) {
            	   section_id = resultSet.getString("section_id");
               }
           }

       } catch (SQLException e) {
           e.printStackTrace();
       }

       return section_id;
   }

}
