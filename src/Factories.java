import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;


public class Factories {
	
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
