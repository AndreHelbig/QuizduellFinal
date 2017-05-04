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
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import com.quizduell.quiduellfinal.Server.CassandraConnector;
import com.quizduell.quiduellfinal.Server.domain.Duel;
import com.quizduell.quiduellfinal.Server.domain.Turn;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

/**
 *
 * @author Andre
 */
public class TurnResource {

    public static void persistTurn(Turn turn) {
        CassandraConnector client = new CassandraConnector();
        client.getSession().execute("INSERT INTO quizduell.turn (id, duelId, playerName, correctAnswers) VALUES(?, ?, ?, ?)",
                turn.id.toString(), turn.duelId.toString(), turn.playerName, turn.correctAnswers);
        client.close();
    }

    public static int countResultsOfDuel(UUID duelId) {
        CassandraConnector client = new CassandraConnector();
        ResultSet rs = client.getSession().execute("SELECT * FROM quizduell.turn WHERE duelId = '" + duelId.toString() + "' ALLOW FILTERING");
        List<Turn> turns = new ArrayList<Turn>();
        for (Row row : rs) {
            turns.add(new Turn(
                    UUID.fromString(row.getString("id")),
                    UUID.fromString(row.getString("duelId")),
                    row.getString("playerName"),
                    row.getInt("correctAnswers")
            ));
        }
        client.close();
        Duel duel = DuelResource.getDuelById(duelId);
        //if count >0 player1 wins if < 0 player 2
        int count = 0;
        for (int i = 0; i < turns.size(); i++) {
            Turn temp = turns.get(i);
            if (temp.playerName == duel.player1) {
                count += temp.getCorrectAnswers();
            } else {
                count -= temp.getCorrectAnswers();
            }

        }
        return count;

    }
}
