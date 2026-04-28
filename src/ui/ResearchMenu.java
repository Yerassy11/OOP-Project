package ui;
import data.Database;
import enums.CitationFormat;
import models.*;
import patterns.PaperComparators;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
public class ResearchMenu {
    private final Researcher researcher;
    private final Scanner scanner;
    private final Database db;
    public ResearcherMenu(Researcher researcher, Scanner scanner){
        this.researcher=researcher;
        this.scanner=scanner;
        this.db=Database.getInstance();
    }
    public void show(){
        boolean running=true;
        while(running){
            System.out.println("\n========== Researcher Menu ==========");
            System.out.println("Researcher: " + researcher.getName() +
                    "  |  h-index: " + researcher.calculateHIndex());
            System.out.println("1. View my papers");
            System.out.println("2. Add a new paper");
            System.out.println("3. Print papers sorted");
            System.out.println("4. View my research projects");
            System.out.println("5. Join a research project");
            System.out.println("6. Print citation for a paper");
            System.out.println("7. View all researchers + h-index ranking");
            System.out.println("8. Print top-cited papers (all university)");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String input=scanner.nextLine().trim();
            switch(input){
                case "1": listMyPapers(); break;
                case "2": addPaper(); break;
                case "3": printSorted(); break;
                case "4": listProjects(); break;
                case "5": joinProject(); break;
                case "6": printCitation(); break;
                case "7": rankResearchers(); break;
                case "8": printTopCitedAll(); break;
                case "0": running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }
    private void listMyPapers(){
        List<ResearchPaper> papers=researcher.getPapers();
        if(papers.isEmpty()){
            System.out.println("No papers yet.");
            return;}
        System.out.println("=== My Papers ===");
        for(int i=0;i<papers.size();i++){
            System.out.println("["+(i+1)+"]"+papers.get(i));
        }
    }
    private void addPaper(){
        try {
            System.out.println("Title: ");
            String title = scanner.nextLine().trim();
            System.out.println("Authors (comma-separated): ");
            String[] authors = scanner.nextLine().split(",");
            for (int i = 0; i < authors.length; i++) authors[i] = authors[i].trim();
            System.out.print("Journal: ");
            String journal = scanner.nextLine().trim();
            System.out.print("DOI: ");
            String doi = scanner.nextLine().trim();
            System.out.print("Citations: ");
            int citations = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Pages: ");
            int pages = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Date published (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine().trim());
            System.out.print("Keywords: ");
            String keywords = scanner.nextLine().trim();
            ResearchPaper paper = new ResearchPaper(title, Arrays.asList(authors),
                    journal, doi, citations, pages, date, keywords);
            researcher.publishPaper(paper);
            db.addPaper(paper);
            News news = new News("New paper by " = researcher.getName(), "\"" + title + "\" published in " + journal, enums.NewsTopic.RESEARCH);
            db.addNews(news);
            System.out.println("Paper added and news posted");
        }
        catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void printSorted() {
        System.out.println("Sort by: 1=Citations  2=Date  3=Length(pages)  4=Title");
        System.out.print("Choice: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            Comparator<ResearchPaper> cmp = PaperComparators.fromChoice(choice);
            researcher.printPapers(cmp);
        } catch (NumberFormatException e) { System.out.println("Invalid."); }
    }
    private void listProjects() {
        List<ResearchProject> projects = researcher.getProjects();
        if (projects.isEmpty()) { System.out.println("Not part of any project."); return; }
        for (ResearchProject p : projects) System.out.println(p);
    }
    private void joinProject() {
        List<ResearchProject> projects = db.getResearchProjects();
        if (projects.isEmpty()) { System.out.println("No projects available."); return; }
        for (int i = 0; i < projects.size(); i++) {
            System.out.println("[" + (i+1) + "] " + projects.get(i).getTopic());
        }
        System.out.print("Choose project: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= projects.size()) { System.out.println("Invalid."); return; }
            researcher.joinProject(projects.get(idx));
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }
    private void printCitation() {
        listMyPapers();
        List<ResearchPaper> papers = researcher.getPapers();
        if (papers.isEmpty()) return;
        System.out.print("Choose paper: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= papers.size()) { System.out.println("Invalid."); return; }
            System.out.println("Format: 1=Plain text  2=BibTeX");
            System.out.print("Choice: ");
            int fmt = Integer.parseInt(scanner.nextLine().trim());
            CitationFormat format = fmt == 2 ? CitationFormat.BIBTEX : CitationFormat.PLAIN_TEXT;
            System.out.println(papers.get(idx).getCitation(format));
        } catch (NumberFormatException e) { System.out.println("Invalid."); }
    }
    private void rankResearchers() {
        List<Researcher> all = db.getResearchers();
        all.sort(null); // uses Researcher.compareTo — by h-index desc
        System.out.println("=== Researcher Rankings (by h-index) ===");
        for (int i = 0; i < all.size(); i++) {
            Researcher r = all.get(i);
            System.out.printf("%d. %-30s h-index=%d  papers=%d%n",
                    i+1, r.getName(), r.calculateHIndex(), r.getPapers().size());
        }
    }
    private void printTopCitedAll() {
        List<ResearchPaper> all = db.getAllPapers();
        all.sort(PaperComparators.BY_CITATIONS);
        System.out.println("=== Top Cited Papers (University) ===");
        int limit = Math.min(10, all.size());
        for (int i = 0; i < limit; i++) {
            System.out.printf("%d. %s%n", i+1, all.get(i));
        }
    }
}
