package student;
import homepage.*;
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
                    if(Login.authenticateUser())
                    {UserLandingPage landingPage = new UserLandingPage();
                    landingPage.displayMenu();}
                    
                   
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
        String courseCheckSql = "SELECT course_id, enrollmentCount, capacity FROM activeCourse WHERE unique_token = ?";
        String courseId = null;
        int currentEnrollment = 0;
        int capacity = 0;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmtCourse = conn.prepareStatement(courseCheckSql)) {
            pstmtCourse.setString(1, token);
            ResultSet rs = pstmtCourse.executeQuery();
            if (rs.next()) {
                courseId = rs.getString("course_id");
                currentEnrollment = rs.getInt("enrollmentCount");
                capacity = rs.getInt("capacity");
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

        // Step 2.5: Check if the student is already enrolled in the course
        if (isStudentAlreadyEnrolled(userId, courseId)) {
            System.out.println("You are already enrolled in this course.");
            return false;
        }

        // Step 3: Enroll if there's room
        if (currentEnrollment < capacity) {
            String enrollSql = "INSERT INTO Enrollments (course_id, student_id) VALUES (?, ?)";
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

    private static boolean isStudentAlreadyEnrolled(String userId, String courseId) {
        String checkEnrollmentSql = "SELECT 1 FROM Enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkEnrollmentSql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // If a record is found, the student is already enrolled
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Assume not enrolled if there's an error
        }
    }


    /*private static String createUserIfNotExist(String firstName, String lastName, String email) {
        String userCheckSql = "SELECT user_id FROM Users WHERE email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmtCheck = conn.prepareStatement(userCheckSql)) {
            pstmtCheck.setString(1, email);
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next()) {
                return rs.getString("user_id"); // User exists, return existing user_id
            } else {
                // Create new user
                String createUserSql = "INSERT INTO Users (first_name, last_name, email) VALUES (?, ?, ?)";
                PreparedStatement pstmtCreate = conn.prepareStatement(createUserSql, Statement.RETURN_GENERATED_KEYS);
                pstmtCreate.setString(1, firstName);
                pstmtCreate.setString(2, lastName);
                pstmtCreate.setString(3, email);
                pstmtCreate.executeUpdate();
                ResultSet keys = pstmtCreate.getGeneratedKeys();
                System.out.println(keys.next());
                if (keys.next()) {
                    return keys.getString(1); // Return new user_id
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user creation fails
    }*/
    private static String createUserIfNotExist(String firstName, String lastName, String email) {
        // Check if the user already exists
        String userCheckSql = "SELECT user_id FROM Users WHERE email = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmtCheck = conn.prepareStatement(userCheckSql)) {
            pstmtCheck.setString(1, email);
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next()) {
                return rs.getString("user_id"); // User exists, return existing user_id
            } else {
                // User does not exist, create a new user
                String createUserSql = "INSERT INTO Users (first_name, last_name, email) VALUES (?, ?, ?)";
                try (PreparedStatement pstmtCreate = conn.prepareStatement(createUserSql)) {
                    pstmtCreate.setString(1, firstName);
                    pstmtCreate.setString(2, lastName);
                    pstmtCreate.setString(3, email);
                    int affectedRows = pstmtCreate.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Creating user failed, no rows affected.");
                    }

                    // Fetch the newly created user's ID
                    try (PreparedStatement pstmtFetch = conn.prepareStatement("SELECT user_id FROM Users WHERE email = ?")) {
                        pstmtFetch.setString(1, email);
                        ResultSet rsFetch = pstmtFetch.executeQuery();
                        if (rsFetch.next()) {
                            return rsFetch.getString("user_id");  // Return new user_id
                        } else {
                            throw new SQLException("Failed to fetch newly created user ID.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user creation fails or user ID cannot be fetched
    }


    
}