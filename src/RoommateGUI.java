/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Wrapper for the buttons and labels for the corresponding roommate.
 */

import javax.swing.JButton;
import javax.swing.JLabel;

public class RoommateGUI {
	private JButton tallyButton;
	private JLabel totalTallyLabel;
	private JLabel diffTallyLabel;
	
	public RoommateGUI(JButton tBtn, JLabel ttL, JLabel tdL) {
		tallyButton = tBtn;
		totalTallyLabel = ttL;
		diffTallyLabel = tdL;
	}

	public JButton getTallyButton() {
		return tallyButton;
	}

	public void setTallyButton(JButton tallyButton) {
		this.tallyButton = tallyButton;
	}

	public JLabel getTotalTallyLabel() {
		return totalTallyLabel;
	}

	public void setTotalTallyLabel(JLabel tallyTotalLabel) {
		this.totalTallyLabel = tallyTotalLabel;
	}

	public JLabel getDiffTallyLabel() {
		return diffTallyLabel;
	}

	public void setDiffTallyLabel(JLabel tallyDiffLabel) {
		this.diffTallyLabel = tallyDiffLabel;
	}
}
