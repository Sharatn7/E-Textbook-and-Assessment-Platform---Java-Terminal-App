import java.util.Scanner;
import java.sql.*;

public class ModifySection {

    public static void modifySection(Scanner scanner,String chapterId) {
        System.out.print("Enter Section Number to Modify: ");
        String sectionNumber = scanner.nextLine();
        	
        String sectionId = AddNewSection.getSectionId(chapterId,sectionNumber);
        
        if(sectionId==null) {
        	System.out.println("Enter an Existing SectionId to Modify");
        	modifySection(scanner,chapterId);
        }
        
        boolean inModifySection = true;
        while (inModifySection) {
            System.out.println("\n=== Modify Section ===");
            System.out.println("1. Hide Section");
            System.out.println("2. Delete Section");
            System.out.println("3. Add New Content Block");
            System.out.println("4. Modify Content Block");
            System.out.println("5. Go Back");
            System.out.print("Enter choice (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    hideSection(scanner, sectionId);  // Hide Section functionality
                    break;
                case 2:
                    deleteSection(scanner, sectionId);  // Delete Section functionality
                    break;
                case 3:
                    AddNewContentBlock.addNewContentBlock(scanner,sectionId);  // Add New Content Block functionality
                    break;
                case 4:
                    ModifyContentBlock.modifyContentBlock(scanner,sectionId);  // Modify Content Block functionality
                    break;
                case 5:
                    System.out.println("Returning to previous menu...");
                    inModifySection = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
    
    public static void hideSection(Scanner scanner, String sectionId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "UPDATE Sections SET Hidden = '1' WHERE section_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, sectionId);

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Section '" + sectionId + "' has been hidden.");
                } else {
                    System.out.println("Failed to hide section. Section ID may not exist.");
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
    
    public static void deleteSection(Scanner scanner, String sectionId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnect.getConnection();

            if (conn != null) {
                String query = "DELETE FROM Sections WHERE section_id = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, sectionId);

                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Section '" + sectionId + "' has been deleted.");
                } else {
                    System.out.println("Failed to delete section. Section ID may not exist.");
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
