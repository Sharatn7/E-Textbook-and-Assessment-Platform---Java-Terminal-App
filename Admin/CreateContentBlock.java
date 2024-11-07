package Admin;
import java.util.Scanner;
public class CreateContentBlock {
	private static Scanner scanner = new Scanner(System.in);
	 public static void createNewContentBlock(String tb_id,String chap_id,String section_id) {
		 
         // Step 2: Display menu after entering Content Block ID
         while (true) {
        	 
        	 System.out.println("Block management for "+section_id);
        	 
        	 System.out.print("Enter Unique Content Block Num: ");
             String block_num = scanner.next();

             System.out.println("\n=== Add New Content Block Menu ===");
             System.out.println("1. Add Text");
             System.out.println("2. Add Picture");
             System.out.println("3. Add Activity");
             System.out.println("4. Go Back");
             System.out.println("5. Landing Page");
             System.out.print("Enter your choice (1-5): ");
             int choice = scanner.nextInt();
             scanner.nextLine(); // Consume the newline

             switch (choice) {
                 case 1:
                     // Step 3: Redirect to Add Text
                     AddText.addText(tb_id,chap_id,section_id,block_num);
                     break;
                 case 2:
                     // Step 4: Redirect to Add Picture
                	 AddPicture.addPicture(tb_id,chap_id,section_id,block_num);
                     break;
                 case 3:
                     // Step 5: Redirect to Add Activity
                     AddActivity.addActivity(tb_id,chap_id,section_id,block_num);
                     break;
                 case 4:
                     // Discard the input and go back to the previous page
                     System.out.println("Going back to the previous page...");
                     return; // Go back to the previous page or menu
                 case 5:
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
	 
	
	  
}
