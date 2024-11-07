package student;
import homepage.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserLandingPage {
    private Scanner scanner = new Scanner(System.in);
    private String userId = Login.getAuthenticatedUserId();
    private SectionResolver sectionResolver = new SectionResolver(Login.getAuthenticatedUserId());

    public void displayMenu() {
        System.out.println("\n--- Student Home Page ---");
        showAvailableContent(Login.getAuthenticatedUserId());
        boolean sessionActive = true;
        while (sessionActive) {
            System.out.println("1. View a section");
            System.out.println("2. View participation activity points");
            System.out.println("3. View Notifications");
            System.out.println("4. Logout");

            System.out.print("Choose option (1-4): ");
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
                	getNotificationsForUser();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    sessionActive = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    
    public void getNotificationsForUser() {
        String selectSql = "SELECT notification_id, message " +
                           "FROM Notifications " +
                           "WHERE user_id = ? " +
                           "ORDER BY timestamp DESC"; // Orders notifications by the most recent first

        String deleteSql = "DELETE FROM Notifications WHERE notification_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            selectStmt.setString(1, userId);
            ResultSet rs = selectStmt.executeQuery();

            System.out.println("---Notifications---");
            if (!rs.isBeforeFirst()) { // Checks if the result set is empty
                System.out.println("No notifications.");
                return;
            }
            while (rs.next()) {
                String message = rs.getString("message");
                int notificationId = rs.getInt("notification_id");

                // Display the notification message
                System.out.println(message);

                // Prepare to delete this notification by its ID
                deleteStmt.setInt(1, notificationId);
                deleteStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while fetching and deleting notifications for user ID " + userId + ": " + e.getMessage());
        }
    }

    
    private void showAvailableContent(String studentId) {
        System.out.println("Available E-book Content:");
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT DISTINCT t.title AS textbook, c.chapter_number AS chapter_num, c.title AS chapter, " +
                     "s.section_number AS sec_num, s.title AS section, " +
                     "cb.content_block_number AS block_num, cb.content_type AS block_type " +
                     "FROM Textbooks t " +
                     "JOIN Chapters c ON t.textbook_id = c.textbook_id " +
                     "JOIN Sections s ON c.chapter_id = s.chapter_id " +
                     "LEFT JOIN ContentBlocks cb ON s.section_id = cb.section_id " +
                     "JOIN Courses co ON t.textbook_id = co.textbook_id " +
                     "JOIN Student st ON co.course_id = st.course_id " +
                     "WHERE c.Hidden = '0' AND s.Hidden = '0' AND cb.Hidden = '0' AND st.student_id = ? " +
                     "ORDER BY t.title, c.chapter_number, s.section_number, cb.content_block_number")) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            String currentTextbook = "";
            String currentChapter = "";
            String currentSection = "";
            int textbookCount = 0;
            int visibleChapterCount = 0;
            int visibleSectionCount = 0;
            int blockCount = 0;

            while (rs.next()) {
                String textbook = rs.getString("textbook");
                String chapter = rs.getString("chapter");
                String section = rs.getString("section");
                String blockType = rs.getString("block_type");

                if (!textbook.equals(currentTextbook)) {
                    textbookCount++;
                    System.out.println("E-book " + textbookCount + ": " + textbook);
                    currentTextbook = textbook;
                    currentChapter = "";
                    visibleChapterCount = 0;
                }
                if (!chapter.equals(currentChapter)) {
                    visibleChapterCount++;
                    System.out.println("  Chapter " + visibleChapterCount + ": " + chapter);
                    currentChapter = chapter;
                    currentSection = "";
                    visibleSectionCount = 0;
                }

                if (!section.equals(currentSection)) {
                    visibleSectionCount++;
                    System.out.println("    Section " + visibleSectionCount + ": " + section);
                    currentSection = section;
                    blockCount = 0;
                }

                // Only print block information if it's a new block
                if (blockCount == 0 || !blockType.equals(currentSection)) {
                    blockCount++;
                    System.out.println("      Block " + blockCount + ": " + blockType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve content.");
        }
    }


    private void viewSection() {
        System.out.println("\n---- View Section ---");

        System.out.print("Enter Textbook Number: ");
        int textbookNum = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Chapter Number: ");
        String chapterNum = scanner.nextLine();  // Read chapter number as string
        System.out.print("Enter Section Number: ");
        String sectionNum = scanner.nextLine();  // Read section number as string
        System.out.println("1. View Block");
        System.out.println("2. Go Back");
        System.out.print("Choose option (1-2): ");
        int option = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (option == 1) {
        	        SectionInfo sectionInfo = sectionResolver.resolveSectionInfo(textbookNum, chapterNum, sectionNum);
        	        if (sectionInfo != null) {
        	            displayBlocks(sectionInfo.getSectionId(), sectionNum, getFirstCourseForTextbook(sectionInfo.getTextbookId())); // Assuming displayBlocks can take section ID as an argument
        	        } else {
                 System.out.println("Invalid course number or chapter or section number. Please try again.");
             }
        } else {
            System.out.println("Returning to User Landing Page...");
        }
    }
 
    public String getFirstCourseForTextbook(String textbookId) {
        String sql = "SELECT co.course_id " +
                     "FROM Courses co " +
                     "WHERE co.textbook_id = ? " +
                     "ORDER BY co.course_id " + // Ensures consistent results
                     "LIMIT 1"; // Ensures only one result is returned

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, textbookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("course_id");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while fetching the first course ID for textbook ID " + textbookId + ": " + e.getMessage());
        }

        return null; // Return null if no course is found or an exception occurs
    }

  
    private void handleContent() {
        System.out.println("Press 'Enter' to continue to the next block or type 'back' to go back.");
        String input = scanner.nextLine();
        if ("back".equalsIgnoreCase(input.trim())) {
            return; // This will return control to the calling method, potentially to navigate back
        }
    }
    
    private void displayBlocks(String sectionId, String sectionNum, String courseNumber) {
        System.out.println("\n--- Displaying Blocks for Section ID: " + sectionNum + " ---");
        List<String> blockIds = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT cb.content_block_id, cb.content_type, cb.content_data, a.activity_number " +
                     "FROM ContentBlocks cb " +
                     "LEFT JOIN Activity a ON cb.content_block_id = a.content_block_id " +
                     "WHERE cb.section_id = ? AND cb.Hidden = '0' AND a.Hidden = '0' " +
                     "ORDER BY cb.content_block_number")) {
            pstmt.setString(1, sectionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String blockId = rs.getString("content_block_id");
                String contentType = rs.getString("content_type");
                String contentData = rs.getString("content_data");
                String activityNumber = rs.getString("activity_number");
                blockIds.add(blockId);

                if ("activity".equalsIgnoreCase(contentType)) {
                    System.out.println("Activity: Answer the question below.");
                    handleActivity(blockId, activityNumber, courseNumber);
                } else {
                    System.out.println("Content: " + contentData);
                    handleContent();
                }
                System.out.println("---");
            }

            if (blockIds.isEmpty()) {
                System.out.println("No blocks available for this section.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve blocks for the section.");
        }
    }
    
    private void handleActivity(String blockId, String activityNumber, String courseNumber) {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT q.question_text, q.option_1, q.option_2, q.option_3, q.option_4, q.answer, q.explanation_1, q.explanation_2, q.explanation_3, q.explanation_4, q.question_id " +
                     "FROM Questions q " +
                     "JOIN Activity a ON q.activity_id = a.activity_id " +
                     "WHERE a.content_block_id = ? AND a.activity_number = ?")) {
            pstmt.setString(1, blockId);
            pstmt.setString(2, activityNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("question_text"));
                System.out.println("1: " + rs.getString("option_1"));
                System.out.println("2: " + rs.getString("option_2"));
                System.out.println("3: " + rs.getString("option_3"));
                System.out.println("4: " + rs.getString("option_4"));
                System.out.println("Enter the ID of the correct answer or press ENTER to skip:");
                String input = scanner.nextLine();
                if (input.isEmpty()) {
                    // Log skipped question with 0 points
                    updateActivityParticipation(Login.getAuthenticatedUserId(), courseNumber, rs.getString("question_id"), 0);
                    System.out.println("Question skipped.");
                } else {
                    try {
                        int answerId = Integer.parseInt(input);
                        checkActivityAnswer(rs, answerId, Login.getAuthenticatedUserId(), courseNumber);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        // Optionally, you might want to allow the user to reattempt entering a valid input here
                    }
                }
                System.out.println("--- Next Question ---");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve or handle activity questions.");
        }
    }


    private void checkActivityAnswer(ResultSet rs, int userAnswer, String studentId, String courseId) throws SQLException {
        String questionId = rs.getString("question_id");
        int correctAnswer = rs.getInt("answer");
        int points = (userAnswer == correctAnswer) ? 3 : 1; // 3 points for correct answer, 1 for attempt

        String explanation = rs.getString("explanation_" + correctAnswer);
        if (userAnswer == correctAnswer) {
            System.out.println("Correct! Explanation: " + explanation);
        } else {
            System.out.println("Incorrect. Correct answer was: " + correctAnswer + ". Explanation: " + explanation);
        }

        // Update the activity participation with points
        updateActivityParticipation(studentId, courseId, questionId, points);
    }

    private void updateActivityParticipation(String studentId, String courseId, String questionId, int points) {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO ActivityParticipation (student_id, course_id, question_id, point) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE point = VALUES(point), timestamp = CURRENT_TIMESTAMP()")) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseId);
            pstmt.setString(3, questionId);
            pstmt.setInt(4, points);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Score updated successfully.");
            } else {
                System.out.println("Failed to update score.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating the activity participation.");
        }
    }


    private void viewParticipationPoints() {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT total_participation_points FROM ParticipationSummary WHERE student_id = ?")) {
            pstmt.setString(1, this.userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalPoints = rs.getInt("total_participation_points");
                System.out.println("Total Participation Points: " + totalPoints);
            } else {
                System.out.println("No participation points found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve participation points.");
        }
    }
}


class SectionResolver {
    private Map<Integer, String> textbookNumberToId = new HashMap<>();
    private Map<String, String> chapterNumberToId = new HashMap<>();
    private Map<String, Map<String, String>> sectionNumberToId = new HashMap<>();
    private String studentId; // Student ID assumed to be set via constructor

    public SectionResolver(String studentId) {
        this.studentId = studentId;
        populateMappings();
    }

    private void populateMappings() {
        int textbookCounter = 1;
        String lastTextbookId = "";
        Map<String, Integer> chapterCounters = new HashMap<>();

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT DISTINCT t.textbook_id, c.chapter_id, c.chapter_number, s.section_id, s.section_number " +
                 "FROM Textbooks t " +
                 "JOIN Chapters c ON t.textbook_id = c.textbook_id " +
                 "JOIN Sections s ON c.chapter_id = s.chapter_id " +
                 "JOIN Courses co ON t.textbook_id = co.textbook_id " +
                 "JOIN Student st ON co.course_id = st.course_id " +
                 "WHERE c.Hidden = '0' AND s.Hidden = '0' AND st.student_id = ? " +
                 "ORDER BY t.textbook_id, c.chapter_number, s.section_number")) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String textbookId = rs.getString("textbook_id");
                String chapterId = rs.getString("chapter_id");
                String sectionId = rs.getString("section_id");

                if (!textbookId.equals(lastTextbookId)) {
                    lastTextbookId = textbookId;
                    textbookNumberToId.put(textbookCounter, textbookId);
                    chapterCounters.clear();
                    textbookCounter++;
                }

                Integer chapterIndex = chapterCounters.computeIfAbsent(chapterId, k -> chapterCounters.size() + 1);
                chapterNumberToId.put(textbookId + "-" + chapterIndex, chapterId);

                Map<String, String> sections = sectionNumberToId.computeIfAbsent(chapterId, k -> new HashMap<>());
                int sectionIndex = sections.size() + 1;
                sections.put(String.valueOf(sectionIndex), sectionId);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while populating mappings: " + e.getMessage());
        }
    }

    public SectionInfo resolveSectionInfo(int textbookNumber, String chapterNumber, String sectionNumber) {
        String textbookId = textbookNumberToId.get(textbookNumber);
        if (textbookId != null) {
            String chapterId = chapterNumberToId.get(textbookId + "-" + chapterNumber);
            if (chapterId != null) {
                Map<String, String> sections = sectionNumberToId.get(chapterId);
                if (sections != null) {
                    String sectionId = sections.get(sectionNumber);
                    if (sectionId != null) {
                        return new SectionInfo(textbookId, sectionId);
                    }
                }
            }
        }
        return null;
    }

}

class SectionInfo {
    private String textbookId;
    private String sectionId;

    public SectionInfo(String textbookId, String sectionId) {
        this.textbookId = textbookId;
        this.sectionId = sectionId;
    }

    public String getTextbookId() {
        return textbookId;
    }

    public String getSectionId() {
        return sectionId;
    }

    @Override
    public String toString() {
        return "TextbookID: " + textbookId + ", SectionID: " + sectionId;
    }
}


