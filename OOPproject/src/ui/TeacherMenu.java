package ui;
import data.Database;
import enums.UrgencyLevel;
import models.*;
import java.util.List;
import java.util.Scanner;
public class TeacherMenu {
    private final Teacher teacher;
    private final Scanner scanner;
    private final Database db;
    public TeacherMenu(Teacher teacher, Scanner scanner) {
        this.teacher = teacher;
        this.scanner = scanner;
        this.db = Database.getInstance();
    }
    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n========== Teacher Menu ==========");
            System.out.println("Logged in as: " + teacher.getFullName() + " [" + teacher.getPosition() + "]");
            System.out.println("1. View my courses");
            System.out.println("2. View students in a course");
            System.out.println("3. Put mark");
            System.out.println("4. Generate grade report");
            System.out.println("5. Send message to employee");
            System.out.println("6. Send complaint");
            System.out.println("7. View inbox");
            System.out.println("8. View news");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": viewCourses(); break;
                case "2": viewStudentsInCourse(); break;
                case "3": putMark(); break;
                case "4": System.out.println(teacher.generateReport()); break;
                case "5": sendMessage(); break;
                case "6": sendComplaint(); break;
                case "7": teacher.viewInbox(); break;
                case "8": viewNews(); break;
                case "0": running = false; teacher.logout(); break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void viewCourses() {
        List<Course> courses = teacher.getCourses();
        if (courses.isEmpty()) { System.out.println("You have no assigned courses."); return; }
        System.out.println("=== My Courses ===");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println("[" + (i+1) + "] " + courses.get(i));
        }
    }

    private void viewStudentsInCourse() {
        List<Course> courses = teacher.getCourses();
        if (courses.isEmpty()) { System.out.println("No courses."); return; }
        viewCourses();
        System.out.print("Choose course: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= courses.size()) { System.out.println("Invalid."); return; }
            Course c = courses.get(idx);
            System.out.println("=== Students in " + c.getName() + " ===");
            if (c.getStudents().isEmpty()) { System.out.println("  No enrolled students."); return; }
            for (Student s : c.getStudents()) {
                System.out.println("  " + s);
            }
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private void putMark() {
        List<Course> courses = teacher.getCourses();
        if (courses.isEmpty()) { System.out.println("No courses."); return; }
        viewCourses();
        System.out.print("Choose course: ");
        try {
            int cIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (cIdx < 0 || cIdx >= courses.size()) { System.out.println("Invalid."); return; }
            Course course = courses.get(cIdx);

            List<Student> students = course.getStudents();
            if (students.isEmpty()) { System.out.println("No students."); return; }
            System.out.println("Students:");
            for (int i = 0; i < students.size(); i++) {
                System.out.println("[" + (i+1) + "] " + students.get(i).getFullName());
            }
            System.out.print("Choose student: ");
            int sIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (sIdx < 0 || sIdx >= students.size()) { System.out.println("Invalid."); return; }
            Student student = students.get(sIdx);

            System.out.print("ATT1 score (0-30): ");
            double att1 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("ATT2 score (0-30): ");
            double att2 = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Final exam score (0-40): ");
            double fin = Double.parseDouble(scanner.nextLine().trim());

            teacher.putMark(student, course, att1, att2, fin);
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private void sendMessage() {
        List<User> users = db.getUsers();
        System.out.println("=== All Employees ===");
        List<Employee> employees = new java.util.ArrayList<>();
        for (User u : users) {
            if (u instanceof Employee && !u.equals(teacher)) {
                employees.add((Employee) u);
                System.out.println("[" + employees.size() + "] " + u.getFullName());
            }
        }
        if (employees.isEmpty()) { System.out.println("No other employees."); return; }
        System.out.print("Choose recipient: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= employees.size()) { System.out.println("Invalid."); return; }
            System.out.print("Message: ");
            String msg = scanner.nextLine().trim();
            teacher.sendMessage(employees.get(idx), msg);
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private void sendComplaint() {
        List<Employee> employees = new java.util.ArrayList<>();
        for (User u : db.getUsers()) {
            if (u instanceof Employee && !u.equals(teacher)) employees.add((Employee) u);
        }
        System.out.println("Send complaint about:");
        for (int i = 0; i < employees.size(); i++) {
            System.out.println("[" + (i+1) + "] " + employees.get(i).getFullName());
        }
        System.out.print("Choose person: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= employees.size()) { System.out.println("Invalid."); return; }
            System.out.println("Urgency: 1=LOW, 2=MEDIUM, 3=HIGH");
            System.out.print("Choice: ");
            int u = Integer.parseInt(scanner.nextLine().trim());
            UrgencyLevel level = u == 3 ? UrgencyLevel.HIGH :
                    u == 2 ? UrgencyLevel.MEDIUM : UrgencyLevel.LOW;
            System.out.print("Description: ");
            String desc = scanner.nextLine().trim();
            teacher.sendComplaint(employees.get(idx), desc, level);
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private void viewNews() {
        List<News> news = db.getNewsFeed();
        if (news.isEmpty()) { System.out.println("No news."); return; }
        System.out.println("=== News ===");
        for (News n : news) System.out.println(n);
    }
}
