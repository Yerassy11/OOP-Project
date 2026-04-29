package ui;
import data.Database;
import models.TechRequest;
import models.TeachSupportSpecialist;
import java.util.List;
import java.util.Scanner;
public class TeachSupportMenu {
    private final TechSupportSpecialist specialist;
    private final Scanner scanner;
    private final Database db;
    public TechSupportMenu(TechSupportSpecialist specialist, Scanner scanner){
        this.specialist=specialist;
        this.scanner=scanner;
        this.db=Database.getInstanse();
    }
    public void show(){
        boolean running=true;
        while(running){
            System.out.println("\\n========== Tech Support Menu ==========");
            System.out.println("1. View all tech requests");
            System.out.println("2. Accept a request");
            System.out.println("3. Reject a request");
            System.out.println("4. Mark request as done");
            System.out.println("5. View inbox");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            String input=scanner.nextLine().trim();
            switch(input){
                case "1": viewAllRequests(); break;
                case "2": handleRequest("ACCEPT"); break;
                case "3": handleRequest("REJECT"); break;
                case "4": handleRequest("DONE"); break;
                case "5": specialist.viewInbox(); break;
                case "0": running = false; specialist.logout(); break;
                default: System.out.println("Invalid option.");
            }
        }
    }
    public void viewAllRequests(){
        List<TechRequest> requests=db.getTechRequests();
        if(requests.isEmpty()){
            System.out.println("No requests.");
            return;}
        System.out.println("=== All Tech Requests ===");
        for(int i=0;i<requests.size();i++){
            System.out.println("["+(i+1)+"]"+ requests.get(i));
        }
    }
    private void handleRequest(String action){
        List<TechRequest> requests=db.getTechRequests();
        if(requests.isEmpty()){
            System.out.println("No requests."); return;}
        viewAllRequests();
        System.out.println("Choose request number;");
        try{
            int idx=Integer.parseInt(scanner.nextLine().trim())-1;
            if(idx<0 || idx>=requests.size()){
                System.out.println("Invalid.");
                return;
            }
            TechRequest req=requests.get(idx);
            specialist.assignRequests(req);
            switch(action){
                case "ACCEPT": specialist.acceptRequest(req); break;
                case "REJECT": specialist.rejectRequest(req); break;
                case "DONE":   specialist.markDone(req); break;
            }
        }
        catch(NumberFormatException e){
            System.out.println("Invalid input.");
        }
    }
}
