import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HomePage {
    public static String currentTAId = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DBConnect.getConnection();
        boolean exit = false;

        while (!exit) {
            System.out.println("Welcome to the Home Page");
            System.out.println("Menu:");
            System.out.println("1. Admin Login");
            System.out.println("2. Faculty Login");
            System.out.println("3. TA Login");
            System.out.println("4. Student Login");
            System.out.println("5. Execute Queries");
            System.out.println("6. Exit");

            System.out.print("Enter your choice (1-6): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    if (validateCredentials(scanner, "Admin", connection)) {
                        System.out.println("Admin Login Successful! Redirecting to Admin Page...");
                    } else {
                        System.out.println("Invalid Admin credentials.");
                    }
                }
                case 2 -> {
                    if (validateCredentials(scanner, "Faculty", connection)) {
                        System.out.println("Faculty Login Successful! Redirecting to Faculty Page...");
                    } else {
                        System.out.println("Invalid Faculty credentials.");
                    }
                }
                case 3 -> {
                    if (validateCredentials(scanner, "TA", connection)) {
                        System.out.println("TA Login Successful! Redirecting to TA Landing Page...");
                        //TALandingPage landingPage = new TALandingPage();
                        //landingPage.showLandingPage(scanner);
                    } else {
                        System.out.println("Invalid TA credentials.");
                    }
                }
                case 4 -> {
                    if (validateCredentials(scanner, "Student", connection)) {
                        System.out.println("Student Login Successful! Redirecting to Student Page...");
                    } else {
                        System.out.println("Invalid Student credentials.");
                    }
                }
                case 5 -> {
                    QueryExecutionPage queryPage = new QueryExecutionPage();
                    queryPage.showQueryPage(scanner, connection);
                }
                case 6 -> {
                    System.out.println("Exiting the application. Goodbye!");
                    exit = true;
                }
                default -> System.out.println("Invalid choice. Please select between 1-6.");
            }
        }
    }

    private static boolean validateCredentials(Scanner scanner, String role, Connection connection) {
        System.out.print("Enter your User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine();

        String sql = "SELECT user_id FROM Users WHERE user_id = ? AND password = ? AND role = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            statement.setString(2, password);
            statement.setString(3, role);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    if ("TA".equals(role)) {
                        currentTAId = userId;
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }
}
