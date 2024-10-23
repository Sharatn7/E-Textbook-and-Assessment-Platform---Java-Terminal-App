import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAApplication {

    public void start(Scanner scanner) {
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("Please enter your TA credentials:");
            System.out.print("User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.println("Menu:");
            System.out.println("1. Sign-In");
            System.out.println("2. Go Back");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    if (validateTACredentials(userId, password)) {
                        System.out.println("TA Login Successful! Redirecting to Landing Page...");
                        loggedIn = true;

                        TALandingPage landingPage = new TALandingPage(userId);
                        landingPage.showLandingPage(scanner);
                        break;
                    } else {
                        System.out.println("Login Incorrect. Please try again.");
                    }
                }
                case 2 -> {
                    System.out.println("Going back to Home Page.");
                    break;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private boolean validateTACredentials(String userId, String password) {

        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                String sql = "SELECT * FROM User WHERE user_id = ? AND password = ? AND role = 'TA'";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userId);
                pstmt.setString(2, password);

                ResultSet rs = pstmt.executeQuery();
                return rs.next();
            } else {
                System.out.println("Database connection failed.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
