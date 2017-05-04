/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizduell.quiduellfinal.Server.domain;

import com.quizduell.quiduellfinal.Server.resource.DuelResource;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Andre
 */
public class Turn {
    @Getter
    @Setter
    public UUID id;
    @Getter
    @Setter
    public UUID duelId;
    @Getter
    @Setter
    public String playerName;
    @Getter
    @Setter
    public int correctAnswers;

    public Turn(UUID duelId, String playerName) {
        this.id = UUID.randomUUID();
        this.duelId = duelId;
        this.playerName = playerName;
        this.correctAnswers = 0;
    }
    
    public Turn(UUID id, UUID duelId, String playerName, int correctAnswers) {
        this.id = id;
        this.duelId = duelId;
        this.playerName = playerName;
        this.correctAnswers = correctAnswers;
    }
    
    
}
