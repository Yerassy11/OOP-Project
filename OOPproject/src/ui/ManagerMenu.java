package ui;
import data.Database;
import enums.CourseType;
import models.*;
import java.util.List;
import java.util.Scanner;

public class ManagerMenu {
    private final Manager manager;
    private final Scanner scanner;
    private final Database db;
    public ManagerMenu(Manager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
        this.db = Database.getInstance();
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n========== Manager Menu ==========");
            System.out.println("Logged in as: " + manager.getFullName() +
                    " [" + manager.getType() + "]");
            System.out.println("1.  Approve student course registration");
            System.out.println("2.  Assign teacher to course");
            System.out.println("3.  Add new course");
            System.out.println("4.  View all students (by GPA)");
            System.out.println("5.  View all students (alphabetically)");
            System.out.println("6.  View teachers by rating");
            System.out.println("7.  Create academic performance report");
            System.out.println("8.  Manage news");
            System.out.println("9.  View pending complaints");
            System.out.println("10. View inbox");
            System.out.println("0.  Logout");
            System.out.print("Choice: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":  approveRegistration(); break;
                case "2":  assignTeacher(); break;
                case "3":  addCourse(); break;
                case "4":  manager.viewStudentsSortedByGpa(db.getStudents()); break;
                case "5":  manager.viewStudentsAlphabetically(db.getStudents()); break;
                case "6":  manager.viewTeachersSortedByRating(db.getTeachers()); break;
                case "7":  System.out.println(manager.createReport(db.getStudents())); break;
                case "8":  addNews(); break;
                case "9":  manager.viewRequests(); break;
                case "10": manager.viewInbox(); break;
                case "0":  running = false; manager.logout(); break;
                default:   System.out.println("Invalid option.");
            }
        }
    }

    private void approveRegistration() {
        List<Student> students = db.getStudents();
        if (students.isEmpty()) { System.out.println("No students."); return; }
        System.out.println("=== Students ===");
        for (int i = 0; i < students.size(); i++) {
            System.out.println("[" + (i+1) + "] " + students.get(i).getFullName() +
                    " (credits: " + students.get(i).getCredits() + ")");
        }
        System.out.print("Choose student: ");
        try {
            int sIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (sIdx < 0 || sIdx >= students.size()) { System.out.println("Invalid."); return; }

            List<Course> courses = db.getCourses();
            System.out.println("=== Courses ===");
            for (int i = 0; i < courses.size(); i++) {
                System.out.println("[" + (i+1) + "] " + courses.get(i).getName() +
                        " (" + courses.get(i).getCredits() + " credits)");
            }
            System.out.print("Choose course: ");
            int cIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (cIdx < 0 || cIdx >= courses.size()) { System.out.println("Invalid."); return; }

            manager.approveRegistration(students.get(sIdx), courses.get(cIdx));
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private void assignTeacher() {
        List<Teacher> teachers = db.getTeachers();
        List<Course> courses = db.getCourses();
        if (teachers.isEmpty() || courses.isEmpty()) {
            System.out.println("Need at least one teacher and one course."); return;
        }
        System.out.println("=== Teachers ===");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println("[" + (i+1) + "] " + teachers.get(i).getFullName());
        }
        System.out.print("Choose teacher: ");
        try {
            int tIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (tIdx < 0 || tIdx >= teachers.size()) { System.out.println("Invalid."); return; }

            System.out.println("=== Courses ===");
            for (int i = 0; i < courses.size(); i++) {
                System.out.println("[" + (i+1) + "] " + courses.get(i).getName());
            }
            System.out.print("Choose course: ");
            int cIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (cIdx < 0 || cIdx >= courses.size()) { System.out.println("Invalid."); return; }

            manager.assignCourse(teachers.get(tIdx), courses.get(cIdx));
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private void addCourse() {
        try {
            System.out.print("Course name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Credits: ");
            int credits = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("Type: 1=MAJOR  2=MINOR  3=FREE_ELECTIVE");
            System.out.print("Choice: ");
            int t = Integer.parseInt(scanner.nextLine().trim());
            CourseType type = t == 2 ? CourseType.MINOR :
                    t == 3 ? CourseType.FREE_ELECTIVE : CourseType.MAJOR;
            System.out.print("For year of study (1-4): ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Major: ");
            String major = scanner.nextLine().trim();

            Course course = new Course(name, credits, type, year, major);
            db.addCourse(course);
            manager.addCourseForRegistration(course);
            System.out.println("Course created: " + course);
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private void addNews() {
        System.out.print("News title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Body: ");
        String body = scanner.nextLine().trim();
        System.out.println("Topic: 1=RESEARCH  2=ACADEMIC  3=ADMINISTRATIVE  4=GENERAL");
        System.out.print("Choice: ");
        try {
            int t = Integer.parseInt(scanner.nextLine().trim());
            university.enums.NewsTopic topic;
            switch (t) {
                case 1: topic = university.enums.NewsTopic.RESEARCH; break;
                case 2: topic = university.enums.NewsTopic.ACADEMIC; break;
                case 3: topic = university.enums.NewsTopic.ADMINISTRATIVE; break;
                default: topic = university.enums.NewsTopic.GENERAL;
            }
            News news = new News(title, body, topic);
            db.addNews(news);
            manager.manageNews(news);
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }
}