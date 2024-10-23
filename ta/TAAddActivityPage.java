import java.util.Scanner;

public class TAAddActivityPage {

    public void showAddActivityPage(Scanner scanner, String chapterId, String courseId) {
        boolean exit = false;

        while (!exit) {

            System.out.print("Enter Unique Activity ID: ");
            String activityId = scanner.nextLine();


            System.out.println("Menu:");
            System.out.println("1. Add Question");
            System.out.println("2. Go Back");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Adding a Question to Activity ID: " + activityId + " in Chapter ID: " + chapterId + " of Course ID: " + courseId);
                    TAAddQuestionPage addQuestionPage = new TAAddQuestionPage();
                    addQuestionPage.showAddQuestionPage(scanner, activityId, chapterId, courseId);
                    break;
                }
                case 2 -> {
                    System.out.println("Returning to Modify Chapter Page...");
                    exit = true;
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }
}
