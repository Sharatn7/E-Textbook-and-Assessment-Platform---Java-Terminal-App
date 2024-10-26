import java.util.Scanner;

public class ViewCourses {

    public static void viewCourses(Scanner scanner, String facultyId) {
        System.out.println("=== Assigned Courses ===");
        String[] courses = {"Course 101: Data Structures", "Course 102: Algorithms", "Course 103: Database Systems"};

        for (int i = 0; i < courses.length; i++) {
            System.out.println((i + 1) + ". " + courses[i]);
        }

        System.out.println("\n1. Go Back");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (choice == 1) {
            System.out.println("Returning to Faculty Landing Page...");
        } else {
            System.out.println("Invalid choice.");
        }
    }
}
