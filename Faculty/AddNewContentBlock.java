package Faculty;
import homepage.*;
import java.util.Scanner;


public class AddNewContentBlock {

    public static void addNewContentBlock(Scanner scanner,String sectionId) {
        
    	

        // Step 2: Display menu after entering Content Block ID
        while (true) {
        	System.out.print("Enter Unique Content Block Num: ");
            String block_num = scanner.next();
            
            System.out.println("\n=== Add New Content Block Menu ===");
            System.out.println("1. Add Text");
            System.out.println("2. Add Picture");
            System.out.println("3. Add Activity");
            System.out.println("4. Go Back");
           
            System.out.print("Enter your choice (1-4): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Step 3: Redirect to Add Text
                    AddText.addText(scanner,sectionId,block_num);
                    break;
                case 2:
                    // Step 4: Redirect to Add Picture
               	 AddPicture.addPicture(scanner,sectionId,block_num);
                    break;
                case 3:
                    // Step 5: Redirect to Add Activity
                    AddActivity.addActivity(scanner,sectionId,block_num);
                    break;
                case 4:
                    // Discard the input and go back to the previous page
                    System.out.println("Going back to the previous page...");
                    return; // Go back to the previous page or menu
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
                    break;
            }
        }
    }
	 
	
}
