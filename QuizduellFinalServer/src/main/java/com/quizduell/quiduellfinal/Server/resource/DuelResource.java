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
import com.quizduell.quiduellfinal.Server.domain.Duel;
import java.util.UUID;

/**
 *
 * @author Andre
 */
public  class DuelResource {
    
    
    public static void createDuel(Duel duel){
        CassandraConnector client = new CassandraConnector();
        client.getSession().execute("INSERT INTO quizduell.duel (id, player1, player2, turn) VALUES(?, ?, ?, ?)", duel.id.toString(), duel.player1, duel.player2, duel.turn);
        client.close();
    }
    
    public static void updateDuel(Duel duel){
        CassandraConnector client = new CassandraConnector();
        Statement update = QueryBuilder.update("quizduell", "duel").with(QueryBuilder.set("turn", duel.turn)).where(QueryBuilder.eq("id", duel.id.toString()));
        client.getSession().execute(update);
        client.close();
    }
    
    public static Duel getDuel(String player1, String player2){
        CassandraConnector client = new CassandraConnector();
        ResultSet rs = client.getSession().execute(
                "SELECT * from quizduell.duel WHERE player1 = '" + player1 +"' AND player2 = '" + player2+"' ALLOW FILTERING");
        Duel duel = null;
        for (Row row : rs) {
            duel = new Duel();
            duel.setId(UUID.fromString(row.getString("id")));
            duel.setPlayer1(row.getString("player1"));
            duel.setPlayer2(row.getString("player2"));
        }
        client.close();
        return duel;
    }

    public static Duel getDuelById(UUID duelId) {
        CassandraConnector client = new CassandraConnector();
        ResultSet rs = client.getSession().execute(
                "SELECT * from quizduell.duel WHERE id = '"+ duelId.toString() +"' ALLOW FILTERING");
        Duel duel = null;
        for (Row row : rs) {
            duel = new Duel();
            duel.setId(UUID.fromString(row.getString("id")));
            duel.setPlayer1(row.getString("player1"));
            duel.setPlayer2(row.getString("player2"));
        }
        client.close();
        return duel;    
    }
        
}
