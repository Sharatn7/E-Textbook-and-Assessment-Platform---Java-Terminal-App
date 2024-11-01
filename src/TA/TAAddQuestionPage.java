package TA;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAAddQuestionPage {

    public void showAddQuestionPage(Scanner scanner, String activityId) {
        boolean exit = false;

        while (!exit) {
            System.out.print("Enter Unique Question Number: ");
            String questionNumber = scanner.nextLine();

            // Verify if the question already exists
            if (isQuestionExisting(activityId, questionNumber)) {
                System.out.println("This Question Number already exists for the specified activity. Please enter a unique Question Number.");
                continue;
            }

            System.out.print("Enter Question Text: ");
            String questionText = scanner.nextLine();

            System.out.print("Enter Option 1: ");
            String option1 = scanner.nextLine();

            System.out.print("Enter Option 1 Explanation: ");
            String option1Explanation = scanner.nextLine();

            System.out.print("Enter Option 2: ");
            String option2 = scanner.nextLine();

            System.out.print("Enter Option 2 Explanation: ");
            String option2Explanation = scanner.nextLine();

            System.out.print("Enter Option 3: ");
            String option3 = scanner.nextLine();

            System.out.print("Enter Option 3 Explanation: ");
            String option3Explanation = scanner.nextLine();

            System.out.print("Enter Option 4: ");
            String option4 = scanner.nextLine();

            System.out.print("Enter Option 4 Explanation: ");
            String option4Explanation = scanner.nextLine();

            System.out.print("Enter the Correct Answer (Option 1/2/3/4): ");
            int correctAnswer = scanner.nextInt();
            scanner.nextLine();  // Clear the newline

            System.out.println("Menu:");
            System.out.println("1. Save");
            System.out.println("2. Cancel");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    if (saveQuestionToDatabase(activityId, questionNumber, questionText, 
                            option1, option1Explanation, option2, option2Explanation, option3, option3Explanation, 
                            option4, option4Explanation, correctAnswer)) {
                        System.out.println("Question saved successfully.");
                    } else {
                        System.out.println("Failed to save question. Please try again.");
                    }
                    exit = true;
                }
                case 2 -> {
                    System.out.println("Cancelling and returning to Activity Page...");
                    exit = true;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private boolean isQuestionExisting(String activityId, String questionNumber) {
        String sql = "SELECT question_id FROM Questions WHERE activity_id = ? AND question_number = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, activityId);
            statement.setString(2, questionNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Return true if a matching question is found
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking question number: " + e.getMessage());
        }
        return false; // Return false if no matching question was found or on error
    }

    private boolean saveQuestionToDatabase(String activityId, String questionNumber, String questionText, 
                                           String option1, String option1Explanation, 
                                           String option2, String option2Explanation, 
                                           String option3, String option3Explanation, 
                                           String option4, String option4Explanation, 
                                           int correctAnswer) {
        String sqlInsert = "INSERT INTO Questions (activity_id, question_number, question_text, " +
                           "option_1, option_2, option_3, option_4, " +
                           "explanation_1, explanation_2, explanation_3, explanation_4, answer) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(sqlInsert)) {

            insertStatement.setString(1, activityId);
            insertStatement.setString(2, questionNumber);
            insertStatement.setString(3, questionText);
            insertStatement.setString(4, option1);
            insertStatement.setString(5, option2);
            insertStatement.setString(6, option3);
            insertStatement.setString(7, option4);
            insertStatement.setString(8, option1Explanation);
            insertStatement.setString(9, option2Explanation);
            insertStatement.setString(10, option3Explanation);
            insertStatement.setString(11, option4Explanation);
            insertStatement.setInt(12, correctAnswer);

            int rowsAffected = insertStatement.executeUpdate();
            return rowsAffected > 0; // Return true if insertion was successful
        } catch (SQLException e) {
            System.out.println("Database error while saving question: " + e.getMessage());
        }
        return false; // Return false if insertion failed
    }
}
