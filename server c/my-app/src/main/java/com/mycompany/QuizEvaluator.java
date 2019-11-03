package com.mycompany;

import org.jooby.Mutant;

import java.sql.ResultSet;
import java.util.Map;

public class QuizEvaluator {
    public String evaluate(String access_code, ResultSet resultSet, Map<String, Mutant> map)throws Exception{
        String ques_id, key ,question , opt_a, opt_b, opt_c, opt_d, correct_opt, wrong_opt, para = "";

        int index = 1, marks = 0;
        while(resultSet.next()){
            ques_id = String.valueOf(resultSet.getInt(1));
            key = "question_id_"+ques_id;
            if(map.containsKey(key)){
                if(map.get(key).toString().equals("["+resultSet.getString(7)+"]")){
                    marks = marks + resultSet.getInt(8);
                    ques_id = String.valueOf(resultSet.getInt(1));
                    question = resultSet.getString(2);
                    opt_a = resultSet.getString(3);
                    opt_b = resultSet.getString(4);
                    opt_c = resultSet.getString(5);
                    opt_d = resultSet.getString(6);
                    correct_opt = resultSet.getString(7);
                    para = para + "<p><span class=\"correct_ques\">" + String.valueOf(index) + ".</span> " + question + " ( "+ resultSet.getInt(8) +" marks )</p>" + string_input_tag + "question_id_" + ques_id + string_val_a + "  " + opt_a + "<br>"
                            + string_input_tag + "question_id_" + ques_id + string_val_b + "  " + opt_b + "<br>"
                            + string_input_tag + "question_id_" + ques_id + string_val_c + "  " + opt_c + "<br>"
                            + string_input_tag + "question_id_" + ques_id + string_val_d + "  " + opt_d + "<br>"
                            + "<p> Correct Option : "+correct_opt + "</p><br>";


                }else{
                    ques_id = String.valueOf(resultSet.getInt(1));
                    question = resultSet.getString(2);
                    opt_a = resultSet.getString(3);
                    opt_b = resultSet.getString(4);
                    opt_c = resultSet.getString(5);
                    opt_d = resultSet.getString(6);
                    correct_opt = resultSet.getString(7);
                    wrong_opt = map.get(key).value();
                    para = para + "<p><span class=\"wrong_ques\">" + String.valueOf(index) + ".</span> "+ question + " ( "+ resultSet.getInt(8) +" marks )</p>" + string_input_tag + "question_id_" + ques_id + string_val_a + "  " + opt_a + "<br>"
                            + string_input_tag + "question_id_" + ques_id + string_val_b + "  " + opt_b + "<br>"
                            + string_input_tag + "question_id_" + ques_id + string_val_c + "  " + opt_c + "<br>"
                            + string_input_tag + "question_id_" + ques_id + string_val_d + "  " + opt_d + "<br>"
                            + "<p> Correct Option : "+correct_opt + "<br> Marked Answer (Wrong) : "+ wrong_opt + "</p><br>";
                }
                index++;
            } // end outer if map

        }
        String show_marks = "<p> Total Score = " + String.valueOf(marks) + "</p><hr>";
        return head + show_marks + para + end ;
    } // end of function


    // instance variables
    String head = "\n" +
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <style>\n" +
            "        body{\n" +
            "          background-color: #f1f1f1;\n" +
            "        }\n" +
            "        nav{\n" +
            "          padding: 20px;\n" +
            "        }\n" +
            "        nav a{\n" +
            "          padding: 20px;\n" +
            "        }\n" +
            "        a{\n" +
            "          text-decoration: none;\n" +
            "        }\n" +
            "        form{\n" +
            "          padding: 20px;\n" +
            "        }\n" +
            "        .wrong_ques{ background-color : red } \n" +
            "        .correct_ques{ background-color : green } \n" +
            "        form i{\n" +
            "          position: absolute;\n" +
            "          padding: 10px;\n" +
            "        }\n" +
            "        #form-name{\n" +
            "          padding:20px;\n" +
            "          margin:0px;\n" +
            "          font-size:25px;\n" +
            "          letter-spacing: 3px;\n" +
            "        }\n" +
            "        form #add-btn{\n" +
            "          width : 10%;\n" +
            "          background-color: white;\n" +
            "          color: black;\n" +
            "          border: 2px solid #e7e7e7;\n" +
            "          margin:10px;\n" +
            "          padding: 10px;\n" +
            "        }\n" +
            "        iframe{\n" +
            "          display: none;\n" +
            "        }\n" +
            "      </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<form name=\"quiz\" action='/evaluate_quiz' method=\"POST\">",

    end =   "</form>\n" +
            "\n" +
            "</body>\n" +
            "</html>";


    String para ="";

    String string_input_tag = "<input type=\"radio\" name=\"" ,
            string_val_a= "\" value=\"a\">",
            string_val_b= "\" value=\"b\">",
            string_val_c= "\" value=\"c\">",
            string_val_d= "\" value=\"d\">";

}
