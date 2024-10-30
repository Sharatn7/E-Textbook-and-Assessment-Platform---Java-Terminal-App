import java.sql.*;
import java.util.Scanner;

public class CreateFaculty {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    	boolean isLoggedIn = true;
    	while (isLoggedIn) {
            System.out.println("\n=== User Account Management ===");
            System.out.println("1. Create a New User Account");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    createNewUserAccount();
                    break;
                case 2:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
    }

    public static void createNewUserAccount() {
        // Step 1: Take user input for account details
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        // Placeholder for other details (like email, etc.)
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String pass = scanner.nextLine();

        // Step 2: Display Menu after entering details
        while (true) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Add User");
            System.out.println("2. Go Back");
            System.out.print("Enter your choice (1-2): ");
            int menuChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (menuChoice) {
                case 1:
                    // Step 3: Add user to the database
                    if (saveUserToDatabase(firstName, lastName, email, pass)) {
                        System.out.println("User added successfully!");
                    } else {
                        System.out.println("Error occurred while adding the user.");
                    }
                    return; // Go back to the previous menu

                case 2:
                    System.out.println("Going back to the previous page...");
                    return; // Go back to the previous page

                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
    }

    // This method connects to the database and saves the user record
    public static boolean saveUserToDatabase(String firstName, String lastName, String email, String pass) {
    	Connection conn = null;
        String insertQuery = "INSERT INTO Users (first_name, last_name, email, password,role) VALUES (?, ?, ?, ?,?)";

        try {
            conn = DBConnect.getConnection();
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return false;
            }

            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, pass);
            preparedStatement.setString(5, "Faculty");

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}