/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabriel;

import java.io.FileInputStream;
import javazoom.jl.player.*;
import javazoom.jl.player.advanced.AdvancedPlayer;
/**
 *
 * @author Antonette
 */
public class PlayerThread extends Thread{
    private String fileLocation;
    private boolean loop;
    private AdvancedPlayer player;

    public PlayerThread(String fileLocation, boolean loop) {
        this.fileLocation = fileLocation;
        this.loop = loop;
    }
    
    public void run() {
        try {
            do {
                FileInputStream buff = new FileInputStream(fileLocation);
                player = new AdvancedPlayer(buff);
                player.play();
            } while (loop);
        } catch (Exception ioe) {
            // TODO error handling
        }
    }

    public void close(){
        loop = false;
        player.close();
        this.interrupt();
    }
}
