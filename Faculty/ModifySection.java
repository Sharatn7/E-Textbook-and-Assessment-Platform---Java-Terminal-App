import java.util.Scanner;

public class ModifySection {

    public static void modifySection(Scanner scanner) {
        System.out.print("Enter Section Number to Modify: ");
        String sectionNumber = scanner.nextLine();

        boolean inModifySection = true;
        while (inModifySection) {
            System.out.println("\n=== Modify Section ===");
            System.out.println("1. Hide Section");
            System.out.println("2. Delete Section");
            System.out.println("3. Add New Content Block");
            System.out.println("4. Modify Content Block");
            System.out.println("5. Go Back");
            System.out.print("Enter choice (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    HideSection.hideSection(scanner, sectionNumber);  // Hide Section functionality
                    break;
                case 2:
                    DeleteSection.deleteSection(scanner, sectionNumber);  // Delete Section functionality
                    break;
                case 3:
                    AddNewContentBlock.addNewContentBlock(scanner);  // Add New Content Block functionality
                    break;
                case 4:
                    ModifyContentBlock.modifyContentBlock(scanner);  // Modify Content Block functionality
                    break;
                case 5:
                    System.out.println("Returning to previous menu...");
                    inModifySection = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
}
