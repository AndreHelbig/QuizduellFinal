/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizduell.quiduellfinal.Server;

import com.quizduell.quiduellfinal.Server.domain.Duel;
import com.quizduell.quiduellfinal.Server.domain.Question;
import com.quizduell.quiduellfinal.Server.domain.Turn;
import com.quizduell.quiduellfinal.Server.resource.DuelResource;
import com.quizduell.quiduellfinal.Server.resource.QuestionResource;
import com.quizduell.quiduellfinal.Server.resource.TurnResource;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Andre
 */
public class ServerThread implements Runnable {

    private Socket client;
    public Scanner userInput;
    public Scanner in;
    public PrintWriter out;
    

    public ServerThread(Socket client) throws IOException {
        this.client = client;
        in = new Scanner(client.getInputStream());
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        Duel duel = createDuel();
        playDuel(duel);
        evaluateDuel(duel);
    }

    private Duel createDuel() {
        out.println("Enter the name of Player 1");
        out.println("EOF");
        String player1 = in.nextLine();

        out.println("Enter the name of Player 2");
        out.println("EOF");
        String player2 = in.nextLine();

        out.println("Creating Duel between " + player1 + " and " + player2);
        DuelResource.createDuel(new Duel(player1, player2));
        return DuelResource.getDuel(player1, player2);
    }

    private void playDuel(Duel duel) {
        for(int k = 0; k<2; k++){
        out.println(duel.activePlayer() + "'s turn: ");
        Turn turn = new Turn(duel.getId(), duel.activePlayer());
        List<Question> questions = QuestionResource.getQuestions();

        for (int i = 0; i < 3; i++) {
            Question question = questions.get(i);
            out.println(question.text);
            out.println("Choose answer 1-4");
            for (int j = 0; j < question.answers.length; j++) {
                out.println(j + 1 + ". " + question.answers[j]);
            }
            out.println("EOF");
            String answer = question.answers[new Integer(in.nextLine())- 1];
            if (QuestionResource.validateAnswer(question.id, answer)) {
                turn.correctAnswers++;
                out.println("Your answer was correct\n");
            }else{
                out.println("Your answer was wrong\n");
            }
        }
        TurnResource.persistTurn(turn);
        duel.turn = 1;
        DuelResource.updateDuel(duel);
        }
    }

    private void evaluateDuel(Duel duel) {
        int result = TurnResource.countResultsOfDuel(duel.id);
        if(result < 0 ) {
            out.println(duel.player1 +" has won the duel");
        }else if(result ==0){
            out.println("Noone has won the duel");
        }else if(result > 0){
            out.println(duel.player2 + " has won the duel");
        }
    }
    
    

}
