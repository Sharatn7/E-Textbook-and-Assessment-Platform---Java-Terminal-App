package Admin;
import java.sql.*;
import java.util.*;
public class AddQuestion {
	private static Scanner scanner = new Scanner(System.in);
	public static void addQuestion(int tb_id,String chap_id,String section_id,String block_id,String activityId) {
		System.out.print("Enter Question Number: ");
        String questionNum = scanner.nextLine();

        System.out.print("Enter Question Text: ");
        String questionText = scanner.nextLine();

        // Collecting Options and their explanations
        String[] options = new String[4];
        String[] explanations = new String[4];
        int ans = 0;

        for (int i = 0; i < 4; i++) {
            System.out.print("Enter Option " + (i + 1) + " Text: ");
            options[i] = scanner.nextLine();

            System.out.print("Enter Option " + (i + 1) + " Explanation: ");
            explanations[i] = scanner.nextLine();

        }
        System.out.println("Enter Answer Option:");
        ans = scanner.nextInt();
        while (true) {
            System.out.println("\n=== Add Question Menu ===");
            System.out.println("1. Save");
            System.out.println("2. Cancel");
            System.out.println("3. Landing Page");
            System.out.print("Enter your choice (1-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Save question to the database
                    boolean success = saveQuestionToDatabase(tb_id,chap_id,section_id,block_id,activityId,questionNum, questionText, options, explanations, ans);
                    if (success) {
                        System.out.println("Question saved successfully! Returning to Add Activity page...");
                    } else {
                        System.out.println("Failed to save question.");
                    }
                    return; // Go back to the Admin: Add Activity page

                case 2:
                    // Discard the input and go back to the previous page
                    System.out.println("Cancelling and going back to the previous page...");
                    return;

                case 3:
                    // Discard the input and go back to the landing page
                    System.out.println("Cancelling and going back to the landing page...");
                    
                    AdminLandingPage.main(new String[0]);
                    break;

                default:
                    System.out.println("Invalid choice! Please select a valid option.");
                    break;
            }
        }
		
	}

	 private static boolean saveQuestionToDatabase(int tb_id,String chap_id,String section_id,String block_id,String activityId,String questionNum, String questionText, String[] options, String[] explanations, int ans) {
		 Connection conn = null;
		String insertSectionQuery = "INSERT INTO Questions (activity_id,question_number,question_text,option_1,option_2,option_3,option_4,explanation_1,explanation_2,explanation_3,explanation_4,answer) VALUES (?, ?, ?, ?,?,?,?,?,?,?,?,?)"; 
		try {
    	    // Establish a connection to the database
    	    conn = DBConnect.getConnection();
    	    if (conn == null) {
    	        System.out.println("Failed to establish a database connection.");
    	        return false;
    	    }
    	    PreparedStatement preparedStatement = conn.prepareStatement(insertSectionQuery);
    	   
    	    preparedStatement.setString(1, activityId);
    	    preparedStatement.setString(2, questionNum);
    	    preparedStatement.setString(3, questionText);
    	    preparedStatement.setString(4, options[0]);
    	    preparedStatement.setString(8, explanations[0]);
    	    preparedStatement.setString(5, options[1]);
    	    preparedStatement.setString(9, explanations[1]);
    	    preparedStatement.setString(6, options[2]);
    	    preparedStatement.setString(10, explanations[2]);
    	    preparedStatement.setString(7, options[3]);
    	    preparedStatement.setString(11, explanations[3]);
    	    preparedStatement.setInt(12,ans);
    	    
    	    int rowsInserted = preparedStatement.executeUpdate();
    	    
    	    return rowsInserted > 0;
     }
		catch (SQLException e) {
    		if(e.toString().contains("Duplicate"))
    		{
    			System.out.println("QuestionId already exists in the Activity re enter the details");
    			addQuestion(tb_id,chap_id,section_id,block_id,activityId);	
    		}
    	    e.printStackTrace();
    	    return false;
    	} 
     finally {
    	    if (conn != null) {
    	        try {
    	            conn.close();
    	            System.out.println("Database connection closed.");
    	        } catch (SQLException e) {
    	            e.printStackTrace();
    	        }
    	    }
     }}}
