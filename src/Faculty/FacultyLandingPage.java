package Faculty;
import homepage.*;
import java.util.Scanner;

public class FacultyLandingPage {

    public static void facultyLandingMenu(Scanner scanner, String facultyId) {
        boolean continueSession = true;

        while (continueSession) {
            System.out.println("\n=== Faculty Landing Page ===");
            System.out.println("1. Go to Active Course");
            System.out.println("2. Go to Evaluation Course");
            System.out.println("3. View Courses");
            System.out.println("4. Change Password");
            System.out.println("5. Logout");
            System.out.print("Enter choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ActiveCourseMenu.activeCourseMenu(scanner);  // Call Active Course Menu
                    break;
                case 2:
                    EvaluationCourseMenu.evaluationCourseMenu(scanner);  // Call Evaluation Course Menu
                    break;
                case 3:
                    ViewCourses.viewCourses(scanner, facultyId);  // Integrated DB to view courses
                    break;
                case 4:
                    ChangePassword.changePassword(scanner, facultyId);  // Integrated DB to change password
                    break;
                case 5:
                    System.out.println("Logging out...");
                    continueSession = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
    }

    }
