package com.mycompany;

import org.jooby.Request;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Servers {

    // sends data to server_name in separate thread
    private static void thread_distribute(String server_name, int port,String question, String opt_a, String opt_b,
                                          String opt_c, String opt_d, String correct_opt, int weight, String access_code){
        Thread thread = new Thread(){
            public void run() {
                try {
                    UpdateDatabase stub = (UpdateDatabase) Naming.lookup("rmi://localhost:"+port+"/stub");
                    stub.add_question(question, opt_a, opt_b, opt_c, opt_d, correct_opt, weight, access_code);
                }catch(NotBoundException | MalformedURLException | RemoteException e){
                    try{
                        Database db = new Database();
                        db.insert_failed(server_name, question, opt_a, opt_b, opt_c, opt_d, correct_opt, weight, access_code);
                    }catch(Exception ee){
                        System.out.println("can't insert failed distribution");
                    }
                }catch(Exception e){
                    System.out.println("Couldn't forward it to server "+server_name);
                }
            }
        };
        thread.start();
    }

    // syncs with server_name on separate thread
    private static void thread_sync(String this_server_name, int this_port, String server_name, int port){
        Thread thread = new Thread(){
            public void run() {
                try {
                    UpdateDatabase stub = (UpdateDatabase) Naming.lookup("rmi://localhost:"+port+"/stub");
                    stub.sync(this_server_name ,this_port);
                }catch(Exception e){
                    System.out.println("Couldn't sync with server "+server_name);
                }
            }
        };
        thread.start();
    }

    // tells access code rule to server_name server on thread
    private static void thread_tell_access_rules(String server_name, int port, String access_code, int n_easy, int n_medium, int n_hard){
        Thread thread = new Thread(){
            public void run() {
                try {
                    UpdateDatabase stub = (UpdateDatabase) Naming.lookup("rmi://localhost:"+ port +"/stub");
                    stub.update_access_rules(access_code, n_easy, n_medium, n_hard);
                }catch(NotBoundException | MalformedURLException | RemoteException e){
                    try{
                        Database db = new Database();
                        db.insert_failed_access(server_name, access_code, n_easy, n_medium, n_hard);
                    }catch(Exception ee){
                        System.out.println("can't insert failed distribution access code rules");
                    }
                }catch(Exception e){
                    System.out.println("Couldn't tell access code rules to server "+server_name);
                }
            }
        };
        thread.start();
    }

    public static void distribute(String question, String opt_a, String opt_b,
                                  String opt_c, String opt_d, String correct_opt, int weight, String access_code){

        try{
            // distribute parallely
            thread_distribute("a",9871,question, opt_a, opt_b, opt_c, opt_d, correct_opt, weight, access_code);
            thread_distribute("b",9872,question, opt_a, opt_b, opt_c, opt_d, correct_opt, weight, access_code);
            thread_distribute("c",9873,question, opt_a, opt_b, opt_c, opt_d, correct_opt, weight, access_code);

        }catch(Exception e){
            System.out.println("Request distribution Failed");
        }

    }// end of method

    public static void sync_me(){
        try{
            // sync paralllely
            thread_sync("d",9874,"a",9871);
            thread_sync("d",9874,"b",9872);
            thread_sync("d",9874,"c",9873);

        }catch(Exception e){
            System.out.println("Request for sync Failed");
        }
    }
    public static void tell_access_rules(String access_code, int n_easy, int n_medium, int n_hard){
        try {
            // tell parallely
            thread_tell_access_rules("a", 9871, access_code, n_easy, n_medium, n_hard);
            thread_tell_access_rules("b", 9872, access_code, n_easy, n_medium, n_hard);
            thread_tell_access_rules("c", 9873, access_code, n_easy, n_medium, n_hard);
        }catch (Exception e){
            System.out.println("Couldn't tell access code rules to all servers");
        }

    }
} 