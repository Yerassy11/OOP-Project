package university.model.research;

import university.model.enums.CitationFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * A published research paper.
 * Supports PLAIN_TEXT and BIBTEX citation formats.
 */
public class ResearchPaper implements Serializable, Comparable<ResearchPaper> {

    private static final long serialVersionUID = 1L;

    private final String     id;
    private String           title;
    private List<String>     authors;
    private String           journal;
    private String           doi;
    private int              citations;
    private int              pages;
    private LocalDate        datePublished;
    private String           abstractText;

    public ResearchPaper(String title, List<String> authors, String journal,
                         String doi, int pages, LocalDate datePublished, String abstractText) {
        this.id            = UUID.randomUUID().toString();
        this.title         = title;
        this.authors       = new ArrayList<>(authors);
        this.journal       = journal;
        this.doi           = doi;
        this.citations     = 0;
        this.pages         = pages;
        this.datePublished = datePublished;
        this.abstractText  = abstractText;
    }

    /**
     * Returns citation in the requested format.
     */
    public String getCitation(CitationFormat format) {
        switch (format) {
            case PLAIN_TEXT:
                return String.join(", ", authors) + " (" + datePublished.getYear() + "). " +
                       title + ". " + journal + ". " + pages + " pages. DOI: " + doi;
            case BIBTEX:
                String key = authors.get(0).split(" ")[0].toLowerCase() + datePublished.getYear();
                return "@article{" + key + ",\n" +
                       "  author  = {" + String.join(" and ", authors) + "},\n" +
                       "  title   = {" + title + "},\n" +
                       "  journal = {" + journal + "},\n" +
                       "  year    = {" + datePublished.getYear() + "},\n" +
                       "  pages   = {" + pages + "},\n" +
                       "  doi     = {" + doi + "}\n" +
                       "}";
            default:
                return toString();
        }
    }

    public void addCitation() { this.citations++; }

    // ── Comparators (Strategy pattern via static fields) ──────────────────────

    public static final Comparator<ResearchPaper> BY_DATE =
        Comparator.comparing(ResearchPaper::getDatePublished).reversed();

    public static final Comparator<ResearchPaper> BY_CITATIONS =
        Comparator.comparingInt(ResearchPaper::getCitations).reversed();

    public static final Comparator<ResearchPaper> BY_LENGTH =
        Comparator.comparingInt(ResearchPaper::getPages).reversed();

    @Override
    public int compareTo(ResearchPaper other) {
        return BY_CITATIONS.compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper)) return false;
        return Objects.equals(doi, ((ResearchPaper) o).doi);
    }

    @Override
    public int hashCode() { return Objects.hash(doi); }

    @Override
    public String toString() {
        return String.format("ResearchPaper{title='%s', authors=%s, journal='%s', citations=%d, pages=%d, date=%s}",
            title, authors, journal, citations, pages, datePublished);
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public String getId()              { return id; }
    public String getTitle()           { return title; }
    public List<String> getAuthors()   { return Collections.unmodifiableList(authors); }
    public String getJournal()         { return journal; }
    public String getDoi()             { return doi; }
    public int getCitations()          { return citations; }
    public int getPages()              { return pages; }
    public LocalDate getDatePublished(){ return datePublished; }
    public String getAbstractText()    { return abstractText; }

    public void setTitle(String t)     { this.title = t; }
    public void setCitations(int c)    { this.citations = c; }
}
