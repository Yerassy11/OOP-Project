package university.model.news;

import university.model.users.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A comment on a news article.
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String        id;
    private final User          author;
    private final String        content;
    private final LocalDateTime timestamp;

    public Comment(User author, String content) {
        this.id        = UUID.randomUUID().toString();
        this.author    = author;
        this.content   = content;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        return Objects.equals(id, ((Comment) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", timestamp, author.getFullName(), content);
    }

    public String getId()            { return id; }
    public User getAuthor()          { return author; }
    public String getContent()       { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
