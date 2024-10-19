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
                    createETextbook();
                    break;
                case 3:
                    modifyETextbook();
                    break;
                case 4:
                    createNewActiveCourse();
                    break;
                case 5:
                    createNewEvaluationCourse();
                    break;
                case 6:
                    System.out.println("Logging out... Returning to Homepage.");
                    isLoggedIn = false;  // Break the loop and logout
                    break;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
        scanner.close();
    }

    // Placeholder methods for each option. You can replace these with actual logic.
    public static void createFacultyAccount() {
        System.out.println("Navigating to: Create a Faculty Account Page");
        // Add logic here to create a faculty account
    }

    public static void createETextbook() {
        System.out.println("Navigating to: Create E-textbook Page");
        // Add logic here to create an e-textbook
    }

    public static void modifyETextbook() {
        System.out.println("Navigating to: Modify E-textbooks Page");
        // Add logic here to modify an e-textbook
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
