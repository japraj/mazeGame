package ui.controller;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;

// Set look & feel and initialize the application
public class Main {

    public static void main(String[] args) throws InterruptedException {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize Look & Feel");
        }
        new MazeGame();
    }

}
