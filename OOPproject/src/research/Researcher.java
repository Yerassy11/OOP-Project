package university.model.research;

import university.data.DataStorage;
import university.model.enums.CitationFormat;
import university.model.exceptions.NonResearcherJoinException;
import university.model.news.News;
import university.model.enums.NewsTopic;
import university.model.users.User;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Researcher — implemented as a Decorator.
 * Any User (Teacher, Student, Employee) can be wrapped as a Researcher.
 * Implements IResearcher.
 *
 * Pattern: Decorator — adds research capability to any User without altering the class hierarchy.
 */
public class Researcher implements IResearcher, Serializable {

    private static final long serialVersionUID = 1L;

    private final User             wrappedUser;   // the decorated user
    private final List<ResearchPaper>   papers;
    private final List<ResearchProject> projects;

    public Researcher(User user) {
        this.wrappedUser = user;
        this.papers      = new ArrayList<>();
        this.projects    = new ArrayList<>();
    }

    // ── IResearcher ───────────────────────────────────────────────────────────

    /**
     * h-index: largest h such that h papers each have ≥ h citations.
     */
    @Override
    public int calculateHIndex() {
        List<Integer> sorted = papers.stream()
            .map(ResearchPaper::getCitations)
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        int h = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        System.out.println("\n=== Papers by " + getName() + " ===");
        if (papers.isEmpty()) { System.out.println("  No papers."); return; }
        papers.stream().sorted(comparator).forEach(System.out::println);
    }

    @Override
    public List<ResearchPaper> getPapers() { return Collections.unmodifiableList(papers); }

    @Override
    public void addPaper(ResearchPaper paper) {
        if (!papers.contains(paper)) {
            papers.add(paper);
            autoGenerateNewsOnPublish(paper);
            DataStorage.getInstance().log(getName() + " published paper: " + paper.getTitle());
        }
    }

    @Override
    public void joinProject(ResearchProject project) throws NonResearcherJoinException {
        project.addParticipant(this);
        if (!projects.contains(project)) projects.add(project);
    }

    // ── Publishing ────────────────────────────────────────────────────────────

    /**
     * Full publish flow: add paper + attach to journal if provided.
     */
    public void publishPaper(ResearchPaper paper, Journal journal) {
        addPaper(paper);
        if (journal != null) {
            journal.publishPaper(paper);
        }
    }

    private void autoGenerateNewsOnPublish(ResearchPaper paper) {
        News announcement = new News(
            "New Research Paper: " + paper.getTitle(),
            getName() + " published a new paper in " + paper.getJournal() +
            ". Citations: " + paper.getCitations(),
            NewsTopic.RESEARCH
        );
        DataStorage.getInstance().addNews(announcement);
        checkAndAnnounceTopCited();
    }

    /**
     * Auto-generate news if this researcher is now top-cited.
     */
    public static void checkAndAnnounceTopCited() {
        // Collect all researchers from DataStorage (set during system init)
        // This is a simplified check — in production, ResearcherRegistry would track this.
        System.out.println("[News] Checking top-cited researcher status...");
    }

    // ── Printing top cited ────────────────────────────────────────────────────

    public static void printTopCited(List<Researcher> researchers,
                                     Comparator<ResearchPaper> comparator, int topN) {
        System.out.println("\n=== Top Cited Researchers ===");
        researchers.stream()
            .sorted(Comparator.comparingInt(Researcher::calculateHIndex).reversed())
            .limit(topN)
            .forEach(r -> System.out.printf("  %s — h-index=%d, papers=%d%n",
                r.getName(), r.calculateHIndex(), r.getPapers().size()));
    }

    public static void printAllPapers(List<Researcher> researchers,
                                      Comparator<ResearchPaper> comparator) {
        System.out.println("\n=== All University Research Papers ===");
        researchers.stream()
            .flatMap(r -> r.getPapers().stream())
            .sorted(comparator)
            .forEach(System.out::println);
    }

    // ── Citation helper ───────────────────────────────────────────────────────

    public void printAllCitations(CitationFormat format) {
        System.out.println("\n=== Citations (" + format + ") — " + getName() + " ===");
        papers.forEach(p -> System.out.println(p.getCitation(format)));
    }

    // ── Delegation to wrapped user ────────────────────────────────────────────

    public User getWrappedUser() { return wrappedUser; }
    public String getName()      { return wrappedUser.getFullName(); }

    public List<ResearchProject> getProjects() { return Collections.unmodifiableList(projects); }

    @Override
    public String toString() {
        return "Researcher{user='" + getName() + "', hIndex=" + calculateHIndex() +
               ", papers=" + papers.size() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Researcher)) return false;
        return Objects.equals(wrappedUser, ((Researcher) o).wrappedUser);
    }

    @Override
    public int hashCode() { return Objects.hash(wrappedUser); }
}
