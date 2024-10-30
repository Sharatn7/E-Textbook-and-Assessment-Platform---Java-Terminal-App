import java.util.Scanner;

public class ModifyContentBlock {
	private static Scanner scanner = new Scanner(System.in);
	public static void modifyContentBlock(int textbookId, String chapter_id, String sectionId) {
		System.out.print("Enter Content Block Num: ");
        String contentBlockNum = scanner.nextLine();
        
        
        String blockId = AddActivity.getBlockId(sectionId,contentBlockNum);
        
        if(blockId==null ) {
        	System.out.println("Enter an Existing Block Num to Modify");
        	modifyContentBlock( textbookId,  chapter_id,  sectionId);
        }
        
//        deleteBlock(blockId);
        
        
        while (true) {
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
                	AddText.addText(textbookId,chapter_id,sectionId,contentBlockNum);
                    break;
                case 2:
                    // Redirect to Add Picture
                	AddPicture.addPicture(textbookId,chapter_id,sectionId,contentBlockNum);
                    break;
                case 3:
                    // Redirect to Add New Activity
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
	
	}
