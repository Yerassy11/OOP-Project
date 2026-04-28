package interfaces;
import models.User;
public interface IObservable {
    void subscribe(User user);
    void unsubscribe(User user);
    void notifySubscribers(String message);
}
