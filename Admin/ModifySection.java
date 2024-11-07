package Admin;
import java.util.Scanner;

public class ModifySection {
	private static Scanner scanner = new Scanner(System.in);
	public static void modifySection(String tb_id,String chapter_id) {

//        
        System.out.print("Enter Section Number: ");
        String section_num = scanner.next();
        
        
//        String chapterId = CreateChapter.getChapterId(tb_id,chapter_num);
    	String sectionId = CreateSection.getSectionId(chapter_id,section_num);
        
        if(chapter_id==null || sectionId==null) {
        	System.out.println("Enter an Existing ChapterId and section Id to Modify");
        	modifySection(tb_id,chapter_id);
        }
        

        
        
        while (true) {
            System.out.println("\n=== Modify Section Menu ===");
            System.out.println("1. Add New Content Block");
            System.out.println("2. Modify Content Block");
            System.out.println("3. Go Back");
            System.out.println("4. Landing Page");
            System.out.print("Enter your choice (1-4): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Redirect to Add New Content Block
                	
                    CreateContentBlock.createNewContentBlock(tb_id, chapter_id, sectionId);
                    break;
                case 2:
                    // Redirect to Modify Content Block
                	
                    ModifyContentBlock.modifyContentBlock(tb_id, chapter_id, sectionId);
                    break;
                case 3:
                    // Discard the input and go back to the previous page
                    System.out.println("Going back to the previous page...");
                    return;
                case 4:
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
