/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizduell.quiduellfinal.Server.resource;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.quizduell.quiduellfinal.Server.CassandraConnector;
import com.quizduell.quiduellfinal.Server.domain.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Andre
 */
public class QuestionResource {
    
    public static void persistQuestion(Question question) {
        CassandraConnector client = new CassandraConnector();
        client.getSession().execute("INSERT INTO quizduell.question (id, text, answer1, answer2, answer3, answer4) VALUES(?, ?, ?, ?, ?, ?)", 
                question.id.toString(), question.text, question.answer1, question.answer2, question.answer3, question.answer4);
        client.close();
    }
    
    public static List<Question> getQuestions(){
        CassandraConnector client = new CassandraConnector();
        Statement select = QueryBuilder.select().all().from("quizduell", "question");
        ResultSet rs = client.getSession().execute(select);
        List<Question> questions = new ArrayList<Question>();
        for (Row row : rs){
            questions.add(new Question(
                    UUID.fromString(row.getString("id")),
                    row.getString("text"),
                    row.getString("answer1"),
                    row.getString("answer2"),
                    row.getString("answer3"),
                    row.getString("answer4")
            ));
        }
        client.close();
        return questions;
    }
    
    public static Question getQuestionById(UUID id) {
        CassandraConnector client = new CassandraConnector();
        ResultSet rs = client.getSession().execute(
                "SELECT * from quizduell.question WHERE id = '"+ id.toString() +"'ALLOW FILTERING");
        Question question = null;
        for (Row row : rs) {
            question = new Question(
                    UUID.fromString(row.getString("id")),
                    row.getString("text"),
                    row.getString("answer1"),
                    row.getString("answer2"),
                    row.getString("answer3"),
                    row.getString("answer4"));
        }
        client.close();
        return question;
    }
    
    public static boolean validateAnswer(UUID questionId, String answer){
        Question question = getQuestionById(questionId);
        if(question.answer1.equals(answer))
            return true;
        return false;
    }
    
    public static void seedDatabase(){
        persistQuestion(new Question(
                "Wie viele Einwohner hat Karlsurhe?",
                "300000",
                "250000",
                "200000",
                "150000"
        ));
        persistQuestion(new Question(
                "Wann war die Mondlandung",
                "1969",
                "1979",
                "1975",
                "1965"
        ));
        persistQuestion(new Question(
                "Wie viele Millionenstädte hat Deutschland?",
                "4",
                "3",
                "5",
                "6"
        ));
        
    }
}
