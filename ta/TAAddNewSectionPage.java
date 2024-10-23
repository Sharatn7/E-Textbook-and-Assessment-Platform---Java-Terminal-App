import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TAAddNewSectionPage {
	

    public void showAddNewSectionPage(Scanner scanner, String chapterId, String courseId, String previousPage) {
        boolean exit = false;

        while (!exit) {
            System.out.print("Enter Section Number: ");
            String sectionNumber = scanner.nextLine();

            System.out.print("Enter Section Title: ");
            String sectionTitle = scanner.nextLine();

            if (addNewSectionToDatabase(sectionNumber, sectionTitle, chapterId)) {
                System.out.println("Successfully added new section with Number: " + sectionNumber + " and title: " + sectionTitle);
                
                String sectionId = getSectionIdByNumber(sectionNumber, chapterId);
                if (sectionId == null) {
                    System.out.println("Failed to retrieve section ID. Please try again.");
                    continue;
                }

                System.out.println("Menu:");
                System.out.println("1. Add New Content Block");
                System.out.println("2. Go Back");

                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> {
                        System.out.println("Adding a New Content Block to Section ID: " + sectionId + " in Chapter " + chapterId + " of Course " + courseId);
                        TAAddNewContentBlockPage addNewContentBlockPage = new TAAddNewContentBlockPage();
                        addNewContentBlockPage.showAddNewContentBlockPage(scanner, sectionId, chapterId, courseId, "AddNewSection");
                        break;
                    }
                    case 2 -> {
                        System.out.println("Returning to Previous Page...");
                        exit = true;
                        break;
                    }
                    default -> System.out.println("Invalid option, please try again.");
                }
            } else {
                System.out.println("Failed to add new section. Please try again.");
            }
        }
    }

    private boolean addNewSectionToDatabase(String sectionNumber, String sectionTitle, String chapterId) {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                String sql = "INSERT INTO Section (section_number, chapter_id, title, created_by) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, sectionNumber);
                pstmt.setString(2, chapterId);
                pstmt.setString(3, sectionTitle);
                pstmt.setString(4, "TA");

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } else {
                System.out.println("Failed to connect to the database.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getSectionIdByNumber(String sectionNumber, String chapterId) {
        String sectionId = null;
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                String sql = "SELECT section_id FROM Section WHERE section_number = ? AND chapter_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, sectionNumber);
                pstmt.setString(2, chapterId);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    sectionId = rs.getString("section_id");
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sectionId;
    }
}
