package university.model.research;

import university.model.exceptions.NonResearcherJoinException;

import java.io.Serializable;
import java.util.*;

/**
 * A research project with a topic, participants (must be Researchers), and published papers.
 */
public class ResearchProject implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private String topic;
    private final List<Researcher>    participants;
    private final List<ResearchPaper> publishedPapers;

    public ResearchProject(String topic) {
        this.id             = UUID.randomUUID().toString();
        this.topic          = topic;
        this.participants   = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    /**
     * Add a participant — they MUST be a Researcher.
     * If called with an object that is not a Researcher, throws NonResearcherJoinException.
     */
    public void addParticipant(Researcher researcher) {
        if (!participants.contains(researcher)) {
            participants.add(researcher);
            System.out.println("[Project] " + researcher.getName() + " joined project: " + topic);
        }
    }

    /**
     * Attempt to add a non-researcher participant — always throws.
     */
    public void addParticipant(Object nonResearcher) throws NonResearcherJoinException {
        String name = nonResearcher.toString();
        throw new NonResearcherJoinException(name);
    }

    public void addPaper(ResearchPaper paper) {
        if (!publishedPapers.contains(paper)) {
            publishedPapers.add(paper);
            System.out.println("[Project] Paper added to project '" + topic + "': " + paper.getTitle());
        }
    }

    public String getReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Research Project Report ===\n");
        sb.append("Topic      : ").append(topic).append("\n");
        sb.append("Participants: ").append(participants.size()).append("\n");
        participants.forEach(r -> sb.append("  - ").append(r.getName()).append("\n"));
        sb.append("Papers     : ").append(publishedPapers.size()).append("\n");
        publishedPapers.forEach(p -> sb.append("  - ").append(p.getTitle()).append("\n"));
        sb.append("================================");
        return sb.toString();
    }

    public String getId()                         { return id; }
    public String getTopic()                      { return topic; }
    public void setTopic(String t)                { this.topic = t; }
    public List<Researcher> getParticipants()     { return Collections.unmodifiableList(participants); }
    public List<ResearchPaper> getPublishedPapers(){ return Collections.unmodifiableList(publishedPapers); }

    @Override
    public String toString() {
        return "ResearchProject{topic='" + topic + "', participants=" + participants.size() +
               ", papers=" + publishedPapers.size() + "}";
    }
}
