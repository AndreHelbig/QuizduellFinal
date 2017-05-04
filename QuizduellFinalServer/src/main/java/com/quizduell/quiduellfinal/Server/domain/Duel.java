/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizduell.quiduellfinal.Server.domain;


import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Andre
 */
public class Duel {
    @Getter
    @Setter
    public UUID id;
    @Getter
    @Setter
    public String player1;
    @Getter
    @Setter
    public String player2;
    @Getter
    @Setter
    public int turn;
    
    public Duel(String player1, String player2){
        this.id = UUID.randomUUID();
        this.player1 = player1;
        this.player2 = player2;
        this.turn = 1;
    }
    
    public boolean isActive(){
        if(turn<=2){
            return true;
        }
        return false;
    }
    public String activePlayer(){
        if(turn%2 ==0){
            return player1;
        }
        return player2;
    }
    public Duel() {
   
    }
}
