package student;
import homepage.*;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to the E-Textbook Platform");
            System.out.println("1. Admin");
            System.out.println("2. Faculty");
            System.out.println("3. TA");
            System.out.println("4. Student");
            System.out.println("5. Exit");
            System.out.print("Enter choice (1-5): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    //admin;
                    break;
                case 2:
//                    faculty;
                    break;
                case 3:
//                    ta;
                    break;
                case 4:
//                    student;
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
   
    }