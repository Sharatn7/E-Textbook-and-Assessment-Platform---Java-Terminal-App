package Admin;
import java.util.Scanner;
import java.sql.*;

public class ModifyContentBlock {
	private static Scanner scanner = new Scanner(System.in);
	public static void modifyContentBlock(String textbookId, String chapter_id, String sectionId) {
		System.out.print("Enter Content Block Num: ");
        String contentBlockNum = scanner.nextLine();
        
        
        String blockId = AddActivity.getBlockId(sectionId,contentBlockNum);
        
        
        
        
        while (true) {
        	if(blockId==null ) {
            	System.out.println("Enter an Existing Block Num to Modify");
            	modifyContentBlock( textbookId,  chapter_id,  sectionId);
            }
            
            
            System.out.println("\n=== Modify Content Block Menu ===");
            System.out.println("1. Add Text");
            System.out.println("2. Add Picture");
            System.out.println("3. Add New Activity");
            System.out.println("4. Go Back");
            System.out.println("5. Landing Page");
            System.out.print("Enter your choice (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Redirect to Add Text
                	deleteBlock(blockId);
                	AddText.addText(textbookId,chapter_id,sectionId,contentBlockNum);
                    break;
                case 2:
                    // Redirect to Add Picture
                	deleteBlock(blockId);
                	AddPicture.addPicture(textbookId,chapter_id,sectionId,contentBlockNum);
                    break;
                case 3:
                    // Redirect to Add New Activity
                	deleteBlock(blockId);
                	AddActivity.addActivity(textbookId,chapter_id,sectionId,contentBlockNum);
                    break;
                case 4:
                    // Discard the input and go back to the previous page
                    System.out.println("Going back to the previous page...");
                   
                    return;
                case 5:
                    // Discard the input and go back to the landing page
                    System.out.println("Going back to the landing page...");
                    AdminLandingPage.main(new String[0]);
                    return;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
                    break;
            }
        }
    }
	private static void deleteBlock(String blockId) {
        String deleteQuery = "DELETE FROM ContentBlocks WHERE content_block_id = ?";

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, blockId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                //System.out.println("Content block deleted successfully.");
            } else {
                //System.out.println("Content block deletion failed. Block may not exist.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	
	}
