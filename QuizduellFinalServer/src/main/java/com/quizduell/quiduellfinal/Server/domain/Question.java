/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizduell.quiduellfinal.Server.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Andre
 */
public class Question {
    @Getter
    @Setter
    public UUID id;
    @Getter
    @Setter
    public String text;
    @Getter
    @Setter
    public String answer1;
    @Getter
    @Setter
    public String answer2;
    @Getter
    @Setter
    public String answer3;
    @Getter
    @Setter
    public String answer4;
    @Getter
    @Setter
    public String[] answers;
    

    public Question(UUID id, String text, String answer1, String answer2, String answer3, String answer4) {
        this.id = id;
        this.text = text;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        
        answers = new String[] {answer1, answer2, answer3, answer4};
        int index;
        String temp;
        Random random = new Random();
        for (int i = answers.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = answers[index];
            answers[index] = answers[i];
            answers[i] = temp;
        }
    }
    public Question(String text, String answer1, String answer2, String answer3, String answer4) {
        this.id = UUID.randomUUID();
        this.text = text;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
    }
        
    
    
}
