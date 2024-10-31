import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddText {

    public static void addText(Scanner scanner, String sectionId,String blockNum) {
    	System.out.print("Enter Text Content: ");
        String content = scanner.nextLine();

        // Step 2: Display menu after entering the Text content
        while (true) {
            System.out.println("\n=== Add Text Menu ===");
            System.out.println("1. Add");
            System.out.println("2. Go Back");
            
            System.out.print("Enter your choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Step 3: Save text content to the database and go back to the previous page
                    boolean success = saveTextToDatabase(sectionId,blockNum,content);
                    if (success) {
                        System.out.println("Text content added successfully!");
                    } else {
                        System.out.println("Failed to add text content.");
                    }
                    break; // Go back to the previous page

                case 2:
                    // Discard the input and go back to the previous page
                    System.out.println("Going back to the previous page...");
                    return; // Go back to the previous page or menu

                default:
                    System.out.println("Invalid choice! Please select a valid option.");
                    break;
            }
        }
    }

// Placeholder method for saving text content to the database
private static boolean saveTextToDatabase(String section_id,String block_num,String content) {
	 Connection conn = null;
	 String insertSectionQuery = "INSERT INTO ContentBlocks (section_id,content_block_number,content_type,content_data,createdby) VALUES (?, ?, ?, ?,?)";

    System.out.println("Saving text content to the database: " + content);
    try {
   	    // Establish a connection to the database
   	    conn = DBConnect.getConnection();
   	    if (conn == null) {
   	        System.out.println("Failed to establish a database connection.");
   	        return false;
   	    }
   	    PreparedStatement preparedStatement = conn.prepareStatement(insertSectionQuery);
   	    
   	    preparedStatement.setString(1, section_id);
   	    preparedStatement.setString(2, block_num);
   	    preparedStatement.setString(4, content);
   	    preparedStatement.setString(3,"text");
   	    preparedStatement.setString(4,"Faculty");
   	    int rowsInserted = preparedStatement.executeUpdate();
   	    return rowsInserted > 0;
    } catch (SQLException e) {
   		if(e.toString().contains("Duplicate"))
   		{
   			System.out.println("Blockid already exists in the textbook re enter the details");
   			AddNewContentBlock.addNewContentBlock(new Scanner(System.in),section_id);	
   		}
   	    e.printStackTrace();
   	    return false;
   	} 
    finally {
   	    if (conn != null) {
   	        try {
   	            conn.close();
   	            System.out.println("Database connection closed.");
   	        } catch (SQLException e) {
   	            e.printStackTrace();
   	        }
   	    }
   	}
   	    

}}

