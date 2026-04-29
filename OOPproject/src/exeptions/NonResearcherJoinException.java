package exeptions;

public class NonResearcherJoinException extends Exception {
    private final String violatingUser;

    public NonResearcherJoinException(String username) {

        super("User '" + username + "' is not a researcher and cannot join a research project");
        this.violatingUser=username;
    }
    public String getViolatingUser(){
        return violatingUser;
    }
}
