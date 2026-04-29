package data;

import enums.*;
import models.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DataSeeder {
    public static void seed() {
        Database db = Database.getInstance();
        Admin admin = new Admin("Alice", "Admin", "admin@uni.kz",
                "admin123", "IT", 500000, 5);
        db.addUser(admin);

        Teacher t1 = new Teacher("John", "Smith", "j.smith@uni.kz",
                "pass123", "CS", 350000, TeacherPosition.PROFESSOR);
        Teacher t2 = new Teacher("Maria", "Lee", "m.lee@uni.kz",
                "pass123", "Math", 300000, TeacherPosition.SENIOR_LECTOR);
        Teacher t3 = new Teacher("Arman", "Bekov", "a.bekov@uni.kz",
                "pass123", "CS", 250000, TeacherPosition.LECTOR);
        db.addUser(t1);
        db.addUser(t2);
        db.addUser(t3);

        Manager manager = new Manager("Diana", "Park", "d.park@uni.kz",
                "pass123", "OR", 400000, ManagerType.OR);
        db.addUser(manager);

        TechSupportSpecialist tech = new TechSupportSpecialist(
                "Bob", "Tech", "b.tech@uni.kz", "pass123", "IT", 220000);
        db.addUser(tech);

        Student s1 = new Student("Asel", "Nurova", "a.nurova@uni.kz",
                "pass123", 2, "Computer Science");
        Student s2 = new Student("Daniyar", "Seitkali", "d.seitkali@uni.kz",
                "pass123", 4, "Computer Science");
        Student s3 = new Student("Zarina", "Akhmetova", "z.akhmet@uni.kz",
                "pass123", 1, "Mathematics");
        db.addUser(s1);
        db.addUser(s2);
        db.addUser(s3);

        Course oop = new Course("OOP", 5, CourseType.MAJOR, 2, "Computer Science");
        Course calculus = new Course("Calculus", 4, CourseType.MAJOR, 1, "Mathematics");
        Course algo = new Course("Algorithms", 5, CourseType.MAJOR, 3, "Computer Science");
        db.addCourse(oop);
        db.addCourse(calculus);
        db.addCourse(algo);

        manager.assignCourse(t1, oop);
        manager.assignCourse(t3, oop);   // two instructors for OOP
        manager.assignCourse(t2, calculus);
        manager.assignCourse(t1, algo);

        try {
            s1.registerCourse(oop);
            s2.registerCourse(oop);
            s2.registerCourse(algo);
            s3.registerCourse(calculus);
        } catch (Exception e) {
            System.out.println("Seed enroll error: " + e.getMessage());
        }

        t1.putMark(s1, oop, 25, 28, 38);
        t1.putMark(s2, oop, 20, 22, 30);
        t1.putMark(s2, algo, 30, 30, 35);
        t2.putMark(s3, calculus, 18, 20, 25);

        s1.updateGpa(oop.getMarks());
        s2.updateGpa(algo.getMarks());

        Researcher r1 = new Researcher(t1);
        ResearchPaper paper1 = new ResearchPaper(
                "Deep Learning Approaches in NLP",
                Arrays.asList("John Smith", "Maria Lee"),
                "IEEE Transactions on Neural Networks",
                "10.1109/TNN.2023.001",
                42, 12, LocalDate.of(2023, 5, 10), "NLP, deep learning, transformers"
        );
        ResearchPaper paper2 = new ResearchPaper(
                "Graph Algorithms for Large-Scale Networks",
                Arrays.asList("John Smith"),
                "ACM Computing Surveys",
                "10.1145/ACM.2022.002",
                87, 20, LocalDate.of(2022, 11, 3), "graphs, algorithms, scalability"
        );
        ResearchPaper paper3 = new ResearchPaper(
                "Quantum Computing Fundamentals",
                Arrays.asList("John Smith", "Arman Bekova"),
                "Nature Quantum Information",
                "10.1038/NQI.2021.003",
                150, 8, LocalDate.of(2021, 2, 14), "quantum, computing, qubits"
        );
        r1.publishPaper(paper1);
        r1.publishPaper(paper2);
        r1.publishPaper(paper3);
        db.addResearcher(r1);
        db.addPaper(paper1);
        db.addPaper(paper2);
        db.addPaper(paper3);

        Researcher r2 = new Researcher(t2);
        ResearchPaper paper4 = new ResearchPaper(
                "Stochastic Methods in Optimization",
                Arrays.asList("Maria Lee"),
                "SIAM Journal on Optimization",
                "10.1137/SIAM.2023.004",
                19, 15, LocalDate.of(2023, 1, 20), "optimization, stochastic, gradient"
        );
        r2.publishPaper(paper4);
        db.addResearcher(r2);
        db.addPaper(paper4);

        r1.calculateHIndex();
        try {
            s2.assignSupervisor(r1);
        } catch (Exception e) {
            System.out.println("Supervisor seed error: " + e.getMessage());
        }

        ResearchProject project = new ResearchProject("AI in Education");
        try {
            project.addParticipant(r1);
            project.addParticipant(r2);
            project.addPaper(paper1);
        } catch (Exception e) {
            System.out.println("Project seed error: " + e.getMessage());
        }
        db.addResearchProject(project);

        Journal journal = new Journal("University Research Bulletin");
        journal.subscribe(t1);
        journal.subscribe(s2);
        journal.publishPaper(paper2);
        db.addJournal(journal);

        News news1 = new News("Paper published by Prof. Smith",
                "Prof. Smith published a new paper on deep learning.", NewsTopic.RESEARCH);
        News news2 = new News("Spring semester schedule",
                "Classes start April 27.", NewsTopic.ACADEMIC);
        db.addNews(news1);
        db.addNews(news2);

        TechRequest tr = new TechRequest(s1, "Cannot access the student portal");
        db.submitTechRequest(tr);
        tech.assignRequest(tr);

        System.out.println("[Seeder] Demo data loaded successfully.");
    }
}
