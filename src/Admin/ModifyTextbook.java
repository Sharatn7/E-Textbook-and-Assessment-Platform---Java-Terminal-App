package Admin;

import java.sql.*;
import java.util.*;
public class ModifyTextbook {
	private static Scanner scanner = new Scanner(System.in);
	public static void modifyTextbook() {
		System.out.print("Enter a unique E-textbook ID (integer): ");
        int tb_id = scanner.nextInt();
        
        if(checkTextbookExists(tb_id)==false) {
        	System.out.println("Enter existing tbid to modify");
        	modifyTextbook();
        }
        
        while (true) {
            System.out.println("\n=== Modify E-textbook Menu ===");
            System.out.println("1. Add New Chapter");
            System.out.println("2. Modify Existing Chapter");
            System.out.println("3. Go Back");
            System.out.println("4. Landing Page");
            System.out.print("Enter your choice (1-4): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
         

            switch (choice) {
                case 1:
                    // Step 3: Redirect to Add New Chapter
                	CreateChapter.createNewChapter(tb_id);
                    break;
                case 2:
                    // Step 4: Redirect to Modify Existing Chapter
                    ModifyChapter.modifyChapter(tb_id);
                    break;
                case 3:
                    // Discard the input and go back to the previous page
                    System.out.println("Going back to the previous page...");
                    
                    return; // Return to the previous page or menu
                case 4:
                    // Discard the input and go back to the landing page
                    System.out.println("Going back to the landing page...");
                    AdminLandingPage.main(new String[0]);
                    return; // Return to the landing page
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
                    break;
            }
        }
    
}
	
	
	private static boolean checkTextbookExists(int textbookId) {
		 Connection conn = null;
        String query = "SELECT * FROM Textbooks WHERE textbook_id = ?";
        try {
            conn = DBConnect.getConnection(); // Assuming DBConnect class has been implemented for DB connection
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return false;
            }

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, textbookId);

            ResultSet resultSet = statement.executeQuery();
                return resultSet.next(); // Returns true if a matching textbook_id is found
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Returns false if no match found or if an error occurs
    }
}
