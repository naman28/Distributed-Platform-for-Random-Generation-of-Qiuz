package com.mycompany;

import java.sql.ResultSet;


public class QuizGenerator {

    // generate quiz
    public  String fromAccessCode(String access_code)throws Exception {


        // grab all difficulty wise question sets
        ResultSet resultSet_easy = new Database().fetch_questions(access_code,1);
        ResultSet resultSet_medium = new Database().fetch_questions(access_code,2);
        ResultSet resultSet_hard = new Database().fetch_questions(access_code,3);

        // get access code rules
        int n_easy=100 , n_medium= 100, n_hard = 100;
        try{
            ResultSet resultSet_access_rule = new Database().fetch_access_rule(access_code);
            resultSet_access_rule.next();
            n_easy = resultSet_access_rule.getInt(2);
            n_medium = resultSet_access_rule.getInt(3);
            n_hard = resultSet_access_rule.getInt(4);
        }catch (Exception e){
            // do nothing cus no rules found,
            // randomly render atmost 100 question of each level
        }

        String question, opt_a, opt_b, opt_c, opt_d, ques_id;
        int index = 1, easy_count = 1;
        while (resultSet_easy.next()) {
            if(easy_count > n_easy)
                break;
            ques_id = String.valueOf(resultSet_easy.getInt(1));
            question = resultSet_easy.getString(2);
            opt_a = resultSet_easy.getString(3);
            opt_b = resultSet_easy.getString(4);
            opt_c = resultSet_easy.getString(5);
            opt_d = resultSet_easy.getString(6);
            para = para + "<p>" + String.valueOf(index) + ". " + question + " ( 1 mark )</p>" + string_input_tag + "question_id_" + ques_id + string_val_a + "  " + opt_a + "<br>"
                    + string_input_tag + "question_id_" + ques_id + string_val_b + "  " + opt_b + "<br>"
                    + string_input_tag + "question_id_" + ques_id + string_val_c + "  " + opt_c + "<br>"
                    + string_input_tag + "question_id_" + ques_id + string_val_d + "  " + opt_d + "<br>";
            easy_count++;
            index++;
        }

        int  medium_count = 1;
        while (resultSet_medium.next()) {
            if(medium_count > n_medium)
                break;
            ques_id = String.valueOf(resultSet_medium.getInt(1));
            question = resultSet_medium.getString(2);
            opt_a = resultSet_medium.getString(3);
            opt_b = resultSet_medium.getString(4);
            opt_c = resultSet_medium.getString(5);
            opt_d = resultSet_medium.getString(6);
            para = para + "<p>" + String.valueOf(index) + ". " + question + " ( 2 marks )</p>" + string_input_tag + "question_id_" + ques_id + string_val_a + "  " + opt_a + "<br>"
                    + string_input_tag + "question_id_" + ques_id + string_val_b + "  " + opt_b + "<br>"
                    + string_input_tag + "question_id_" + ques_id + string_val_c + "  " + opt_c + "<br>"
                    + string_input_tag + "question_id_" + ques_id + string_val_d + "  " + opt_d + "<br>";
            medium_count++;
            index++;
        }

        int hard_count = 1;
        while (resultSet_hard.next()) {
            if(hard_count > n_hard)
                break;
            ques_id = String.valueOf(resultSet_hard.getInt(1));
            question = resultSet_hard.getString(2);
            opt_a = resultSet_hard.getString(3);
            opt_b = resultSet_hard.getString(4);
            opt_c = resultSet_hard.getString(5);
            opt_d = resultSet_hard.getString(6);
            para = para + "<p>" + String.valueOf(index) + ". " + question + " ( 3 marks )</p>" + string_input_tag + "question_id_" + ques_id + string_val_a + "  " + opt_a + "<br>"
                    + string_input_tag + "question_id_" + ques_id + string_val_b + "  " + opt_b + "<br>"
                    + string_input_tag + "question_id_" + ques_id + string_val_c + "  " + opt_c + "<br>"
                    + string_input_tag + "question_id_" + ques_id + string_val_d + "  " + opt_d + "<br>";
            hard_count++;
            index++;
        }
        return head + para + "<br>"+ end;
    }

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

    end = "<input type=\"submit\" value = \"Evaluate Quiz\" id=\"add-btn\"/>\n" +
            "</form>\n" +
            "\n" +
            "</body>\n" +
            "</html>";


    String para ="";

    String string_input_tag = "<input type=\"radio\" name=\"" ,
            string_val_a= "\" value=\"a\">",
            string_val_b= "\" value=\"b\">",
            string_val_c= "\" value=\"c\">",
            string_val_d= "\" value=\"d\">";

    // end of instance variables

}
