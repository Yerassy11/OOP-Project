package university.model.research;

import university.model.users.User;
import university.pattern.IObservable;

import java.io.Serializable;
import java.util.*;

/**
 * University research journal.
 * Implements Observer pattern — notifies subscribers when a paper is published.
 * Pattern: Observer.
 */
public class Journal implements IObservable, Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private String name;
    private String issn;
    private final List<User>          subscribers;
    private final List<ResearchPaper> papers;

    public Journal(String name, String issn) {
        this.id          = UUID.randomUUID().toString();
        this.name        = name;
        this.issn        = issn;
        this.subscribers = new ArrayList<>();
        this.papers      = new ArrayList<>();
    }

    // ── IObservable ───────────────────────────────────────────────────────────

    @Override
    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
            System.out.println("[Journal] " + user.getFullName() + " subscribed to " + name);
        }
    }

    @Override
    public void unsubscribe(User user) {
        subscribers.remove(user);
        System.out.println("[Journal] " + user.getFullName() + " unsubscribed from " + name);
    }

    @Override
    public void notifySubscribers(String message) {
        subscribers.forEach(u -> u.update("[" + name + "] " + message));
    }

    // ── Publishing ────────────────────────────────────────────────────────────

    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
        notifySubscribers("New paper published: " + paper.getTitle() +
                          " by " + paper.getAuthors());
        System.out.println("[Journal] Paper published in " + name + ": " + paper.getTitle());
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getId()                      { return id; }
    public String getName()                    { return name; }
    public String getIssn()                    { return issn; }
    public List<User> getSubscribers()         { return Collections.unmodifiableList(subscribers); }
    public List<ResearchPaper> getPapers()     { return Collections.unmodifiableList(papers); }

    @Override
    public String toString() {
        return "Journal{name='" + name + "', issn='" + issn + "', papers=" + papers.size() +
               ", subscribers=" + subscribers.size() + "}";
    }
}
