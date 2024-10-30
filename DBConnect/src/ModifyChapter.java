import java.util.*;
public class ModifyChapter {
	private static Scanner scanner = new Scanner(System.in);
	public static void modifyChapter(int tb_id) { 
	System.out.print("Enter the Chapter id");
    String chapter_id = "chap"+scanner.nextInt();
    scanner.nextLine();
	while (true) {
		 System.out.println("\n=== Modify Chapter Menu ===");
         System.out.println("1. Add New Section");
         System.out.println("2. Modify Existing Section");
         System.out.println("3. Go Back");
         System.out.println("4. Landing Page");
         System.out.print("Enter your choice (1-4): ");
         int choice = scanner.nextInt();
         scanner.nextLine(); // Consume the newline

         switch (choice) {
             case 1:
                 // Step 3: Redirect to Add New Section
            	 CreateSection.createNewSection(tb_id,chapter_id);
                 break;
             case 2:
                 // Step 4: Redirect to Modify Existing Section
            	 ModifySection.modifySection(tb_id,chapter_id);
                 break;
             case 3:
                 // Discard the input and go back to the previous page
                 System.out.println("Going back to the previous page...");
                 ModifyTextbook.modifyTextbook();
                 return; // Go back to the previous page or menu
             case 4:
                 // Discard the input and go back to the landing page
                 System.out.println("Going back to the landing page...");
                 AdminLandingPage.main(new String[0]);
                 return; // Go back to the landing page
             default:
                 System.out.println("Invalid choice! Please select a valid option.");
                 break;
         }
     }
    }
}
