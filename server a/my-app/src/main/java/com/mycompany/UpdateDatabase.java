package com.mycompany;
// Creating a UpdateDatabase interface


import java.rmi.*;

public interface UpdateDatabase extends Remote{
	// Declaring the method prototype 
	public void add_question(String a, String b, String c, String d, String e, String f, int g, String h) throws RemoteException;
	public void sync(String server_name, int rmi_port)throws RemoteException;
	public void update_access_rules(String access_code, int n_easy, int n_medium, int n_hard)throws RemoteException;
} 

