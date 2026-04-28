package university.model.news;

import university.model.enums.NewsTopic;
import university.model.users.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A university news article.
 * RESEARCH topic news is always pinned (sorted first).
 */
public class News implements Serializable, Comparable<News> {

    private static final long serialVersionUID = 1L;

    private final String        id;
    private String              title;
    private String              body;
    private NewsTopic           topic;
    private boolean             isPinned;
    private final LocalDateTime createdAt;
    private final List<Comment> comments;

    public News(String title, String body, NewsTopic topic) {
        this.id        = UUID.randomUUID().toString();
        this.title     = title;
        this.body      = body;
        this.topic     = topic;
        this.isPinned  = (topic == NewsTopic.RESEARCH); // auto-pin research news
        this.createdAt = LocalDateTime.now();
        this.comments  = new ArrayList<>();
    }

    public void addComment(User author, String content) {
        comments.add(new Comment(author, content));
    }

    /**
     * Pinned (Research) news sorts before unpinned; within same pin status, newest first.
     */
    @Override
    public int compareTo(News other) {
        if (this.isPinned && !other.isPinned) return -1;
        if (!this.isPinned && other.isPinned) return 1;
        return other.createdAt.compareTo(this.createdAt); // newest first
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof News)) return false;
        return Objects.equals(id, ((News) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return String.format("[%s%s] %s — %s\n  %s\n  Comments: %d",
            isPinned ? "📌 " : "", topic, title, createdAt.toLocalDate(), body, comments.size());
    }

    public String getId()              { return id; }
    public String getTitle()           { return title; }
    public String getBody()            { return body; }
    public NewsTopic getTopic()        { return topic; }
    public boolean isPinned()          { return isPinned; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
    public List<Comment> getComments() { return Collections.unmodifiableList(comments); }

    public void setTitle(String t)     { this.title = t; }
    public void setBody(String b)      { this.body = b; }
    public void setPinned(boolean p)   { this.isPinned = p; }
}
