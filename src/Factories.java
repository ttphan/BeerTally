/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Various factory methods to create objects on the fly.
 */

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Factories {
	
	/**
	 * Creates roommate GUI objects to be used in the tally panel.
	 * @return	RoommateGUI object
	 */
	public static RoommateGUI roommateFactory() {
		JButton tallyButton = new JButton();
		tallyButton.setPreferredSize(new Dimension(100, 100));
		
		JLabel totalTallyLabel = new JLabel("0");
		totalTallyLabel.setPreferredSize(new Dimension(40, 20));

		JLabel diffTallyLabel = new JLabel("0");
		diffTallyLabel.setPreferredSize(new Dimension(40, 20));
		
		
		return new RoommateGUI(tallyButton, totalTallyLabel, diffTallyLabel);
	}
	
	public static JButton menuButtonFactory(String title, JPanel panel, int index) {
		JButton button = new JButton(title);
		button.setPreferredSize(new Dimension(250, 80));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 10, 0);
		gbc.gridx = 0;
		gbc.gridy = index;
		panel.add(button, gbc);
		
		return button;
	}
}
