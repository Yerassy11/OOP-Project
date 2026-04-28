package data;

import models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Database implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "university_data.ser";

    private static Database instance;
    private List<User> users;
    private List<Course> courses;
    private List<Researcher> researchers;
    private List<ResearchProject> researchProjects;
    private List<ResearchPaper> allPapers;
    private List<News> newsFeed;
    private List<Journal> journals;
    private List<TechRequest> techRequests;
    private List<LogEntry> logs;
    private List<StudentOrganization> organizations;

    private Database() {
        users             = new ArrayList<>();
        courses           = new ArrayList<>();
        researchers       = new ArrayList<>();
        researchProjects  = new ArrayList<>();
        allPapers         = new ArrayList<>();
        newsFeed          = new ArrayList<>();
        journals          = new ArrayList<>();
        techRequests      = new ArrayList<>();
        logs              = new ArrayList<>();
        organizations     = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = loadFromDisk();
        }
        return instance;
    }

    public static void saveToDisk() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(instance);
            System.out.println("[DB] Data saved.");
        } catch (IOException e) {
            System.out.println("[DB] Save failed: " + e.getMessage());
        }
    }

    private static Database loadFromDisk() {
        File f = new File(FILE_PATH);
        if (!f.exists()) {
            System.out.println("[DB] No saved data found. Starting fresh.");
            return new Database();
        }
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(f))) {
            Database db = (Database) ois.readObject();
            System.out.println("[DB] Data loaded from disk.");
            return db;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[DB] Load failed, starting fresh: " + e.getMessage());
            return new Database();
        }
    }

    public void addUser(User user) {
        users.add(user);
        log(user.getEmail(), "Account created");
    }

    public void removeUser(User user) {
        users.remove(user);
        log(user.getEmail(), "Account removed");
    }

    public Optional<User> findUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<User> getUsers() { return users; }

    public List<Student> getStudents() {
        List<Student> result = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Student && !(u instanceof MasterStudent) && !(u instanceof PhDStudent)) {
                result.add((Student) u);
            }
        }
        return result;
    }

    public List<Teacher> getTeachers() {
        List<Teacher> result = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Teacher) result.add((Teacher) u);
        }
        return result;
    }

    public List<Manager> getManagers() {
        List<Manager> result = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Manager) result.add((Manager) u);
        }
        return result;
    }

    public void addCourse(Course course) { courses.add(course); }
    public void removeCourse(Course course) { courses.remove(course); }
    public List<Course> getCourses() { return courses; }

    public Optional<Course> findCourseByName(String name) {
        return courses.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public void addResearcher(Researcher r) { researchers.add(r); }
    public List<Researcher> getResearchers() { return researchers; }

    public void addResearchProject(ResearchProject p) { researchProjects.add(p); }
    public List<ResearchProject> getResearchProjects() { return researchProjects; }

    public void addPaper(ResearchPaper p) { allPapers.add(p); }
    public List<ResearchPaper> getAllPapers() { return allPapers; }

    public void addNews(News news) {
        newsFeed.add(news);
        newsFeed.sort(null); // uses News.compareTo — pinned first
    }
    public List<News> getNewsFeed() { return newsFeed; }

    public void addJournal(Journal j) { journals.add(j); }
    public List<Journal> getJournals() { return journals; }

    public void submitTechRequest(TechRequest r) { techRequests.add(r); }
    public List<TechRequest> getTechRequests() { return techRequests; }

    public void addOrganization(StudentOrganization org) { organizations.add(org); }
    public List<StudentOrganization> getOrganizations() { return organizations; }

    public void log(String userEmail, String action) {
        logs.add(new LogEntry(userEmail, action));
    }

    public List<LogEntry> getLogs() { return logs; }
}