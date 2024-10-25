import java.util.Scanner;

public class Faculty {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean applicationRunning = true;

        while (applicationRunning) {
            boolean isLoggedIn = false;

            // Home Page: Faculty login
            while (!isLoggedIn) {
                System.out.println("=== Faculty Login ===");
                System.out.print("Enter User ID: ");
                String userId = scanner.nextLine();

                System.out.print("Enter Password: ");
                String password = scanner.nextLine();

                // Simulate a credential check
                if (validateCredentials(userId, password)) {
                    System.out.println("Login Successful!");
                    // After successful login, show the Sign-In/Go Back menu
                    boolean isMenuActive = true;
                    while (isMenuActive) {
                        System.out.println("\n1. Sign-In");
                        System.out.println("2. Go Back");
                        System.out.print("Enter choice (1-2): ");
                        int menuChoice = scanner.nextInt();
                        scanner.nextLine();  // Consume newline character

                        switch (menuChoice) {
                            case 1:
                                // Proceed to Faculty Landing Page
                                isLoggedIn = true;  // Set logged in status to true
                                isMenuActive = false;  // Exit the Sign-In menu
                                break;
                            case 2:
                                // Go back to Home Page
                                System.out.println("Going back to Home Page...");
                                isMenuActive = false;
                                break;
                            default:
                                System.out.println("Invalid choice! Please select a valid option.");
                        }
                    }
                } else {
                    System.out.println("Login Incorrect. Please try again.");
                }
            }

            // Faculty landing page
            if (isLoggedIn) {
                boolean continueSession = true;
                while (continueSession) {
                    // Display menu
                    System.out.println("\n=== Faculty Landing Page ===");
                    System.out.println("1. Go to Active Course");
                    System.out.println("2. Go to Evaluation Course");
                    System.out.println("3. View Courses");
                    System.out.println("4. Change Password");
                    System.out.println("5. Logout");
                    System.out.print("Enter choice (1-5): ");

                    int choice = scanner.nextInt();
                    scanner.nextLine();  // Consume newline character

                    switch (choice) {
                        case 1:
                            activeCourseMenu(scanner);
                            break;
                        case 2:
                            evaluationCourseMenu(scanner);
                            break;
                        case 3:
                            viewCourses(scanner);  // Updated viewCourses with a parameter
                            break;
                        case 4:
                            changePassword(scanner);  // Call the changePassword method
                            break;
                        case 5:
                            System.out.println("Logging out... Returning to Home Page.");
                            continueSession = false;  // Break the landing page loop
                            break;
                        default:
                            System.out.println("Invalid choice! Please select a valid option.");
                    }
                }
            }
        }

        scanner.close();
    }

    // Placeholder for credential validation
    public static boolean validateCredentials(String userId, String password) {
        return userId.equals("faculty123") && password.equals("password");
    }

    // View Courses: Display assigned courses and give option to go back
    public static void viewCourses(Scanner scanner) {
        // Display assigned courses
        System.out.println("=== Assigned Courses ===");
        String[] courses = {"Course 101: Data Structures", "Course 102: Algorithms", "Course 103: Database Systems"};

        // Display the list of courses
        for (int i = 0; i < courses.length; i++) {
            System.out.println((i + 1) + ". " + courses[i]);
        }

        // After displaying the courses, offer the option to go back
        boolean continueViewCourses = true;
        while (continueViewCourses) {
            System.out.println("\n1. Go Back");
            System.out.print("Enter choice (1 to Go Back): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 1) {
                System.out.println("Returning to Faculty Landing Page...");
                continueViewCourses = false;  // Exit the view courses loop
            } else {
                System.out.println("Invalid choice! Please select 1 to go back.");
            }
        }
    }

    // Change Password: Implements the change password functionality
    public static void changePassword(Scanner scanner) {
        boolean inChangePassword = true;
        while (inChangePassword) {
            // Display the change password form
            System.out.println("=== Change Password ===");
            System.out.print("Enter current password: ");
            String currentPassword = scanner.nextLine();

            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();

            System.out.print("Confirm new password: ");
            String confirmPassword = scanner.nextLine();

            // Display the menu
            System.out.println("\n1. Update");
            System.out.println("2. Go Back");
            System.out.print("Enter choice (1-2): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            switch (choice) {
                case 1:
                    // Check if the current password is correct (for demo purposes)
                    if (currentPassword.equals("password")) {
                        // Check if new password matches the confirmation
                        if (newPassword.equals(confirmPassword)) {
                            // Simulate updating the password
                            System.out.println("Password updated successfully!");
                            inChangePassword = false;  // Exit the change password process
                        } else {
                            System.out.println("New passwords do not match. Please try again.");
                        }
                    } else {
                        System.out.println("Current password is incorrect. Please try again.");
                    }
                    break;
                case 2:
                    // Go back to the Faculty Landing Page
                    System.out.println("Returning to Faculty Landing Page...");
                    inChangePassword = false;  // Exit the change password process
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to update or 2 to go back.");
            }
        }
    }

    // Active Course Menu
    public static void activeCourseMenu(Scanner scanner) {
        System.out.println("Enter Course ID: ");
        String courseId = scanner.nextLine();

        boolean inActiveCourse = true;
        while (inActiveCourse) {
            System.out.println("\n=== Active Course Menu ===");
            System.out.println("1. View Worklist");
            System.out.println("2. Approve Enrollment");
            System.out.println("3. View Students");
            System.out.println("4. Add New Chapter");
            System.out.println("5. Modify Chapters");
            System.out.println("6. Add TA");
            System.out.println("7. Go Back");
            System.out.print("Enter choice (1-7): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    viewWorklist(scanner);  // Updated to handle Go Back
                    break;
                case 2:
                    approveEnrollment(scanner);  // Updated to handle save and cancel
                    break;
                case 3:
                    viewStudents(scanner);  // Updated to handle Go Back
                    break;
                case 4:
                    addNewChapter(scanner);  // Updated for new functionality
                    break;
                case 5:
                    modifyChapters(scanner);  // Implemented modifyChapters method
                    break;
                case 6:
                    addTA(scanner);  // Updated to handle save and cancel
                    break;
                case 7:
                    inActiveCourse = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // Faculty: Modify Chapters: Offers options for hiding, deleting, adding, and modifying sections
    public static void modifyChapters(Scanner scanner) {
        System.out.print("Enter Unique Chapter ID: ");
        String chapterId = scanner.nextLine();

        boolean inModifyChapters = true;
        while (inModifyChapters) {
            System.out.println("\n=== Modify Chapter ===");
            System.out.println("1. Hide Chapter");
            System.out.println("2. Delete Chapter");
            System.out.println("3. Add New Section");
            System.out.println("4. Modify Section");
            System.out.println("5. Go Back");
            System.out.print("Enter choice (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    hideChapter(scanner, chapterId);  // Functionality to hide chapter
                    break;
                case 2:
                    deleteChapter(scanner, chapterId);  // Functionality to delete chapter
                    break;
                case 3:
                    addNewSection(scanner);  // Reuse the addNewSection function for adding new section
                    break;
                case 4:
                    modifySection(scanner);  // New functionality to modify sections
                    break;
                case 5:
                    System.out.println("Returning to previous menu...");
                    inModifyChapters = false;  // Exit and return to the previous menu
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    // Hide Chapter Functionality
    public static void hideChapter(Scanner scanner, String chapterId) {
        boolean inHideChapter = true;
        while (inHideChapter) {
            System.out.println("\n=== Hide Chapter ===");
            System.out.println("1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Chapter '" + chapterId + "' has been hidden.");
                    inHideChapter = false;
                    break;
                case 2:
                    System.out.println("Cancelling hide operation and returning to Modify Chapters menu...");
                    inHideChapter = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Delete Chapter Functionality
    public static void deleteChapter(Scanner scanner, String chapterId) {
        boolean inDeleteChapter = true;
        while (inDeleteChapter) {
            System.out.println("\n=== Delete Chapter ===");
            System.out.println("1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Chapter '" + chapterId + "' has been deleted.");
                    inDeleteChapter = false;
                    break;
                case 2:
                    System.out.println("Cancelling delete operation and returning to Modify Chapters menu...");
                    inDeleteChapter = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Modify Section Functionality: Offers options to hide, delete, add new content, modify content, or go back
    public static void modifySection(Scanner scanner) {
        System.out.print("Enter Section Number to Modify: ");
        String sectionNumber = scanner.nextLine();

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
                    hideSection(scanner, sectionNumber);  // Functionality to hide section
                    break;
                case 2:
                    deleteSection(scanner, sectionNumber);  // Functionality to delete section
                    break;
                case 3:
                    addNewContentBlock(scanner);  // Functionality to add new content block
                    break;
                case 4:
                    modifyContentBlock(scanner);  // Functionality to modify content block
                    break;
                case 5:
                    System.out.println("Returning to previous menu...");
                    inModifySection = false;  // Exit and return to the previous menu
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }


    // Hide Section Functionality
    public static void hideSection(Scanner scanner, String sectionNumber) {
        boolean inHideSection = true;
        while (inHideSection) {
            System.out.println("\n=== Hide Section ===");
            System.out.println("1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Section '" + sectionNumber + "' has been hidden.");
                    inHideSection = false;
                    break;
                case 2:
                    System.out.println("Cancelling hide operation and returning to Modify Section menu...");
                    inHideSection = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Delete Section Functionality
    public static void deleteSection(Scanner scanner, String sectionNumber) {
        boolean inDeleteSection = true;
        while (inDeleteSection) {
            System.out.println("\n=== Delete Section ===");
            System.out.println("1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Section '" + sectionNumber + "' has been deleted.");
                    inDeleteSection = false;
                    break;
                case 2:
                    System.out.println("Cancelling delete operation and returning to Modify Section menu...");
                    inDeleteSection = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Add New Content Block Functionality
    public static void addNewContentBlock(Scanner scanner) {
        System.out.print("Enter Content Block ID: ");
        String contentBlockId = scanner.nextLine();

        boolean inAddContentBlock = true;
        while (inAddContentBlock) {
            System.out.println("\n=== Add New Content Block ===");
            System.out.println("1. Add Text");
            System.out.println("2. Add Picture");
            System.out.println("3. Add Activity");
            System.out.println("4. Go Back");
            System.out.print("Enter choice (1-4): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addText(scanner, contentBlockId);  // Integrated Add Text functionality
                    break;
                case 2:
                    addPicture(scanner, contentBlockId);  // Integrated Add Picture functionality
                    break;
                case 3:
                    addActivity(scanner, contentBlockId);  // Integrated Add Activity functionality
                    break;
                case 4:
                    System.out.println("Returning to previous menu...");
                    inAddContentBlock = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
        }
    }

    // Modify Content Block Functionality
    public static void modifyContentBlock(Scanner scanner) {
        System.out.print("Enter Content Block ID to Modify: ");
        String contentBlockId = scanner.nextLine();

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
                    hideContentBlock(scanner, contentBlockId);  // Hide Content Block
                    break;
                case 2:
                    deleteContentBlock(scanner, contentBlockId);  // Delete Content Block
                    break;
                case 3:
                    addText(scanner, contentBlockId);  // Add Text functionality
                    break;
                case 4:
                    addPicture(scanner, contentBlockId);  // Add Picture functionality
                    break;
                case 5:
                    hideActivity(scanner, contentBlockId);  // Hide Activity
                    break;
                case 6:
                    deleteActivity(scanner, contentBlockId);  // Delete Activity
                    break;
                case 7:
                    addActivity(scanner, contentBlockId);  // Add Activity
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

    // Faculty: Add Text inside Modify Content Block
    public static void addText(Scanner scanner, String contentBlockId) {
        System.out.print("Enter Text: ");
        String text = scanner.nextLine();

        boolean inAddText = true;
        while (inAddText) {
            System.out.println("\n1. ADD");
            System.out.println("2. Go Back");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    if (!text.isEmpty()) {
                        System.out.println("Text added to Content Block '" + contentBlockId + "' successfully!");
                    } else {
                        System.out.println("Failed to add text.");
                    }
                    inAddText = false;
                    break;
                case 2:
                    System.out.println("Discarding input and going back...");
                    inAddText = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to add or 2 to go back.");
            }
        }
    }

    // Faculty: Add Picture inside Modify Content Block
    public static void addPicture(Scanner scanner, String contentBlockId) {
        System.out.print("Enter Picture URL: ");
        String url = scanner.nextLine();

        boolean inAddPicture = true;
        while (inAddPicture) {
            System.out.println("\n1. ADD");
            System.out.println("2. Go Back");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    if (!url.isEmpty()) {
                        System.out.println("Picture added to Content Block '" + contentBlockId + "' successfully!");
                    } else {
                        System.out.println("Failed to add picture.");
                    }
                    inAddPicture = false;
                    break;
                case 2:
                    System.out.println("Discarding input and going back...");
                    inAddPicture = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to add or 2 to go back.");
            }
        }
    }

    // Faculty: Add Activity inside Modify Content Block
    public static void addActivity(Scanner scanner, String contentBlockId) {
        System.out.print("Enter Activity ID: ");
        String activityId = scanner.nextLine();

        boolean inAddActivity = true;
        while (inAddActivity) {
            System.out.println("\n1. Add Question");
            System.out.println("2. Go Back");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    //System.out.println("Navigating to question page for Activity ID: " + activityId + " in Content Block '" + contentBlockId + "'");
                    addQuestion(scanner);
                    inAddActivity = false;
                    break;
                case 2:
                    System.out.println("Going back...");
                    inAddActivity = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to add question or 2 to go back.");
            }
        }
    }

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

    // Faculty: Hide Activity inside Modify Content Block
    public static void hideActivity(Scanner scanner, String contentBlockId) {
        System.out.print("Enter Activity ID to Hide: ");
        String activityId = scanner.nextLine();

        boolean inHideActivity = true;
        while (inHideActivity) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Activity '" + activityId + "' hidden in Content Block '" + contentBlockId + "'");
                    inHideActivity = false;
                    break;
                case 2:
                    System.out.println("Cancelling hide operation and returning to Modify Content Block menu...");
                    inHideActivity = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Faculty: Delete Activity inside Modify Content Block
    public static void deleteActivity(Scanner scanner, String contentBlockId) {
        System.out.print("Enter Activity ID to Delete: ");
        String activityId = scanner.nextLine();

        boolean inDeleteActivity = true;
        while (inDeleteActivity) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Activity '" + activityId + "' deleted from Content Block '" + contentBlockId + "'");
                    inDeleteActivity = false;
                    break;
                case 2:
                    System.out.println("Cancelling delete operation and returning to Modify Content Block menu...");
                    inDeleteActivity = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Hide Content Block Functionality
    public static void hideContentBlock(Scanner scanner, String contentBlockId) {
        boolean inHideContentBlock = true;
        while (inHideContentBlock) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Content Block '" + contentBlockId + "' has been hidden.");
                    inHideContentBlock = false;
                    break;
                case 2:
                    System.out.println("Cancelling hide operation and returning to Modify Content Block menu...");
                    inHideContentBlock = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Delete Content Block Functionality
    public static void deleteContentBlock(Scanner scanner, String contentBlockId) {
        boolean inDeleteContentBlock = true;
        while (inDeleteContentBlock) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Content Block '" + contentBlockId + "' has been deleted.");
                    inDeleteContentBlock = false;
                    break;
                case 2:
                    System.out.println("Cancelling delete operation and returning to Modify Content Block menu...");
                    inDeleteContentBlock = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1 to Save or 2 to Cancel.");
            }
        }
    }

    // Faculty: Add TA: Get user input, offer save and cancel options
    public static void addTA(Scanner scanner) {
        System.out.print("Enter TA First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter TA Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter TA Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Default Password: ");
        String defaultPassword = scanner.nextLine();

        boolean inAddTA = true;
        while (inAddTA) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Simulate saving to the database
                    if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !defaultPassword.isEmpty()) {
                        System.out.println("TA '" + firstName + " " + lastName + "' added with email: " + email);
                        inAddTA = false;  // Exit and go back to Active Course Menu
                    } else {
                        System.out.println("Failed to add TA. Some fields are missing.");
                    }
                    break;
                case 2:
                    System.out.println("Cancelling and returning to Active Course Menu...");
                    inAddTA = false;  // Go back to Active Course Menu
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1 to save or 2 to cancel.");
            }
        }
    }

    // Faculty: Add New Chapter: Get input, allow adding a new section or going back
    public static void addNewChapter(Scanner scanner) {
        System.out.print("Enter Chapter ID: ");
        String chapterId = scanner.nextLine();

        System.out.print("Enter Chapter Title: ");
        String chapterTitle = scanner.nextLine();

        boolean inAddChapter = true;
        while (inAddChapter) {
            System.out.println("\n1. Add New Section");
            System.out.println("2. Go Back");
            System.out.print("Enter choice (1 to Add New Section, 2 to Go Back): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addNewSection(scanner);  // Go to Add New Section functionality
                    break;
                case 2:
                    System.out.println("Cancelling and returning to Active Course Menu...");
                    inAddChapter = false;  // Go back to Active Course Menu
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1 to add a section or 2 to go back.");
            }
        }
    }

    // Faculty: Add New Section: Get input, allow adding content or going back
    public static void addNewSection(Scanner scanner) {
        System.out.print("Enter Section Number: ");
        String sectionNumber = scanner.nextLine();

        System.out.print("Enter Section Title: ");
        String sectionTitle = scanner.nextLine();

        boolean inAddSection = true;
        while (inAddSection) {
            System.out.println("\n1. Add New Content Block");
            System.out.println("2. Go Back");
            System.out.print("Enter choice (1 to Add New Content Block, 2 to Go Back): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Navigating to Add New Content Block...");
                    addNewContentBlock(scanner);  // Add New Content Block functionality
                    break;
                case 2:
                    System.out.println("Cancelling and returning to Add New Chapter...");
                    inAddSection = false;  // Go back to Add New Chapter
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1 to add content or 2 to go back.");
            }
        }
    }

    // Faculty: Approve Enrollment: Get Student ID, offer save and cancel options, and go back to active course menu
    public static void approveEnrollment(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        boolean inApproveEnrollment = true;
        while (inApproveEnrollment) {
            System.out.println("\n1. Save");
            System.out.println("2. Cancel");
            System.out.print("Enter choice (1 to Save, 2 to Cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Simulate saving to the database
                    if (!studentId.isEmpty()) {
                        System.out.println("Enrollment approved for Student ID: " + studentId);
                        inApproveEnrollment = false;  // Go back to Active Course Menu
                    } else {
                        System.out.println("Failed to approve enrollment. Student ID missing.");
                    }
                    break;
                case 2:
                    System.out.println("Cancelling and returning to Active Course Menu...");
                    inApproveEnrollment = false;  // Go back to Active Course Menu
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1 to save or 2 to cancel.");
            }
        }
    }

    // Faculty: View Students: Display the list of students and go back to active course menu
    public static void viewStudents(Scanner scanner) {
        System.out.println("\n=== Students ===");
        String[] students = {"Student 1", "Student 2", "Student 3"};
        
        // Display the student list
        for (int i = 0; i < students.length; i++) {
            System.out.println((i + 1) + ". " + students[i]);
        }

        // Give option to go back
        boolean inViewStudents = true;
        while (inViewStudents) {
            System.out.println("\n1. Go Back");
            System.out.print("Enter choice (1 to Go Back): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 1) {
                System.out.println("Returning to Active Course Menu...");
                inViewStudents = false;  // Go back to Active Course Menu
            } else {
                System.out.println("Invalid choice. Please select 1 to go back.");
            }
        }
    }

    // Faculty: View Worklist: Display the waiting list and go back to active course menu
    public static void viewWorklist(Scanner scanner) {
        System.out.println("\n=== Worklist ===");
        String[] worklist = {"Student 1", "Student 2", "Student 3"};
        
        // Display the worklist
        for (int i = 0; i < worklist.length; i++) {
            System.out.println((i + 1) + ". " + worklist[i]);
        }

        // Give option to go back
        boolean inWorklist = true;
        while (inWorklist) {
            System.out.println("\n1. Go Back");
            System.out.print("Enter choice (1 to Go Back): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (choice == 1) {
                System.out.println("Returning to Active Course Menu...");
                inWorklist = false;  // Go back to Active Course Menu
            } else {
                System.out.println("Invalid choice. Please select 1 to go back.");
            }
        }
    }

    // Evaluation Course Menu
    public static void evaluationCourseMenu(Scanner scanner) {
        System.out.println("Enter Course ID: ");
        String courseId = scanner.nextLine();

        boolean inEvaluationCourse = true;
        while (inEvaluationCourse) {
            System.out.println("\n=== Evaluation Course Menu ===");
            System.out.println("1. Add New Chapter");
            System.out.println("2. Modify Chapters");
            System.out.println("3. Go Back");
            System.out.print("Enter choice (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addNewChapter(scanner);
                    break;
                case 2:
                    modifyChapters(scanner);
                    break;
                case 3:
                    inEvaluationCourse = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
