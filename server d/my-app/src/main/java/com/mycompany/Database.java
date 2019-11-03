package com.mycompany;

import java.sql.*;

class Database{

    Connection conn;

    Database()throws Exception{
        Class.forName("org.mariadb.jdbc.Driver");
        this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Question_Bank_d","root","");
    }


    public void insert(String question, String opt_a, String opt_b,
                       String opt_c, String opt_d, String correct_opt, int weight, String access_code){
        try{
            String query = " insert into question_details (question, opt_a, opt_b, opt_c, opt_d, correct_opt, weight , access_code)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, question);
            preparedStmt.setString (2, opt_a);
            preparedStmt.setString (3, opt_b);
            preparedStmt.setString (4, opt_c);
            preparedStmt.setString (5, opt_d);
            preparedStmt.setString (6, correct_opt);
            preparedStmt.setInt(7, weight);
            preparedStmt.setString (8, access_code);

            // execute the preparedstatement
            preparedStmt.execute();
            conn.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }


    public ResultSet display(String access_code) {
        ResultSet rs = null;
        try {
            String query = "select * from question_details where access_code = ?";
            // prepared select query
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, access_code);
            rs = preparedStmt.executeQuery();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public ResultSet fetch_questions(String access_code, int weight) {
        ResultSet rs = null;
        try {
            String query = "select * from question_details where access_code = ? and weight = ? ORDER BY RAND()";
            // prepared select query
            PreparedStatement preparedStmt = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            preparedStmt.setString(1, access_code);
            preparedStmt.setInt(2,weight);
            rs = preparedStmt.executeQuery();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public void insert_failed(String server, String question, String opt_a, String opt_b,
                              String opt_c, String opt_d, String correct_opt, int weight, String access_code){
        try{
            String table_name = "failed_distribution_to_" + server;
            String query = " insert into "+ table_name +" (question, opt_a, opt_b, opt_c, opt_d, correct_opt, weight , access_code)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, question);
            preparedStmt.setString (2, opt_a);
            preparedStmt.setString (3, opt_b);
            preparedStmt.setString (4, opt_c);
            preparedStmt.setString (5, opt_d);
            preparedStmt.setString (6, correct_opt);
            preparedStmt.setInt(7, weight);
            preparedStmt.setString (8, access_code);

            // execute the preparedstatement
            preparedStmt.execute();

            conn.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public ResultSet fetch_failed(String server_name) {
        ResultSet rs = null;
        try {
            String table_name = "failed_distribution_to_" + server_name;
            String query = "select * from " + table_name;
            // prepared select query
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            rs = preparedStmt.executeQuery();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }
    public void empty_failed(String server_name) {
        ResultSet rs = null;
        try {
            String table_name = "failed_distribution_to_" + server_name;
            String query = "Delete from "+ table_name;
            // prepared select query
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            rs = preparedStmt.executeQuery();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void set_update_access_rules(String access_code, int n_easy, int n_medium, int n_hard){
        try{
            String query = " insert into access_code_rules (access_code, easy, medium, hard)"
                    + " values (?, ?, ?, ?) on duplicate key update easy = ? , medium = ? , hard = ?";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, access_code);
            preparedStmt.setInt (2, n_easy);
            preparedStmt.setInt (3, n_medium);
            preparedStmt.setInt (4, n_hard);
            preparedStmt.setInt (5, n_easy);
            preparedStmt.setInt (6, n_medium);
            preparedStmt.setInt (7, n_hard);

            // execute the preparedstatement
            preparedStmt.execute();
            conn.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public ResultSet fetch_access_rule(String access_code) {
        ResultSet rs = null;
        try {
            String query = "select * from access_code_rules where access_code = ? ";
            // prepared select query
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, access_code);
            rs = preparedStmt.executeQuery();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public void insert_failed_access(String server_name, String access_code, int n_easy, int n_medium, int n_hard){
        try{
            String table_name = "access_code_rules_to_"+server_name;
            String query = " insert into " + table_name + " (access_code, easy, medium, hard)"
                    + " values (?, ?, ?, ?) on duplicate key update easy = ? , medium = ? , hard = ?";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, access_code);
            preparedStmt.setInt (2, n_easy);
            preparedStmt.setInt (3, n_medium);
            preparedStmt.setInt (4, n_hard);
            preparedStmt.setInt (5, n_easy);
            preparedStmt.setInt (6, n_medium);
            preparedStmt.setInt (7, n_hard);

            // execute the preparedstatement
            preparedStmt.execute();
            conn.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public ResultSet fetch_failed_access_send(String server_name) {
        ResultSet rs = null;
        try {
            String table_name = "access_code_rules_to_" + server_name;
            String query = "select * from " + table_name;
            // prepared select query
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            rs = preparedStmt.executeQuery();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public void empty_failed_access_send(String server_name) {
        ResultSet rs = null;
        try {
            String table_name = "access_code_rules_to_" + server_name;
            String query = "Delete from "+ table_name;
            // prepared select query
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            rs = preparedStmt.executeQuery();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}


/*
create table question_details (
  id int unsigned auto_increment not null,
  question varchar(100) not null,
  opt_a varchar(100) not null,
  opt_b varchar(100) not null,
  opt_c varchar(100) not null,
  opt_d varchar(100) not null,
  correct_opt varchar(1) not null,
  weight int not null,
  access_code varchar(32) not null,
  primary key (id)
);

*/