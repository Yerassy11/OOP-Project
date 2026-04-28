package ui;

import data.Database;
import models.*;
import patterns.UserFactory;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final Admin admin;
    private final Scanner scanner;
    private final Database db;
    public AdminMenu(Admin admin, Scanner scanner) {
        this.admin = admin;
        this.scanner = scanner;
        this.db = Database.getInstance();
    }
    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n========== Admin Menu ==========");
            System.out.println("1. Add user");
            System.out.println("2. Remove user");
            System.out.println("3. Update user info");
            System.out.println("4. List all users");
            System.out.println("5. View system logs");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": addUser(); break;
                case "2": removeUser(); break;
                case "3": updateUser(); break;
                case "4": listUsers(); break;
                case "5": admin.printLogs(); break;
                case "0": running = false; admin.logout(); break;
                default:  System.out.println("Invalid option.");
            }
        }
    }
    private void addUser() {
        System.out.println("Roles: STUDENT | TEACHER | MANAGER | ADMIN | TECH_SUPPORT | MASTER | PHD");
        System.out.print("Role: ");
        String roleStr = scanner.nextLine().trim();
        UserFactory.Role role;
        try {
            role = UserFactory.parseRole(roleStr);
        } catch (Exception e) {
            System.out.println("Unknown role."); return;
        }
        System.out.print("First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Department/Major: ");
        String dept = scanner.nextLine().trim();
        double salary = 0;
        if (role != UserFactory.Role.STUDENT && role != UserFactory.Role.MASTER_STUDENT
                && role != UserFactory.Role.PHD_STUDENT) {
            System.out.print("Salary: ");
            try { salary = Double.parseDouble(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { salary = 0; }
        }

        User user = UserFactory.create(role, firstName, lastName, email, password, dept, salary);
        admin.addUser(user);
    }

    private void removeUser() {
        listUsers();
        System.out.print("Enter email of user to remove: ");
        String email = scanner.nextLine().trim();
        db.findUserByEmail(email).ifPresentOrElse(
                u -> admin.removeUser(u),
                () -> System.out.println("User not found.")
        );
    }

    private void updateUser() {
        listUsers();
        System.out.print("Enter email of user to update: ");
        String email = scanner.nextLine().trim();
        db.findUserByEmail(email).ifPresentOrElse(u -> {
            System.out.print("New first name (blank = keep): ");
            String fn = scanner.nextLine().trim();
            System.out.print("New last name (blank = keep): ");
            String ln = scanner.nextLine().trim();
            System.out.print("New email (blank = keep): ");
            String em = scanner.nextLine().trim();
            admin.updateUser(u,
                    fn.isEmpty() ? u.getFirstName() : fn,
                    ln.isEmpty() ? u.getLastName() : ln,
                    em.isEmpty() ? u.getEmail() : em);
        }, () -> System.out.println("User not found."));
    }

    private void listUsers() {
        List<User> users = db.getUsers();
        System.out.println("=== All Users (" + users.size() + ") ===");
        for (User u : users) {
            System.out.println("  [" + u.getClass().getSimpleName() + "] " +
                    u.getFullName() + " <" + u.getEmail() + ">");
        }
    }
}
