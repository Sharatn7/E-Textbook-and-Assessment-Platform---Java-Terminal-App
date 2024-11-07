package TA;
import java.util.Scanner;

public class TALandingPage {

    public void showLandingPage(Scanner scanner) {
        boolean stayOnLandingPage = true;

        while (stayOnLandingPage) {

            System.out.println("TA Landing Page Menu:");
            System.out.println("1. Go to Active Course");
            System.out.println("2. View Courses");
            System.out.println("3. Change Password");
            System.out.println("4. Logout");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> { 
                    System.out.println("Redirecting to Active Course...");
                    TAActiveCoursePage activeCoursePage = new TAActiveCoursePage();
                    activeCoursePage.showActiveCoursePage(scanner);
                }
                case 2 -> {
                    System.out.println("Viewing Courses...");
                    TAViewCoursesPage viewCoursesPage = new TAViewCoursesPage();
                    viewCoursesPage.showViewCoursesPage(scanner);
                }
                case 3 -> {
                    System.out.println("Changing Password...");
                    TAChangePasswordPage changePasswordPage = new TAChangePasswordPage();
                    changePasswordPage.showChangePasswordPage(scanner);
                }
                case 4 -> {
                    System.out.println("Logging out...");
                    stayOnLandingPage = false;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
        System.out.println("You have been logged out.");
    }
}
