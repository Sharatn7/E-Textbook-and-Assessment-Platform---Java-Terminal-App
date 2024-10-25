import java.util.Scanner;

public class ModifyContentBlock {

    public static void modifyContentBlock(Scanner scanner) {
        System.out.print("Enter Content Block ID to Modify: ");
        String contentBlockId = scanner.nextLine();

        boolean inModifyContentBlock = true;
        while (inModifyContentBlock) {
            System.out.println("\n=== Modify Content Block ===");
            System.out.println("1. Hide Content Block");
            System.out.println("2. Delete Content Block");
            System.out.println("3. Add Text");
            System.out.println("4. Add Picture");
            System.out.println("5. Hide Activity");
            System.out.println("6. Delete Activity");
            System.out.println("7. Add Activity");
            System.out.println("8. Go Back");
            System.out.print("Enter choice (1-8): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    HideContentBlock.hideContentBlock(scanner, contentBlockId);  // Hide Content Block functionality
                    break;
                case 2:
                    DeleteContentBlock.deleteContentBlock(scanner, contentBlockId);  // Delete Content Block functionality
                    break;
                case 3:
                    AddText.addText(scanner, contentBlockId);  // Add Text functionality
                    break;
                case 4:
                    AddPicture.addPicture(scanner, contentBlockId);  // Add Picture functionality
                    break;
                case 5:
                    HideActivity.hideActivity(scanner, contentBlockId);  // Hide Activity functionality
                    break;
                case 6:
                    DeleteActivity.deleteActivity(scanner, contentBlockId);  // Delete Activity functionality
                    break;
                case 7:
                    AddActivity.addActivity(scanner, contentBlockId);  // Add Activity functionality
                    break;
                case 8:
                    System.out.println("Returning to previous menu...");
                    inModifyContentBlock = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
    }
}
