package com.mycompany;


import javax.xml.crypto.Data;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;


public class Updatedb extends UnicastRemoteObject
		implements UpdateDatabase {
	Updatedb() throws RemoteException{
		super(9874);
	}

	// add questions to its local database
	public void add_question(String question, String opt_a, String opt_b,
							 String opt_c, String opt_d, String correct_opt, int weight, String access_code)throws RemoteException{

		try {
			// add this question to database
			Database db = new Database();
			db.insert(question, opt_a, opt_b, opt_c, opt_d, correct_opt, weight, access_code);
		}catch (Exception e){
			System.out.println(e);
		}

	}

	// adds access code rules to its local db
	public void update_access_rules(String access_code, int n_easy, int n_medium, int n_hard)throws RemoteException{
		try{
			new Database().set_update_access_rules(access_code, n_easy , n_medium , n_hard);
		}catch (Exception e){
			System.out.println(e);
		}
	}

	// serves sync requests
	public void sync(String server_name, int rmi_port)throws RemoteException{

		// data distribution
		Thread thread_data_distribute = new Thread(){
			public void run() {
				try {

					// add unsent questions to remote database
					Database db = new Database();
					ResultSet resultSet = db.fetch_failed(server_name);

					// trying adding these questions to remote db again
					UpdateDatabase stub = (UpdateDatabase) Naming.lookup("rmi://localhost:"+rmi_port+"/stub");
					while (resultSet.next()) {
						System.out.println("sent one row");
						stub.add_question(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
								resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7), resultSet.getString(8));
					}

					System.out.println("Suuceessfully reached sync and sent");
					// delete table entries
					Database db_obj = new Database();
					db_obj.empty_failed(server_name);

				}catch (Exception e){
					System.out.println("Data Sync Error");
				}
			}
		};
		thread_data_distribute.start();

		// access code distribution
		Thread thread_access_rules_distribution = new Thread(){
			public void run() {
				try {

					// add unsent questions to remote database
					Database db = new Database();
					ResultSet resultSet = db.fetch_failed_access_send(server_name);

					// trying adding these questions to remote db again
					UpdateDatabase stub = (UpdateDatabase) Naming.lookup("rmi://localhost:"+rmi_port+"/stub");
					while (resultSet.next()) {
						System.out.println("sent one row");
						stub.update_access_rules(resultSet.getString(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4));
					}

					System.out.println("Suuceessfully reached sync and sent");
					// delete table entries
					Database db_obj = new Database();
					db_obj.empty_failed_access_send(server_name);

				}catch (Exception e){
					System.out.println("Access Rules Sync Error");
				}
			}
		};
		thread_access_rules_distribution.start();
	}

} 

