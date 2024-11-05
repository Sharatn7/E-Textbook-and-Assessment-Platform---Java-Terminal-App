package Faculty;
import homepage.*;
import java.util.Scanner;
import java.sql.*;

public class ModifyContentBlock {

    public static void modifyContentBlock(Scanner scanner,String sectionId) {
        System.out.print("Enter Content Block Num to Modify: ");
        String contentBlockNum = scanner.nextLine();
        
        String contentBlockId = AddActivity.getBlockId(sectionId, contentBlockNum);
        
        if(contentBlockId==null) {
        	System.out.println("Enter an Existing Content Block Id to Modify");
        	modifyContentBlock(scanner,sectionId);
        }

        boolean inModifyContentBlock = true;
        while (inModifyContentBlock) {
            System.out.println("\n=== Modify Content Block ===");
            System.out.println("1. Hide Content Block");
            System.out.println("2. Delete Content Block");
            System.out.println("3. Add Text");
            System.out.println("4. Add Picture");
            System.out.println("5. Hide Activity");
            System.out.println("6. Delete Activity");
            System.out.println("7. Add Activity");
            System.out.println("8. Go Back");
            System.out.print("Enter choice (1-8): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    hideContentBlock(scanner, contentBlockId);  // Hide Content Block functionality
                    break;
                case 2:
                    deleteContentBlock(scanner, contentBlockId);  // Delete Content Block functionality
                    break;
                case 3:
                	deleteContentBlock(scanner, contentBlockId);
                	 AddText.addText(scanner,sectionId,contentBlockNum);  // Add Text functionality
                    break;
                case 4:
                	deleteContentBlock(scanner, contentBlockId);
               	    AddPicture.addPicture(scanner,sectionId,contentBlockNum);  // Add Picture functionality
                    break;
                case 5:
                    hideActivity(scanner, contentBlockId);  // Hide Activity functionality
                    break;
                case 6:
                    deleteActivity(scanner, contentBlockId);  // Delete Activity functionality
                    break;
                case 7:
                	deleteContentBlock(scanner, contentBlockId);
                	AddActivity.addActivity(scanner,sectionId,contentBlockNum);  // Add Activity functionality
                    break;
                case 8:
                    System.out.println("Returning to previous menu...");
                    inModifyContentBlock = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
    }
    
    public static void hideContentBlock(Scanner scanner, String contentBlockId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "UPDATE ContentBlocks SET Hidden = '1' WHERE content_block_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, contentBlockId);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Content block '" + contentBlockId + "' has been hidden.");
                } else {
                    System.out.println("Failed to hide content block. Content block ID may not exist.");
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
    }
    
    public static void deleteContentBlock(Scanner scanner, String contentBlockId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "DELETE FROM ContentBlocks WHERE content_block_id = ? and (createdby= ? or createdby = ?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, contentBlockId);
                pstmt.setString(2, "Faculty");
                pstmt.setString(3, "TA");

                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Content block '" + contentBlockId + "' has been deleted.");
                } else {
                    System.out.println("Failed to delete content block. Content block ID may not exist or it is crated by admin");
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
    }
    public static void hideActivity(Scanner scanner, String contentBlockId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "UPDATE Activity SET hidden = '1' WHERE content_block_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, contentBlockId);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Activities for content block '" + contentBlockId + "' have been hidden.");
                } else {
                    System.out.println("Failed to hide activities. Content block ID may not exist.");
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
    }
    public static void deleteActivity(Scanner scanner, String contentBlockId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "DELETE FROM Activity WHERE content_block_id = ?  and (createdby= ? or createdby = ?)";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, contentBlockId);
                
                pstmt.setString(2, "Faculty");
                pstmt.setString(3, "TA");


                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Activities for content block '" + contentBlockId + "' have been deleted.");
                } else {
                    System.out.println("Failed to delete activities. Content block ID may not exist. Or might have been created by Admin.");
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
    }


}
