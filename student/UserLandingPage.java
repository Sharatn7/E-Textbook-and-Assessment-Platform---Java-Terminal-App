import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserLandingPage {
    private Scanner scanner;
    private String userId;

    public UserLandingPage(String userId) {
        this.scanner = new Scanner(System.in);
        this.userId = userId;
    }

    public void displayMenu() {
        showAvailableContent();
        boolean sessionActive = true;
        while (sessionActive) {
            System.out.println("\n--- User Landing Page ---");
            System.out.println("1. View a section");
            System.out.println("2. View participation activity points");
            System.out.println("3. Logout");

            System.out.print("Choose option (1-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewSection();
                    break;
                case 2:
                    viewParticipationPoints();
                    break;
                case 3:
                    System.out.println("Logging out...");
                    sessionActive = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void showAvailableContent() {
        System.out.println("Available E-book Content:");
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT t.title AS textbook, c.chapter_num, c.title AS chapter, s.section_number, s.title AS section " +
                     "FROM Textbook t " +
                     "JOIN Chapter c ON t.tb_id = c.tb_id " +
                     "JOIN Section s ON c.chapter_id = s.chapter_id " +
                     "ORDER BY t.title, c.chapter_num, s.section_number")) {

            ResultSet rs = stmt.executeQuery();
            String currentTextbook = "";

            while (rs.next()) {
                String textbook = rs.getString("textbook");
                String chapter = rs.getString("chapter_num") + " " + rs.getString("chapter");
                String section = rs.getString("section_number") + " " + rs.getString("section");

                if (!textbook.equals(currentTextbook)) {
                    System.out.println("E-book " + textbook + ":");
                    currentTextbook = textbook;
                }
                System.out.println("  Chapter " + chapter);
                System.out.println("    Section " + section);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve content.");
        }
    }

    private void viewSection() {
        System.out.println("\n--- View Section ---");
        System.out.print("Enter Chapter ID: ");
        String chapterId = scanner.nextLine();
        System.out.print("Enter Section ID: ");
        String sectionId = scanner.nextLine();
        System.out.println("1. View Block");
        System.out.println("2. Go Back");
        System.out.print("Choose option (1-2): ");
        int option = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (option == 1) {
            displayBlocks(chapterId, sectionId);
        } else {
            System.out.println("Returning to User Landing Page...");
        }
    }
    

        public void displayBlocks(String chapterId, String sectionId) {
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "SELECT block_id, content, type, correct_answer, option1, option2, option3, option4 FROM ContentBlock WHERE chapter_id = ? AND section_id = ? ORDER BY block_id")) {
                pstmt.setString(1, chapterId);
                pstmt.setString(2, sectionId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int blockId = rs.getInt("block_id");
                    String content = rs.getString("content");
                    String type = rs.getString("type"); // Assuming 'content' or 'activity'
                    String correctAnswer = rs.getString("correct_answer"); // For activities
                    String[] options = new String[] {rs.getString("option1"), rs.getString("option2"), rs.getString("option3"), rs.getString("option4")}; // For activities

                    viewBlock(blockId, content, type, correctAnswer, options);
                }
                if (!rs.isBeforeFirst()) {
                    System.out.println("No blocks found. Returning to landing page...");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to retrieve blocks.");
            }
        }

        private void viewBlock(int blockId, String content, String type, String correctAnswer, String[] options) {
            System.out.println("Block " + blockId + ": " + content);
            if (type.equals("activity")) {
                System.out.println("Choose the correct answer:");
                for (int i = 0; i < options.length; i++) {
                    System.out.println((i + 1) + ". " + options[i]);
                }
                int userChoice = scanner.nextInt() - 1;
                scanner.nextLine(); // consume newline
                if (options[userChoice].equals(correctAnswer)) {
                    System.out.println("Correct! Explanation: [insert explanation here]");
                } else {
                    System.out.println("Incorrect. Correct answer: " + correctAnswer + ". Explanation: [insert explanation here]");
                }
            }

            System.out.println("1. Next/Submit");
            System.out.println("2. Go back");
            System.out.print("Choose option (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (choice == 2) {
                return; // Exit to previous menu
            }
            // Continue to next block or end
        }


    private void viewParticipationPoints() {
            try (Connection conn = DBConnect.getConnection()) {
                String sql = "SELECT SUM(points) AS totalPoints FROM Participation WHERE student_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, this.userId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int totalPoints = rs.getInt("totalPoints");
                    System.out.println("Total Participation Points: " + totalPoints);
                } else {
                    System.out.println("No participation points found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
}
