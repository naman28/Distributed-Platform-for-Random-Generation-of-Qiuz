package com.mycompany;

import org.apache.commons.lang3.RandomStringUtils;
import org.jooby.*;
import org.jooby.jedis.Redis;
import org.jooby.jedis.RedisSessionStore;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.*;

/**
 * @author jooby generator
 */

public class App extends Jooby {

  {

    port( 8091 );
    Servers.sync_me();

      post("/validate_rules",(req,res)->{

          String access_code = req.cookie("access_code").value();
          int input = Integer.parseInt(req.param("input").value());
          int weight = Integer.parseInt(req.param("weight").value());

          if(input < 0 )
              res.status(200)
                      .type("text/plain")
                      .send(" How can this be negative!!");
          ResultSet rs = new Database().fetch_questions(access_code,weight);
          rs.last();
          int count = rs.getRow();
          String span = "";
          if(input > count)
                 span = " Max "+count;
          res.status(200)
                  .type("text/plain")
                  .send(span);
      });


      post("/set_access_rules",(req,res)->{
          // grab access code

          String access_code = req.cookie("access_code").value();
          int n_easy = Integer.parseInt(req.param("easy").value());
          int n_medium = Integer.parseInt(req.param("medium").value());
          int n_hard = Integer.parseInt(req.param("hard").value());

          // tell access rules to servers
          Thread thread_req_forward = new Thread(){
              public void run(){
                  try{
                      Servers.tell_access_rules(access_code, n_easy, n_medium, n_hard);
                  }catch(Exception e){
                      System.out.println("request forwarding failed");
                  }
              }
          };
          thread_req_forward.start();

          // update in local db
          new Database().set_update_access_rules(access_code, n_easy, n_medium, n_hard);

          res.status(200)
                  .type("text/plain")
                  .send("Access Rules Applied.");
      });

      get("/get_access_code",(req,res) -> {
          String access_code = req.cookie("access_code").value();
          res.status(200)
                  .type("text/plain")
                  .send(access_code);
      } );

      get("/input_questions.html", (req,res,chain) -> {
          String access_code = RandomStringUtils.randomAlphanumeric(7);
          Cookie.Definition ck = new Cookie.Definition("access_code", access_code);
          Cookie cookie = ck.toCookie();
          res.cookie(cookie);
          chain.next(req, res);
      });


      post("/add_question", (req,res) -> {
          // grab access code
          String access_code = req.cookie("access_code").value();

          // grab question details
          String question = req.param("question").value();
          String opt_a = req.param("opt_a").value();
          String opt_b = req.param("opt_b").value();
          String opt_c = req.param("opt_c").value();
          String opt_d = req.param("opt_d").value();
          String correct_opt = req.param("correct_opt").value();
          int weight = Integer.parseInt(req.param("weight").value());

          // request forwarding to servers
          Thread thread_req_forward = new Thread(){
              public void run(){
                  try{
                      Servers.distribute(question, opt_a, opt_b, opt_c, opt_d, correct_opt, weight, access_code);
                  }catch(Exception e){
                      System.out.println("request forwarding failed");
                  }
              }
          };
          thread_req_forward.start();

          // add this question to database
          Database db = new Database();
          db.insert(question, opt_a, opt_b, opt_c, opt_d, correct_opt, weight, access_code);

          // acknowledge
          res.status(200)
                  .type("text/plain")
                  .send("Question Added Successfully!!");
      });

      get("/get_questions", (req,res,chain) -> {
          String access_code = req.param("access_code").value().trim();
          Cookie.Definition ck = new Cookie.Definition("access_code", access_code);
          Cookie cookie = ck.toCookie();
          res.cookie(cookie);

          QuizGenerator generate_quiz = new QuizGenerator();
          res.send(generate_quiz.fromAccessCode(access_code));
      });

      post("/evaluate_quiz", (req,res) -> {
          // grab access code
          String access_code = req.cookie("access_code").value();
          // grab user answers
          Map<String,Mutant> map = req.params().toMap();
          // correct answers
          ResultSet resultSet = (new Database()).display(access_code);
          // evaluate
          String html_response = new QuizEvaluator().evaluate(access_code,resultSet,map);

          res.send(html_response);
      });

    assets("/", "index.html");
    assets("/**");
  }

  // main
  public static void main(final String[] args) {

      try {

          // Registry
          Thread thread_1 = new Thread(){
              public void run(){
                  try{
                    LocateRegistry.createRegistry(9871);
                    System.out.println("Located Registry successful");
                  }catch(Exception e){
                      System.out.println("LocateRegistry Error");
                  }
              }
          };
          thread_1.start();

          // Name Binding
          Thread thread_2 = new Thread(){
              public void run(){
                  try{
                      // binding rmi object
                      UpdateDatabase obj = new Updatedb();
                      Naming.rebind("rmi://localhost:9871/stub", obj);
                        System.out.println("Bind successful");
                  }catch(Exception e){
                      System.out.println("Bind Failed..!!");
                  }
              }
          };
          thread_2.start();


          // starting the app
          run(App::new, args);

      }catch(Exception e){
          System.out.println(e);
      }
  }
}


// grab access code
          /*
          List<Cookie> ck =  req.cookies();
          String access_code = ck.get(0).value().get();
          */