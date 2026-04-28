package interfaces;

public interface IAuthernticable {
    boolean login(String password);
    void logout();
}
