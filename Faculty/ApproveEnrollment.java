package Faculty;
import homepage.*;
import java.sql.*;
import java.util.Scanner;

import java.sql.*;
import java.util.Scanner;

public class ApproveEnrollment {

    public static void approveEnrollment(Scanner scanner, String courseId) {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        if (!isStudentIdValid(studentId, courseId)) {
            System.out.println("Enter valid student details");
            approveEnrollment(scanner, courseId);
            return;
        }

        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                conn.setAutoCommit(false); // Start transaction

                try {
                    // Fetch current enrollment and capacity
                    String fetchCourseQuery = "SELECT enrollmentCount, capacity FROM activeCourse WHERE course_id = ?";
                    try (PreparedStatement fetchStmt = conn.prepareStatement(fetchCourseQuery)) {
                        fetchStmt.setString(1, courseId);
                        ResultSet rs = fetchStmt.executeQuery();
                        if (rs.next()) {
                            int enrollmentCount = rs.getInt("enrollmentCount");
                            int capacity = rs.getInt("capacity");

                            if (enrollmentCount < capacity) {
                                // Update role for the approved student
                                String updateUserRoleQuery = "UPDATE Users SET role = 'Student' WHERE user_id = ?";
                                try (PreparedStatement updateRoleStmt = conn.prepareStatement(updateUserRoleQuery)) {
                                    updateRoleStmt.setString(1, studentId);
                                    updateRoleStmt.executeUpdate();
                                }

                                // Insert the student entry into Student table
                                String insertStudentQuery = "INSERT INTO Student (student_id, course_id) VALUES (?, ?)";
                                try (PreparedStatement insertStmt = conn.prepareStatement(insertStudentQuery)) {
                                    insertStmt.setString(1, studentId);
                                    insertStmt.setString(2, courseId);
                                    insertStmt.executeUpdate();
                                }

                                // Increment enrollment count in activeCourse
                                String incrementEnrollmentQuery = "UPDATE activeCourse SET enrollmentCount = enrollmentCount + 1 WHERE course_id = ?";
                                try (PreparedStatement incrementStmt = conn.prepareStatement(incrementEnrollmentQuery)) {
                                    incrementStmt.setString(1, courseId);
                                    incrementStmt.executeUpdate();
                                }

                                // Finally, delete student entry from Enrollments
                                String deleteEnrollmentQuery = "DELETE FROM Enrollments WHERE student_id = ? AND course_id = ?";
                                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteEnrollmentQuery)) {
                                    deleteStmt.setString(1, studentId);
                                    deleteStmt.setString(2, courseId);
                                    deleteStmt.executeUpdate();
                                }
                                System.out.println("Enrollment approved for Student ID: " + studentId);

                                // Check if the course has reached full capacity
                                if (enrollmentCount + 1 == capacity) {
                                    notifyAndClearWaitlistedStudents(conn, courseId);
                                }
                            } else {
                                notifyAndClearWaitlistedStudents(conn, courseId);
                                System.out.println("Capacity reached. Notifications sent to all waiting students.");
                            }
                        }
                    }
                    conn.commit(); // Commit transaction
                } catch (SQLException e) {
                    conn.rollback(); // Rollback on error
                    e.printStackTrace();
                } finally {
                    conn.setAutoCommit(true); // Restore default commit behavior
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void notifyAndClearWaitlistedStudents(Connection conn, String courseId) throws SQLException {
        // Add all student_ids for that course_id to Notifications
        String addNotificationQuery = "INSERT INTO Notifications (user_id, message) " +
                                      "SELECT student_id, 'Course capacity reached' " +
                                      "FROM Enrollments WHERE course_id = ?";
        try (PreparedStatement notifyStmt = conn.prepareStatement(addNotificationQuery)) {
            notifyStmt.setString(1, courseId);
            notifyStmt.executeUpdate();
        }

        // Delete all student_ids from Enrollments for that course_id
        String deleteAllQuery = "DELETE FROM Enrollments WHERE course_id = ?";
        try (PreparedStatement deleteAllStmt = conn.prepareStatement(deleteAllQuery)) {
            deleteAllStmt.setString(1, courseId);
            deleteAllStmt.executeUpdate();
        }
    }

    private static boolean isStudentIdValid(String studentId, String courseId) {
        String query = "SELECT * FROM Enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, studentId);
            pstmt.setString(2, courseId);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}