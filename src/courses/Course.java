package university.model.courses;

import university.model.enums.CourseType;
import university.model.enums.School;
import university.model.users.Student;
import university.model.users.Teacher;

import java.io.Serializable;
import java.util.*;

public class Course implements Serializable, Comparable<Course> {

    private static final long serialVersionUID = 1L;

    private final String courseId;
    private String name;
    private int credits;
    private CourseType type;
    private School targetSchool;
    private int targetYear;

    private final List<Teacher> instructors; // can be multiple (lecture vs practice)
    private final List<Student> students;
    private final List<Lesson>  lessons;
    private final List<Mark>    marks;

    public Course(String name, int credits, CourseType type, School targetSchool, int targetYear) {
        this.courseId     = "CRS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.name         = name;
        this.credits      = credits;
        this.type         = type;
        this.targetSchool = targetSchool;
        this.targetYear   = targetYear;
        this.instructors  = new ArrayList<>();
        this.students     = new ArrayList<>();
        this.lessons      = new ArrayList<>();
        this.marks        = new ArrayList<>();
    }

    public void addInstructor(Teacher t)    { if (!instructors.contains(t)) instructors.add(t); }
    public void enrollStudent(Student s)    { if (!students.contains(s)) students.add(s); }
    public void addLesson(Lesson l)         { lessons.add(l); }
    public void addMark(Mark m)             {
        marks.add(m);
        m.getStudent().addMark(m);
    }

    public void viewLessons() {
        System.out.println("\n=== Lessons for " + name + " ===");
        lessons.forEach(System.out::println);
    }

    @Override
    public int compareTo(Course other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        return Objects.equals(courseId, ((Course) o).courseId);
    }

    @Override
    public int hashCode() { return Objects.hash(courseId); }

    @Override
    public String toString() {
        return "Course{id='" + courseId + "', name='" + name + "', credits=" + credits +
                ", type=" + type + ", school=" + targetSchool + ", year=" + targetYear + "}";
    }


    public String getCourseId()            { return courseId; }
    public String getName()                { return name; }
    public int getCredits()                { return credits; }
    public CourseType getType()            { return type; }
    public School getTargetSchool()        { return targetSchool; }
    public int getTargetYear()             { return targetYear; }
    public List<Teacher> getInstructors()  { return Collections.unmodifiableList(instructors); }
    public List<Student> getStudents()     { return Collections.unmodifiableList(students); }
    public List<Lesson> getLessons()       { return Collections.unmodifiableList(lessons); }
    public List<Mark> getMarks()           { return Collections.unmodifiableList(marks); }

    public void setName(String n)          { this.name = n; }
    public void setCredits(int c)          { this.credits = c; }
    public void setType(CourseType t)      { this.type = t; }
}