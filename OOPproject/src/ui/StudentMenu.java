package ui;
import data.Database;
import enums.CitationFormat;
import models.*;
import patterns.PaperComparators;
import java.util.List;
import java.util.Scanner;
public class StudentMenu {
    private final Student student;
    private final Scanner scanner;
    private final Database db;
    public StudentMenu(Student student, Scanner scanner) {
        this.student = student;
        this.scanner = scanner;
        this.db = Database.getInstance();
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n========== Student Menu ==========");
            System.out.println("Logged in as: " + student.getFullName());
            System.out.println("1. View available courses");
            System.out.println("2. Register for a course");
            System.out.println("3. View my courses");
            System.out.println("4. View my marks");
            System.out.println("5. Get transcript");
            System.out.println("6. Rate a teacher");
            System.out.println("7. View news feed");
            System.out.println("8. Submit tech request");
            System.out.println("9. View my info");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": viewAvailableCourses(); break;
                case "2": registerCourse(); break;
                case "3": viewMyCourses(); break;
                case "4": viewMarks(); break;
                case "5": student.getTranscript().print(); break;
                case "6": rateTeacher(); break;
                case "7": viewNews(); break;
                case "8": submitTechRequest(); break;
                case "9": System.out.println(student); break;
                case "0": running = false; student.logout(); break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void viewAvailableCourses() {
        List<Course> courses = db.getCourses();
        if (courses.isEmpty()) { System.out.println("No courses available."); return; }
        System.out.println("=== Available Courses ===");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            System.out.printf("[%d] %s | %d credits | %s | Year %d | Major: %s%n",
                    i + 1, c.getName(), c.getCredits(), c.getType(), c.getYear(), c.getMajor());
        }
    }

    private void registerCourse() {
        viewAvailableCourses();
        List<Course> courses = db.getCourses();
        System.out.print("Enter course number: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= courses.size()) { System.out.println("Invalid number."); return; }
            student.registerCourse(courses.get(idx));
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
        } catch (Exception e) {
            System.out.println("Cannot register: " + e.getMessage());
        }
    }

    private void viewMyCourses() {
        List<Course> myCourses = student.getCourses();
        if (myCourses.isEmpty()) { System.out.println("You are not enrolled in any courses."); return; }
        System.out.println("=== My Courses ===");
        for (Course c : myCourses) {
            System.out.println("  " + c.getName() + " (" + c.getCredits() + " credits)");
            System.out.println("    Instructors: ");
            for (Teacher t : c.getInstructors()) {
                System.out.println("      - " + t.getFullName() + " [" + t.getPosition() + "]");
            }
        }
    }

    private void viewMarks() {
        System.out.println("=== My Marks ===");
        boolean found = false;
        for (Course c : student.getCourses()) {
            Mark m = c.getMarkForStudent(student);
            if (m != null) {
                System.out.println(m);
                found = true;
            }
        }
        if (!found) System.out.println("No marks recorded yet.");
    }

    private void rateTeacher() {
        List<Teacher> teachers = db.getTeachers();
        if (teachers.isEmpty()) { System.out.println("No teachers found."); return; }
        System.out.println("=== Teachers ===");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println("[" + (i+1) + "] " + teachers.get(i).getFullName());
        }
        System.out.print("Choose teacher: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= teachers.size()) { System.out.println("Invalid."); return; }
            System.out.print("Rating (1.0 - 5.0): ");
            double score = Double.parseDouble(scanner.nextLine().trim());
            student.rateTeacher(teachers.get(idx), score);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void viewNews() {
        List<News> news = db.getNewsFeed();
        if (news.isEmpty()) { System.out.println("No news."); return; }
        System.out.println("=== News Feed ===");
        for (News n : news) {
            System.out.println(n);
            System.out.println("  " + n.getBody());
        }
    }

    private void submitTechRequest() {
        System.out.print("Describe your issue: ");
        String desc = scanner.nextLine().trim();
        if (desc.isEmpty()) { System.out.println("Description cannot be empty."); return; }
        TechRequest req = new TechRequest(student, desc);
        db.submitTechRequest(req);
        System.out.println("Request submitted: " + req.getRequestId());
    }
}
