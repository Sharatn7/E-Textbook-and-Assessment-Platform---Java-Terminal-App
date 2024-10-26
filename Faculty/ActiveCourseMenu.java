import java.util.Scanner;

public class ActiveCourseMenu {

    public static void activeCourseMenu(Scanner scanner) {
        System.out.println("Enter Course ID: ");
        String courseId = scanner.nextLine();

        boolean inActiveCourse = true;
        while (inActiveCourse) {
            System.out.println("\n=== Active Course Menu ===");
            System.out.println("1. View Worklist");
            System.out.println("2. Approve Enrollment");
            System.out.println("3. View Students");
            System.out.println("4. Add New Chapter");
            System.out.println("5. Modify Chapters");
            System.out.println("6. Add TA");
            System.out.println("7. Go Back");
            System.out.print("Enter choice (1-7): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    ViewWorklist.viewWorklist(scanner);  // Now connected to the database
                    break;
                case 2:
                    ApproveEnrollment.approveEnrollment(scanner);  // Now connected to the database
                    break;
                case 3:
                    ViewStudents.viewStudents(scanner);  // Now connected to the database
                    break;
                case 4:
                    AddNewChapter.addNewChapter(scanner);
                    break;
                case 5:
                    ModifyChapters.modifyChapters(scanner);
                    break;
                case 6:
                    AddTA.addTA(scanner);
                    break;
                case 7:
                    inActiveCourse = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
