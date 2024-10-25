import java.util.Scanner;

public class EvaluationCourseMenu {

    public static void evaluationCourseMenu(Scanner scanner) {
        System.out.println("Enter Course ID: ");
        String courseId = scanner.nextLine();

        // No SQL operation needed here, so no try-catch block for SQLException
        boolean inEvaluationCourse = true;
        while (inEvaluationCourse) {
            System.out.println("\n=== Evaluation Course Menu ===");
            System.out.println("1. Add New Chapter");
            System.out.println("2. Modify Chapters");
            System.out.println("3. Go Back");
            System.out.print("Enter choice (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    AddNewChapter.addNewChapter(scanner);
                    break;
                case 2:
                    ModifyChapters.modifyChapters(scanner);
                    break;
                case 3:
                    inEvaluationCourse = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
