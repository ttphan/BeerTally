/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Various factory methods to create objects on the fly.
 */

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;


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
		totalTallyLabel.setPreferredSize(new Dimension(40, 20));
		
		return new RoommateGUI(tallyButton, totalTallyLabel, diffTallyLabel);
	}
}
