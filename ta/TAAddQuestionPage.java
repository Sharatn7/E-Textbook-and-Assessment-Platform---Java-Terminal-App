import java.util.Scanner;

public class TAAddQuestionPage {

    public void showAddQuestionPage(Scanner scanner, String activityId, String chapterId, String courseId) {
        boolean exit = false;

        while (!exit) {

            System.out.print("Enter Question ID: ");
            String questionId = scanner.nextLine();

            System.out.print("Enter Question Text: ");
            String questionText = scanner.nextLine();

            System.out.print("Enter Option 1: ");
            String option1 = scanner.nextLine();

            System.out.print("Enter Option 1 Explanation: ");
            String option1Explanation = scanner.nextLine();

            System.out.print("Enter Option 2: ");
            String option2 = scanner.nextLine();

            System.out.print("Enter Option 2 Explanation: ");
            String option2Explanation = scanner.nextLine();

            System.out.print("Enter Option 3: ");
            String option3 = scanner.nextLine();

            System.out.print("Enter Option 3 Explanation: ");
            String option3Explanation = scanner.nextLine();

            System.out.print("Enter Option 4: ");
            String option4 = scanner.nextLine();

            System.out.print("Enter Option 4 Explanation: ");
            String option4Explanation = scanner.nextLine();

            System.out.print("Enter the Correct Answer (Option 1/2/3/4): ");
            String correctAnswer = scanner.nextLine();

            System.out.println("Menu:");
            System.out.println("1. Save");
            System.out.println("2. Cancel");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Saving the question to Activity ID: " + activityId);
                    System.out.println("Question ID: " + questionId);
                    System.out.println("Question: " + questionText);
                    System.out.println("Option 1: " + option1 + " - " + option1Explanation);
                    System.out.println("Option 2: " + option2 + " - " + option2Explanation);
                    System.out.println("Option 3: " + option3 + " - " + option3Explanation);
                    System.out.println("Option 4: " + option4 + " - " + option4Explanation);
                    System.out.println("Correct Answer: " + correctAnswer);

                    System.out.println("Question saved successfully.");
                    exit = true;
                    TAAddActivityPage addActivityPage = new TAAddActivityPage();
                    addActivityPage.showAddActivityPage(scanner, chapterId, courseId);
                }
                case 2 -> {

                    System.out.println("Cancelling and returning to Activity Page...");
                    exit = true;
                    TAAddActivityPage addActivityPage = new TAAddActivityPage();
                    addActivityPage.showAddActivityPage(scanner, chapterId, courseId);
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }
}