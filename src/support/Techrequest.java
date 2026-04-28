package university.model.support;

import university.model.enums.RequestStatus;
import university.model.users.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A technical support request (e.g. fix a projector, printer).
 * Status flow: NEW → VIEWED → ACCEPTED|REJECTED → DONE
 */
public class TechRequest implements Serializable, Comparable<TechRequest> {

    private static final long serialVersionUID = 1L;

    private final String        id;
    private final User          submittedBy;
    private String              description;
    private RequestStatus       status;
    private final LocalDateTime createdAt;

    public TechRequest(User submittedBy, String description) {
        this.id          = UUID.randomUUID().toString();
        this.submittedBy = submittedBy;
        this.description = description;
        this.status      = RequestStatus.NEW;
        this.createdAt   = LocalDateTime.now();
    }

    public void updateStatus(RequestStatus newStatus) {
        this.status = newStatus;
        university.data.DataStorage.getInstance().log(
                "TechRequest " + id.substring(0, 8) + " status → " + newStatus);
    }

    @Override
    public int compareTo(TechRequest other) {
        return this.createdAt.compareTo(other.createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TechRequest)) return false;
        return Objects.equals(id, ((TechRequest) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return String.format("TechRequest{id='%s', by='%s', status=%s, desc='%s', at=%s}",
                id.substring(0, 8), submittedBy.getFullName(), status, description, createdAt.toLocalDate());
    }

    public String getId()              { return id; }
    public User getSubmittedBy()       { return submittedBy; }
    public String getDescription()     { return description; }
    public RequestStatus getStatus()   { return status; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}