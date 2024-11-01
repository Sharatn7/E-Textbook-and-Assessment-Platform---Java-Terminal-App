package Faculty;
import homepage.*;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AddActivity {

    public static void addActivity(Scanner scanner, String sectionId,String blockNum) {
    	System.out.print("Enter Unique Activity Number: ");
        String activity_num = scanner.nextLine();
        
        saveActivityToDatabase(sectionId,blockNum,activity_num);
        while (true) {
            System.out.println("\n=== Add Activity Menu ===");
            System.out.println("1. Add Question");
            System.out.println("2. Go Back");
            
            System.out.print("Enter your choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Step 3: Redirect to Add Question
                	String blockId = getBlockId(sectionId,blockNum);
                	String activityId = getActivityId(blockId,activity_num);
                    AddQuestion.addQuestion(new Scanner(System.in),activityId);
                    break;
                case 2:
                    // Discard the input and go back to the previous page
                    System.out.println("Going back to the previous page...");
                    return; // Go back to the previous page or menu
                
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
                    break;
            }
        }
	}
	// Placeholder method for saving text content to the database
	 private static boolean saveActivityToDatabase(String section_id,String block_num,String activity_num) {
		 Connection conn = null;
		 String insertSectionQuery = "INSERT INTO ContentBlocks (section_id,content_block_number,content_type,content_data,createdby) VALUES (?, ?, ?, ?,?)";
		 String insertSectionQuery1 = "INSERT INTO Activity (content_block_id,activity_number,createdby) VALUES (?, ?,?)";
		 
	     System.out.println("Saving text content to the database: " + activity_num);
	     try {
	    	    // Establish a connection to the database
	    	    conn = DBConnect.getConnection();
	    	    if (conn == null) {
	    	        System.out.println("Failed to establish a database connection.");
	    	        return false;
	    	    }
	    	    PreparedStatement preparedStatement = conn.prepareStatement(insertSectionQuery);
	    	    preparedStatement.setString(1, section_id);
	    	    preparedStatement.setString(2, block_num);
	    	    preparedStatement.setString(4, activity_num);
	    	    preparedStatement.setString(3,"activity");
	    	    preparedStatement.setString(5,"Faculty");
	    	    int rowsInserted = preparedStatement.executeUpdate();
	    	    
	    	    String block_id = getBlockId(section_id,block_num);
	    	    
	    	    preparedStatement = conn.prepareStatement(insertSectionQuery1);
	    	    preparedStatement.setString(1, block_id);
	    	    preparedStatement.setString(2, activity_num);
	    	    preparedStatement.setString(3, "Faculty");
	    	    rowsInserted = preparedStatement.executeUpdate();
	    	    
	    	    return rowsInserted > 0;
	     } catch (SQLException e) {
	    		if(e.toString().contains("Duplicate"))
	    		{
	    			if(e.toString().contains("content_block_id")) {
	    			System.out.println("Blockid already exists in the textbook re enter the details");
	    			AddNewContentBlock.addNewContentBlock(new Scanner(System.in),section_id);	}
	    			if(e.toString().contains("activity_id")) {
	    				System.out.println("Activity id already exists in the textbook re enter the details");
		    			addActivity(new Scanner(System.in),section_id,  block_num);
	    				
	    			}
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
	    	}
	    	    

	}
	     public static String getBlockId(String section_id, String block_number) {
	        String query = "SELECT content_block_id FROM ContentBlocks WHERE section_id = ? AND content_block_number = ?";
	        String content_block_id = null;

	        try (Connection connection = DBConnect.getConnection();
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setString(1, section_id);
	            statement.setString(2, block_number);

	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                	content_block_id = resultSet.getString("content_block_id");
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return content_block_id;
	    }
	
	     public static String getActivityId(String block_id, String activity_number) {
		        String query = "SELECT activity_id FROM Activity WHERE content_block_id = ? AND activity_number = ?";
		        String activity_id = null;

		        try (Connection connection = DBConnect.getConnection();
		             PreparedStatement statement = connection.prepareStatement(query)) {

		            statement.setString(1, block_id);
		            statement.setString(2, activity_number);

		            try (ResultSet resultSet = statement.executeQuery()) {
		                if (resultSet.next()) {
		                	activity_id = resultSet.getString("activity_id");
		                }
		            }

		        } catch (SQLException e) {
		            e.printStackTrace();
		        }

		        return activity_id;
		    }
		


}
