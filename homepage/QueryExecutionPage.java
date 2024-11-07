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
             SELECT CONCAT(U.first_name, ' ', U.last_name) AS Name, 'Faculty' AS role
            FROM Faculty F
            JOIN Users U ON F.faculty_id = U.user_id
            UNION
            SELECT CONCAT(U.first_name, ' ', U.last_name) AS Name, 'TA' AS role
            FROM TA T
            JOIN Users U ON T.ta_id = U.user_id
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Faculty and TAs:");
            while (resultSet.next()) {
                String Name = resultSet.getString("Name");
                String role = resultSet.getString("role");
                System.out.println(Name+ " - " + role);
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


    private void executeActiveCoursesQuery(Connection connection) {
    	String sql = """
    		    SELECT A.course_id, CONCAT(U.first_name, ' ', U.last_name) AS faculty_name, A.EnrollmentCount 
    		    FROM activeCourse A 
    		    JOIN Faculty f ON f.course_id = A.course_id 
    		    JOIN Users U ON f.faculty_id = U.user_id
    		    """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Active Courses:");
            while (resultSet.next()) {
                System.out.println("Course ID: " + resultSet.getString("course_id") +
                                   ", Faculty: " + resultSet.getString("faculty_name") +
                                   ", Total Students: " + resultSet.getInt("EnrollmentCount"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


    private void executeLargestWaitingListQuery(Connection connection) {
        String sql = """
        SELECT course_id, COUNT(student_id) AS waiting_list_count
        FROM Enrollments
       GROUP BY course_id
       ORDER BY waiting_list_count DESC
        LIMIT 1""";
        
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
    	String sql = "SELECT "
    	           + "CASE "
    	           + "WHEN CB.content_type = 'activity' THEN CONCAT('Question:',Q.question_text, '|Option1:', Q.option_1, '|Option2:', Q.option_2, '|Option3:', Q.option_3, '|Option4:', Q.option_4, '|Answer:', Q.answer) "
    	           + "ELSE CB.content_data "
    	           + "END AS content, CB.content_type "
    	           + "FROM ContentBlocks CB "
    	           + "JOIN Sections S ON CB.section_id = S.section_id "
    	           + "JOIN Chapters C ON S.chapter_id = C.chapter_id "
    	           + "LEFT JOIN Activity A ON CB.content_block_id = A.content_block_id AND CB.content_type = 'activity' "
    	           + "LEFT JOIN Questions Q ON A.activity_id = Q.activity_id "
    	           + "WHERE C.textbook_id = '101' "
    	           + "AND C.chapter_number = 'chap02' "
    	           + "ORDER BY S.section_number, CB.content_block_number;";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Contents of Chapter 02:");
            while (resultSet.next()) {
            	System.out.print(resultSet.getString("content_type")+" ");
            	String data = resultSet.getString("content");
            	String a[]=data.split("|");
            	for(int i=0;i<a.length;i++)
            		System.out.print(a[i]);
            	System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


    private void executeIncorrectAnswersQuery(Connection connection) {
        String sql = "WITH CTE AS (\r\n"
        		+ "    SELECT Questions.question_id AS qn_id\r\n"
        		+ "    FROM Questions\r\n"
        		+ "    INNER JOIN Activity ON Questions.activity_id = Activity.activity_id\r\n"
        		+ "    INNER JOIN ContentBlocks ON Activity.content_block_id = ContentBlocks.content_block_id\r\n"
        		+ "    INNER JOIN Sections ON ContentBlocks.section_id = Sections.section_id\r\n"
        		+ "    INNER JOIN Chapters ON Sections.chapter_id = Chapters.chapter_id\r\n"
        		+ "    INNER JOIN Textbooks ON Chapters.textbook_id = Textbooks.textbook_id\r\n"
        		+ "    WHERE \r\n"
        		+ "        Textbooks.textbook_id = 101\r\n"
        		+ "        AND Chapters.chapter_number = 'chap01'\r\n"
        		+ "        AND Sections.section_number = 'Sec02'\r\n"
        		+ "        AND Activity.activity_number = 'ACT0'\r\n"
        		+ "        AND Questions.question_number = 'Q2'\r\n"
        		+ ")\r\n"
        		+ "\r\n"
        		+ "\r\n"
        		+ "SELECT option_1 AS incorrect_option, explanation_1 AS explanation\r\n"
        		+ "FROM Questions, CTE\r\n"
        		+ "WHERE question_id  = CTE.qn_id AND answer != '1'\r\n"
        		+ "UNION\r\n"
        		+ "SELECT option_2 AS incorrect_option, explanation_2 AS explanation\r\n"
        		+ "FROM Questions, CTE\r\n"
        		+ "WHERE question_id  = CTE.qn_id AND answer != '2'\r\n"
        		+ "UNION\r\n"
        		+ "SELECT option_3 AS incorrect_option, explanation_3 AS explanation\r\n"
        		+ "FROM Questions, CTE\r\n"
        		+ "WHERE question_id  = CTE.qn_id AND answer != '3'\r\n"
        		+ "UNION\r\n"
        		+ "SELECT option_4 AS incorrect_option, explanation_4 AS explanation\r\n"
        		+ "FROM Questions, CTE\r\n"
        		+ "WHERE question_id  = CTE.qn_id AND answer != '4'";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Incorrect Answers for Q2 of Activity0:");
            while (resultSet.next()) {
                System.out.println("Incorrect Answer: " + resultSet.getString("incorrect_option") + 
                                   ", Explanation: " + resultSet.getString("explanation"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }


    private void executeActiveAndEvaluationBooksQuery(Connection connection) {
    	String sql = "SELECT DISTINCT T.title, C1.title AS active_course_title, F1.faculty_id AS active_instructor, "
    	           + "C2.title AS evaluation_course_title, F2.faculty_id AS evaluation_instructor "
    	           + "FROM Courses C1 "
    	           + "JOIN Faculty F1 ON C1.course_id = F1.course_id "
    	           + "JOIN Courses C2 ON substring(C1.textbook_id, 3) = substring(C2.textbook_id, 3) AND C2.course_id != C1.course_id "
    	           + "JOIN Faculty F2 ON C2.course_id = F2.course_id "
    	           + "JOIN Textbooks T ON C1.textbook_id = T.textbook_id "
    	           + "WHERE C1.course_type = 'Active' "
    	           + "AND C2.course_type = 'Evaluation' "
    	           + "AND F1.faculty_id != F2.faculty_id;";


        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Books in Active and Evaluation Status by Different Instructors:");
            while (resultSet.next()) {
                System.out.println("Title:"+resultSet.getString("title")+", Active Course ID: " + resultSet.getString("active_course_title") +
                                   ", Active Instructor: " + resultSet.getString("active_instructor") +
                                   ", Evaluation Course ID: " + resultSet.getString("evaluation_course_title") +
                                   ", Evaluation Instructor: " + resultSet.getString("evaluation_instructor"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }
}
