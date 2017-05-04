/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizduell.quiduellfinal.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andre
 */
public class Server implements Runnable {

    private int port = 4041;
    private ServerSocket serverSocket = null;
    private boolean isActive = true;
    private Thread thread = null;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        new Thread(server).start();

        try {
            Thread.sleep(2000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();
    }

    @Override
    public void run() {
        synchronized (this) {
            this.thread = Thread.currentThread();
        }
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (isActive()) {

            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isActive()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            try {
                new Thread(
                        new ServerThread(
                                clientSocket)
                ).start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private synchronized boolean isActive() {
        return this.isActive;
    }

    private synchronized void stop() throws IOException {
        this.isActive = false;
        serverSocket.close();
    }

}
