package Admin;
import java.util.Scanner;

public class AdminLandingPage {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isLoggedIn = true;

        while (isLoggedIn) {
            // Display the menu
            System.out.println("\n=== Admin Landing Page ===");
            System.out.println("1. Create a Faculty Account");
            System.out.println("2. Create E-textbook");
            System.out.println("3. Modify E-textbooks");
            System.out.println("4. Create New Active Course");
            System.out.println("5. Create New Evaluation Course");
            System.out.println("6. Logout");
            System.out.print("Enter choice (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character left after nextInt()

            switch (choice) {
                case 1:
                	CreateFaculty.createNewUserAccount();
                    break;
                case 2:
                    CreateTextbook.createTextbook();
                    break;
                case 3:
                    ModifyTextbook.modifyTextbook();
                    break;
                case 4:
                    CreateActiveCourse.createNewActiveCourse();
                    break;
                case 5:
                	CreateEvaluationCourse.createNewEvaluationCourse();
                    break;
                case 6:
                    System.out.println("Logging out... Returning to Homepage.");
                    isLoggedIn = false;  // Break the loop and logout
                    return;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
        scanner.close();
    }

    
    public static void createNewActiveCourse() {
        System.out.println("Navigating to: Create New Active Course Page");
        // Add logic here to create a new active course
    }

    public static void createNewEvaluationCourse() {
        System.out.println("Navigating to: Create New Evaluation Course Page");
        // Add logic here to create a new evaluation course
    }
}
