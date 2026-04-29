package interfaces;
import enums.CitationFormat;
import models.ResearchPaper;
import models.ResearchProject;
import java.util.Comparator;
import java.util.List;

public interface IResearcher {
    int calculateHIndex();
    void printPapers(Comparator<ResearchPaper> comparator);
    String getCitationFormat(ResearchPaper paper, CitationFormatFormat format);
    void joinProject(ResearchProject project) throws Exception;
    List<ResearchPaper> getPapers();
    int getHIndex();
}
