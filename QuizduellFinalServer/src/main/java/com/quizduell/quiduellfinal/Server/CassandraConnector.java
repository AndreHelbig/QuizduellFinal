/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizduell.quiduellfinal.Server;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

/**
 *
 * @author Andre
 */
public class CassandraConnector {
    private Cluster cluster;
    private Session session;
    
    
    public void connect(){
        this.cluster = Cluster.builder().addContactPoint("192.168.99.100").withPort(19042).build();
        
        session = cluster.connect();
    }
    
    public Session getSession(){
        if(this.session == null){
            connect();
        }
        return this.session;
    }
    
    public void close(){
        cluster.close();
    }
}
