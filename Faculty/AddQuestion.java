import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AddQuestion {

    public static void addQuestion(Scanner scanner) {
        System.out.println("=== Add New Question ===");

        System.out.print("A. Enter Question ID: ");
        String questionId = scanner.nextLine();

        System.out.print("B. Enter Question Text: ");
        String questionText = scanner.nextLine();

        System.out.print("C. Enter Option 1: ");
        String option1 = scanner.nextLine();

        System.out.print("D. Enter Option 1 Explanation: ");
        String explanation1 = scanner.nextLine();

        System.out.print("E. Enter Option 2: ");
        String option2 = scanner.nextLine();

        System.out.print("F. Enter Option 2 Explanation: ");
        String explanation2 = scanner.nextLine();

        System.out.print("G. Enter Option 3: ");
        String option3 = scanner.nextLine();

        System.out.print("H. Enter Option 3 Explanation: ");
        String explanation3 = scanner.nextLine();

        System.out.print("I. Enter Option 4: ");
        String option4 = scanner.nextLine();

        System.out.print("J. Enter Option 4 Explanation: ");
        String explanation4 = scanner.nextLine();

        System.out.print("K. Enter Answer: ");
        String answer = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "INSERT INTO Questions (question_id, question_text, option1, explanation1, option2, explanation2, option3, explanation3, option4, explanation4, answer) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, questionId);
                pstmt.setString(2, questionText);
                pstmt.setString(3, option1);
                pstmt.setString(4, explanation1);
                pstmt.setString(5, option2);
                pstmt.setString(6, explanation2);
                pstmt.setString(7, option3);
                pstmt.setString(8, explanation3);
                pstmt.setString(9, option4);
                pstmt.setString(10, explanation4);
                pstmt.setString(11, answer);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Question successfully added.");
                } else {
                    System.out.println("Failed to add question.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        boolean inAddQuestion = true;
        while (inAddQuestion) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    if (!questionId.isEmpty() && !questionText.isEmpty() && !option1.isEmpty() && !answer.isEmpty()) {
                        System.out.println("Question successfully added.");
                        inAddQuestion = false;  // Exit after saving
                    } else {
                        System.out.println("Failed to add question. Some fields are missing.");
                    }
                    break;
                case 2:
                    System.out.println("Operation canceled. Returning to previous menu...");
                    inAddQuestion = false;  // Exit and discard input
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }
}
