package client;

import exceptions.*;
import interfaces.*;
import server.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//Client program, which connects to the bank using RMI and class methods of the remote bank object
public class StudentClient {
    static int serverAddress, serverPort, account;
    static String operation, password;
    static int studentID; //id of logged in student client
    static int sessionID, id=0;
    static ExamServerInterface examEng; //Exam Server
    static String courseCode; //new students course code, for querying assessment
    static Date startDate, endDate;
    static ArrayList<Assessment> assessments; //Student assessments
    static Assessment ass;

    public static void main (String args[]) throws UnauthorizedAccess, NoMatchingAssessment {
        try {
            //Parse the command line arguments into the program
            getCommandLineArguments(args);
            //Set up the rmi registry and get the remote bank object from it
            String name = "ExamServer";
            Registry registry = LocateRegistry.getRegistry(serverPort);
            examEng = (ExamServerInterface) registry.lookup(name);
            System.out.println("\n----------------\nClient Connected" + "\n----------------\n");
        } catch (InvalidArgumentException ie){
            ie.printStackTrace();
            System.out.println(ie);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
  
        //Switch based on the operation
        switch (operation){
            case "login":
                try {
                    //Login with studentID and password
                    sessionID = examEng.login(studentID, password);
                  
                    
                    /*
                    //Account acc = examEng.accountDetails(id);
                    //Print account details
                    System.out.println("--------------------------\nAccount Details:\n--------------------------\n" +
                                       "Account Number: " + acc.getAccountNumber() +
                                       "\nSessionID: " + id +
                                       "\nstudentID: " + acc.getstudentID() +
                                       "\nBalance: " + acc.getBalance() +
                                       "\n--------------------------\n");
                    System.out.println("Session active for 5 minutes");
                    
                    System.out.println("Use Session Token " + id + " for all other operations");
                    */
                //Catch exceptions that can be thrown from the server
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InvalidLoginException e) {
                    e.printStackTrace();
                }
                break;

            case "getAssessment":
                try {
                    //Retrieves an assessment for logged in user for particular course code e.g "CT475"
                	 	ass = examEng.getAssessment(sessionID, studentID, courseCode);
                	 	
                	 	
                //Catch exceptions that can be thrown from the server
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NoMatchingAssessment e) {
                    e.printStackTrace();
                }
                break;
                
            // submit and assignment
                
            case "submitAssessment":
            		try {
            			examEng.submitAssessment(sessionID, studentID, ass);
            		} catch (RemoteException e) {
                        e.printStackTrace();
                } catch (NoMatchingAssessment e) {
                        e.printStackTrace();
                }
			break;
                
            case "getAvailableSummary":
			break;
                
            default:
                //Catch all case for operation that isn't one of the above
                System.out.println("Operation not supported");
                break;
        }
    }

    public static void getCommandLineArguments(String args[]) throws InvalidArgumentException{
        //Makes sure server, port and operation are entered as arguments
        if(args.length < 4) {
            throw new InvalidArgumentException(args.length);
        }

        //Parses arguments from command line
        //arguments are in different places based on operation, so switch needed here
        serverPort = Integer.parseInt(args[1]);
        operation = args[2];
        switch (operation){
            case "login":
            		System.out.println("Inside Login Command Line Switch");
                studentID = Integer.parseInt(args[3]);
                password = args[4];
                break;
            case "getAvailableSummary":
                account = Integer.parseInt(args[3]);
                sessionID = Integer.parseInt(args[4]);
                break;
            case "getAssessment":
                account = Integer.parseInt(args[3]);
                sessionID = Integer.parseInt(args[4]);
                courseCode = args[5];
                break;
            case "submitAssignment":
                account = Integer.parseInt(args[3]);
                sessionID = Integer.parseInt(args[4]);
                //ass.getAssociatedID() = Integer.parseInt(args[5]);
                //startDate = new Date(args[4]);
                //endDate = new Date(args[5]);
            break;
        }
    }
}
