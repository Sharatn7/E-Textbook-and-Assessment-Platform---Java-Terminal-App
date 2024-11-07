package TA;
import java.util.Scanner;

public class TAAddNewContentBlockPage {

    // Global variable to track content block visibility
    public static boolean isContentBlockVisible = true;

    public void showAddNewContentBlockPage(Scanner scanner, String sectionId) {
        boolean exit = false;
        boolean isContentBlockCreated = false;

        while (!exit) {
            System.out.print("Enter Content Block ID: ");
            String contentBlockNumber = scanner.nextLine();

            System.out.println("Menu:");
            System.out.println("1. Add Text");
            System.out.println("2. Add Picture");
            System.out.println("3. Add Activity");
            System.out.println("4. Hide Activity");
            System.out.println("5. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Adding Text to Content Block ID: " + contentBlockNumber);
                    TAAddTextPage addTextPage = new TAAddTextPage();
                    addTextPage.showAddTextPage(scanner, contentBlockNumber, sectionId);
                    isContentBlockCreated = true;
                }
                case 2 -> {
                    System.out.println("Adding Picture to Content Block ID: " + contentBlockNumber);
                    TAAddPicturePage addPicturePage = new TAAddPicturePage();
                    addPicturePage.showAddPicturePage(scanner, contentBlockNumber, sectionId);
                    isContentBlockCreated = true;
                }
                case 3 -> {
                    System.out.println("Adding Activity to Content Block ID: " + contentBlockNumber);
                    TAAddActivityPage addActivityPage = new TAAddActivityPage();
                    addActivityPage.showAddActivityPage(scanner, contentBlockNumber, sectionId);
                    isContentBlockCreated = true;
                }
                case 4 -> {
                    System.out.println("Hiding the need to create activity block " + contentBlockNumber);
                    isContentBlockVisible = true;
                }
                case 5 -> {
                    if (!isContentBlockCreated) {
                        System.out.println("Content block not created.");
                        isContentBlockVisible = false;
                    }
                    System.out.println("Returning to Previous Page...");
                    exit = true;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }
}
