package enums;

public enum TeacherPosition {
    TUTOR,
    LECTOR,
    SENIOR_LECTOR,
    PROFESSOR;

    public boolean isProfessor(){
        return this==PROFESSOR;
    }
}
