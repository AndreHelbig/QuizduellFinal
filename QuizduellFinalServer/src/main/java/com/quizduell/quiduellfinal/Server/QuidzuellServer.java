/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizduell.quiduellfinal.Server;

import com.quizduell.quiduellfinal.Server.resource.DuelResource;
import com.quizduell.quiduellfinal.Server.domain.Duel;
import com.quizduell.quiduellfinal.Server.domain.Question;
import com.quizduell.quiduellfinal.Server.domain.Turn;
import com.quizduell.quiduellfinal.Server.resource.QuestionResource;
import com.quizduell.quiduellfinal.Server.resource.TurnResource;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Andre
 */
public class QuidzuellServer {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static CassandraConnector connector = null;

    private static final int maxClientsCount = 10;
    private static final ClientThread[] threads = new ClientThread[maxClientsCount];

    public static void main(String args[]) {

        int portNumber = 4322;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

        connector = new CassandraConnector();
        connector.connect();

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new ClientThread(clientSocket, threads)).start();
                        System.out.println("Client connected" + i);
                        break;
                    }
                }
                if (i == maxClientsCount) {
                    DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
                    os.writeBytes("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

class ClientThread extends Thread {

    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final ClientThread[] threads;
    private int maxClientsCount;

    public ClientThread(Socket clientSocket, ClientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        ClientThread[] threads = this.threads;
        try {
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
            os.println("Enter the name of Player 1");
            String player1 = is.readLine().trim();

            os.println("Enter the name of Player 2");
            String player2 = is.readLine().trim();

            os.println("Creating Duel between " + player1 + " and " + player2);
            DuelResource.createDuel(new Duel(player1, player2));

            Duel duel = DuelResource.getDuel(player1, player2);
            
            os.println(duel.activePlayer() + "'s turn: \n");
            
            Turn turn = new Turn(duel.getId(), duel.activePlayer());
            List<Question> questions = QuestionResource.getQuestions();

            for (int i = 0; i < 3; i++) {
                Question question = questions.get(i);
                os.println(question.text);
                os.println("Choose answer 1-4");
                for (int j = 0; j < question.answers.length; j++) {
                    os.println(j + 1 + ". " + question.answers[j]);
                }
                String answer = question.answers[new Integer(is.readLine().trim())-1];
                if(QuestionResource.validateAnswer(question.id, answer)){
                   turn.correctAnswers++; 
                }                
            }
            TurnResource.persistTurn(turn);
            
            while (true) {
                String line = is.readLine();
                if (line.startsWith("/quit")) {
                    break;
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null) {
                        threads[i].os.println("<" + "&gr; " + line);
                    }
                }
            }
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("*** The user "
                            + " is leaving the chat room !!! ***");
                }
            }
            os.println("");

            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        }

    }
}
