package homepage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class QueryExecutionPage {

    public void showQueryPage(Scanner scanner, Connection connection) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Query Execution Page");
            System.out.println("Choose a query to execute:");
            System.out.println("1. Number of sections in the first chapter of a textbook");
            System.out.println("2. Names of Faculty and TAs with roles");
            System.out.println("3. Active courses with faculty and total students");
            System.out.println("4. Course with the largest waiting list");
            System.out.println("5. Contents of Chapter 02 of textbook 101 in sequence");
            System.out.println("6. Incorrect answers and explanations for Q2 of Activity0");
            System.out.println("7. Books active by one instructor and evaluation by another");
            System.out.println("8. Go back to Home Page");

            System.out.print("Enter your choice (1-8): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> executeSectionCountQuery(scanner, connection);
                case 2 -> executeFacultyAndTAQuery(connection);
                case 3 -> executeActiveCoursesQuery(connection);
                case 4 -> executeLargestWaitingListQuery(connection);
                case 5 -> executeChapterContentsQuery(connection);
                case 6 -> executeIncorrectAnswersQuery(connection);
                case 7 -> executeActiveAndEvaluationBooksQuery(connection);
                case 8 -> exit = true;
                default -> System.out.println("Invalid choice. Please select between 1-8.");
            }
        }
    }

    private void executeSectionCountQuery(Scanner scanner, Connection connection) {
        System.out.print("Enter Textbook ID: ");
        int textbookId = scanner.nextInt();
        
        // Query to count the sections of the first chapter of a given textbook
        String sql = "SELECT COUNT(*) AS section_count FROM Sections " +
                     "WHERE chapter_id = (SELECT chapter_id FROM Chapters " +
                     "WHERE textbook_id = ? ORDER BY chapter_number LIMIT 1)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the textbook ID parameter in the query
            statement.setInt(1, textbookId);
            
            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            
            // Retrieve and print the section count
            if (resultSet.next()) {
                System.out.println("Number of sections: " + resultSet.getInt("section_count"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


    private void executeFacultyAndTAQuery(Connection connection) {
        String sql = """
            SELECT U.first_name, U.last_name, 'Faculty' AS role
            FROM Faculty F 
            JOIN Users U ON F.faculty_id = U.user_id
            UNION
            SELECT U.first_name, U.last_name, 'TA' AS role
            FROM TA T 
            JOIN Users U ON T.ta_id = U.user_id
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Faculty and TAs:");
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String role = resultSet.getString("role");
                System.out.println(firstName + " " + lastName + " - " + role);
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


    private void executeActiveCoursesQuery(Connection connection) {
        String sql = "SELECT C.course_id, CONCAT(U.first_name, ' ', U.last_name) AS faculty_name, " +
                     "COUNT(E.student_id) AS total_students " +
                     "FROM Courses C " +
                     "JOIN Faculty F ON C.course_id = F.course_id " +
                     "JOIN Users U ON F.faculty_id = U.user_id " +
                     "LEFT JOIN Enrollments E ON C.course_id = E.course_id " +
                     "WHERE C.course_type = 'Active' " +
                     "GROUP BY C.course_id, faculty_name";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Active Courses:");
            while (resultSet.next()) {
                System.out.println("Course ID: " + resultSet.getString("course_id") +
                                   ", Faculty: " + resultSet.getString("faculty_name") +
                                   ", Total Students: " + resultSet.getInt("total_students"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


    private void executeLargestWaitingListQuery(Connection connection) {
        String sql = "SELECT course_id, (enrollmentCount - capacity) AS waiting_list_count " +
                     "FROM activeCourse WHERE enrollmentCount > capacity " +
                     "ORDER BY waiting_list_count DESC LIMIT 1";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                System.out.println("Course with largest waiting list: " + resultSet.getString("course_id") +
                                   ", Waiting List Count: " + resultSet.getInt("waiting_list_count"));
            } else {
                System.out.println("No courses have a waiting list.");
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }

    private void executeChapterContentsQuery(Connection connection) {
        String sql = "SELECT CB.content_data " +
                     "FROM ContentBlocks CB " +
                     "JOIN Sections S ON CB.section_id = S.section_id " +
                     "JOIN Chapters C ON S.chapter_id = C.chapter_id " +
                     "WHERE C.textbook_id = 101 AND C.chapter_number = 'chap02' " +
                     "ORDER BY S.section_number, CB.content_block_number";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Contents of Chapter 02:");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("content_data"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


    private void executeIncorrectAnswersQuery(Connection connection) {
        String sql = "SELECT option_1 AS incorrect_answer, explanation_1 AS explanation " +
                     "FROM Questions " +
                     "WHERE question_number = 'Q2' AND activity_id = 'ACT0' AND answer != 1 " +
                     "UNION ALL " +
                     "SELECT option_3, explanation_3 " +
                     "FROM Questions " +
                     "WHERE question_number = 'Q2' AND activity_id = 'ACT0' AND answer != 3 " +
                     "UNION ALL " +
                     "SELECT option_4, explanation_4 " +
                     "FROM Questions " +
                     "WHERE question_number = 'Q2' AND activity_id = 'ACT0' AND answer != 4";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Incorrect Answers for Q2 of Activity0:");
            while (resultSet.next()) {
                System.out.println("Incorrect Answer: " + resultSet.getString("incorrect_answer") + 
                                   ", Explanation: " + resultSet.getString("explanation"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


    private void executeActiveAndEvaluationBooksQuery(Connection connection) {
        String sql = "SELECT DISTINCT T.title, C1.course_id AS active_course_id, F1.faculty_id AS active_instructor, " +
                     "C2.course_id AS evaluation_course_id, F2.faculty_id AS evaluation_instructor " +
                     "FROM Courses C1 " +
                     "JOIN Courses C2 ON C1.textbook_id = C2.textbook_id " +
                     "JOIN Textbooks T ON C1.textbook_id = T.textbook_id " +
                     "JOIN Faculty F1 ON C1.course_id = F1.course_id " +
                     "JOIN Faculty F2 ON C2.course_id = F2.course_id " +
                     "WHERE C1.course_type = 'Active' " +
                     "AND C2.course_type = 'Evaluation' " +
                     "AND C1.course_id != C2.course_id " +
                     "AND F1.faculty_id != F2.faculty_id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Books in Active and Evaluation Status by Different Instructors:");
            while (resultSet.next()) {
                System.out.println("Title: " + resultSet.getString("title") +
                                   ", Active Course ID: " + resultSet.getString("active_course_id") +
                                   ", Active Instructor: " + resultSet.getString("active_instructor") +
                                   ", Evaluation Course ID: " + resultSet.getString("evaluation_course_id") +
                                   ", Evaluation Instructor: " + resultSet.getString("evaluation_instructor"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }
}
