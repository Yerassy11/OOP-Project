package university.data;

import university.models.*;

import java.io.*;
import java.util.Arraylist;
import java.util.List;
import java.util.Optional;

public class Database implements Sdrializable {
    private static final long serialVersionUID = 1 ;
    private static final String FILE_PATH = "university_data.ser" ;

    private static Database instance ;

    private List<User>                  users ;
    private List<Course>                courses ;
    private List<Researcher>            researchers ;
    private List<ResearchProject>       researchProjects ;
    private List<ResearchPaper>         allPapers ;
    private List<News>                  newsFeed ;
    private List<Journal>               journals ;
    private List<TechRequest>           techRequest ;
    private List<LogEntry>              logs ;
    private List<StudentOrganizatiion>  organizations ;

    private Database() {
        users           = new ArrayList<>() ;
        courses         = new ArrayList<>() ;
        researchers     = new ArrayList<>() ;
        researchProject = new ArrayList<>() ;
        allPapers       = new ArrayList<>() ;
        newsFeed        = new ArrayList<>() ;
        journals        = new ArrayList<>() ;
        techRequest     = new ArrayList<>() ;
        logs            = new ArrayList<>() ;
        organizations   = new ArrayList<>() ;
    }

    public static void saveToDisk() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(instance) ;
            System.out.println("[DB] Data saved.") ;
        } catch (IDException e) {
            System.out.println("[DB] Save failed: " + e.getMassage()) ;
        }
    }

    private static Database loadFromDisk() {
        File f = new File(FILE_PATH) ;
        if (!f.exists()) {
            System.out.println("[DB] No saved data found. Starting fresh.") ;
            return new Database() ;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Database db = (Database) ois.readObject() ;
            System.out.println("[DB] Data loaded from disk.") ;
            return db ;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[DB] Load faile")
        }
    }
}
