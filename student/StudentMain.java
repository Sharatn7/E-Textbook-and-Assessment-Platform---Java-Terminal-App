import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Statement;

public class StudentMain {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. Enroll in a course");
            System.out.println("2. Sign-In");
            System.out.println("3. Go Back");

            System.out.print("Choose option (1-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    enrollInCourse();
                    break;
                case 2:
                    signIn();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    
    private static void signIn() {
        System.out.println("\n--- Sign In ---");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.println("1. Sign-In");
        System.out.println("2. Go Back");
        System.out.print("Choose option (1-2): ");
        int option = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (option == 1) {

            boolean isAuthenticated = checkCredentials(userId, password, "STUDENT");

            if (isAuthenticated) {
                System.out.println("Login successful.");
                UserLandingPage landingPage = new UserLandingPage(userId);
                landingPage.displayMenu();
            } else {
                System.out.println("Login Incorrect. Please try again.");
            }
        } else {
            System.out.println("Going back to main menu.");
        }
   
    }

    private static boolean checkCredentials(String userId, String password, String role) {

    	String sql = "SELECT COUNT(*) FROM User WHERE user_id = ? AND password = ? AND role = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            pstmt.setString(3, role);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count is greater than 0, credentials are valid
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if any exception occurs or no match is found
    }

    private static void enrollInCourse() {
        System.out.println("\n--- Enroll in Course ---");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Course Token: ");
        String token = scanner.nextLine();

        System.out.println("1. Enroll");
        System.out.println("2. Go Back");
        System.out.print("Choose option (1-2): ");
        int option = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (option == 1) {
            if (enrollStudent(firstName, lastName, email, token)) {
                System.out.println("Enrollment request recorded and processed.");
            } else {
                System.out.println("Failed to enroll. Please check the details and try again.");
            }
        } else {
            System.out.println("Going back to main menu.");
        }
    }

    private static boolean enrollStudent(String firstName, String lastName, String email, String token) {
        // Step 1: Check if the course token is valid and fetch course details
        String courseCheckSql = "SELECT course_id, current_enrollment_count, course_capacity FROM active_course WHERE token = ?";
        String courseId = null;
        int currentEnrollment = 0;
        int capacity = 0;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmtCourse = conn.prepareStatement(courseCheckSql)) {
            pstmtCourse.setString(1, token);
            ResultSet rs = pstmtCourse.executeQuery();
            if (rs.next()) {
                courseId = rs.getString("course_id");
                currentEnrollment = rs.getInt("current_enrollment_count");
                capacity = rs.getInt("course_capacity");
            } else {
                return false; // Token not valid
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Step 2: Check if user exists and create a new user if not
        String userId = createUserIfNotExist(firstName, lastName, email);
        if (userId == null) {
            return false; // Failed to create or retrieve user
        }

        // Step 3: Enroll if there's room
        if (currentEnrollment < capacity) {
            String enrollSql = "INSERT INTO Enrollment (course_id, user_id) VALUES (?, ?)";
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement pstmtEnroll = conn.prepareStatement(enrollSql)) {
                pstmtEnroll.setString(1, courseId);
                pstmtEnroll.setString(2, userId);
                pstmtEnroll.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("Sorry, the course is at full capacity.");
            return false;
        }
    }

    private static String createUserIfNotExist(String firstName, String lastName, String email) {
        String userCheckSql = "SELECT user_id FROM User WHERE email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmtCheck = conn.prepareStatement(userCheckSql)) {
            pstmtCheck.setString(1, email);
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next()) {
                return rs.getString("user_id"); // User exists, return existing user_id
            } else {
                // Create new user
                String createUserSql = "INSERT INTO User (first_name, last_name, email, role, password) VALUES (?, ?, ?, 'STUDENT', 'defaultpass')";
                PreparedStatement pstmtCreate = conn.prepareStatement(createUserSql, Statement.RETURN_GENERATED_KEYS);
                pstmtCreate.setString(1, firstName);
                pstmtCreate.setString(2, lastName);
                pstmtCreate.setString(3, email);
                pstmtCreate.executeUpdate();
                ResultSet keys = pstmtCreate.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getString(1); // Return new user_id
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user creation fails
    }

    
}
