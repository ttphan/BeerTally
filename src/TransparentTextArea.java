/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Subclass of JTextArea, makes the history log translucent.
 */

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class TransparentTextArea extends JTextArea {

    public TransparentTextArea() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
    	// Set opacity to 75% (256 * 0.75)
        g.setColor(new Color(255, 255, 255, 194));
        int width = getWidth();
        int height = getHeight();
        g.fillRect(0, 0, width, height);
        super.paintComponent(g);
    }

}