/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Main GUI class, handles most of the GUI aspects of the application.
 */

import java.awt.*;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainGui {

	private JFrame frame;
	
	/**
	 * Tally button panel, containing the roommates and tally count.
	 */
	private JPanel pTallyButtons;
	
	/**
	 * Left panel, containing the tally buttons and scrolling text.
	 */
	
	/**
	 * Quotes panel, scrolling text from right to left
	 */
	
	MarqueePanel pQuotes;
	
	private JPanel pLeft;
	
	/**
	 * Right panel, containing history and menu.
	 */
	private JPanel pRight;
	
	/**
	 * Panel with menu buttons.
	 */
	private JPanel pRightButtons;
	
	/**
	 * History log panel.
	 */
	private JPanel pHistory;
	
	/**
	 * Apply button, commits the tallies to the database.
	 */
	private JButton bApplyTally;
	
	/**
	 * Cancel button, rolls back the uncommitted tallies.
	 */
	private JButton bCancelTally;
	
	/**
	 * History log.
	 */
	private JTextArea taHistory;
	
	/**
	 * Buttons and labels relevant to a roommate.
	 */
	private Map<Integer, RoommateGUI> mpRoommates = new HashMap<Integer, RoommateGUI>();
	
	private Map<Integer, String> names = DBHandler.getActiveRoommates();
	
	/**
	 * Used to fetch the resolutions of the screen
	 */
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui window = new MainGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGui() {
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		// Adjust size according to screen resolution		
		frame.setBounds(0, 0, screen.width, screen.height);
		
		// Maximized
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		// Full screen without menu
		frame.setUndecorated(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// Split the pane in two sides, left the buttons, right the history log and apply button
		JSplitPane spMain = new JSplitPane();
		spMain.setEnabled(false);
		spMain.setBounds(0, 0, screen.width, screen.height);
		spMain.setDividerLocation(0.66);
		spMain.setDividerSize(0);
		
		frame.getContentPane().add(spMain);
		
		// Create and initialize right panel
		pRight = new JPanel();
		spMain.setRightComponent(pRight);	
		
		pLeft = new JPanel();
		spMain.setLeftComponent(pLeft);
		pLeft.setLayout(new BorderLayout(0, 0));
		
		// Create and initialize left panel
		pTallyButtons = new JPanel();
		pLeft.add(pTallyButtons, BorderLayout.CENTER);
		// Auto-generated with WindowBuilder
		GridBagLayout gbl_pTallyButtons = new GridBagLayout();
		gbl_pTallyButtons.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pTallyButtons.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pTallyButtons.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 
				1.0, 1.0, Double.MIN_VALUE};
		gbl_pTallyButtons.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 
				1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		pTallyButtons.setLayout(gbl_pTallyButtons);
		
		JButton bRM_1 = new JButton("1");
		bRM_1.setPreferredSize(new Dimension(100, 100));	
		GridBagConstraints gbc_bRM_1 = new GridBagConstraints();
		gbc_bRM_1.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_1.gridx = 1;
		gbc_bRM_1.gridy = 2;
		pTallyButtons.add(bRM_1, gbc_bRM_1);
		
		JLabel lRMDiffTally_1 = new JLabel("0");
		lRMDiffTally_1.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_1 = new GridBagConstraints();
		gbc_lRMDiffTally_1.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_1.gridx = 3;
		gbc_lRMDiffTally_1.gridy = 2;
		pTallyButtons.add(lRMDiffTally_1, gbc_lRMDiffTally_1);
		
		JLabel lRMTally_8 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_8 = new GridBagConstraints();
		gbc_lRMTally_8.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_8.gridx = 5;
		gbc_lRMTally_8.gridy = 2;
		pTallyButtons.add(lRMTally_8, gbc_lRMTally_8);
		
		JLabel lRMDiffTally_8 = new JLabel("0");
		lRMDiffTally_8.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_8 = new GridBagConstraints();
		gbc_lRMDiffTally_8.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_8.gridx = 6;
		gbc_lRMDiffTally_8.gridy = 2;
		pTallyButtons.add(lRMDiffTally_8, gbc_lRMDiffTally_8);
		
		JButton bRM_15 = new JButton("15");
		bRM_15.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_15 = new GridBagConstraints();
		gbc_bRM_15.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_15.gridx = 7;
		gbc_bRM_15.gridy = 2;
		pTallyButtons.add(bRM_15, gbc_bRM_15);
		
		JLabel lRMTally_15 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_15 = new GridBagConstraints();
		gbc_lRMTally_15.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_15.gridx = 8;
		gbc_lRMTally_15.gridy = 2;
		pTallyButtons.add(lRMTally_15, gbc_lRMTally_15);
		
		JLabel lRMDiffTally_15 = new JLabel("0");
		lRMDiffTally_15.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_15 = new GridBagConstraints();
		gbc_lRMDiffTally_15.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_15.gridx = 9;
		gbc_lRMDiffTally_15.gridy = 2;
		pTallyButtons.add(lRMDiffTally_15, gbc_lRMDiffTally_15);
		
		JButton bRM_2 = new JButton("2");
		bRM_2.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_2 = new GridBagConstraints();
		gbc_bRM_2.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_2.gridx = 1;
		gbc_bRM_2.gridy = 4;
		pTallyButtons.add(bRM_2, gbc_bRM_2);
		
		JLabel lRMDiffTally_2 = new JLabel("0");
		lRMDiffTally_2.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_2 = new GridBagConstraints();
		gbc_lRMDiffTally_2.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_2.gridx = 3;
		gbc_lRMDiffTally_2.gridy = 4;
		pTallyButtons.add(lRMDiffTally_2, gbc_lRMDiffTally_2);
		
		JLabel lRMTally_9 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_9 = new GridBagConstraints();
		gbc_lRMTally_9.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_9.gridx = 5;
		gbc_lRMTally_9.gridy = 4;
		pTallyButtons.add(lRMTally_9, gbc_lRMTally_9);
		
		JLabel lRMDiffTally_9 = new JLabel("0");
		lRMDiffTally_9.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_9 = new GridBagConstraints();
		gbc_lRMDiffTally_9.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_9.gridx = 6;
		gbc_lRMDiffTally_9.gridy = 4;
		pTallyButtons.add(lRMDiffTally_9, gbc_lRMDiffTally_9);
		
		JButton bRM_16 = new JButton("16");
		bRM_16.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_16 = new GridBagConstraints();
		gbc_bRM_16.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_16.gridx = 7;
		gbc_bRM_16.gridy = 4;
		pTallyButtons.add(bRM_16, gbc_bRM_16);
		
		JLabel lRMTally_16 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_16 = new GridBagConstraints();
		gbc_lRMTally_16.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_16.gridx = 8;
		gbc_lRMTally_16.gridy = 4;
		pTallyButtons.add(lRMTally_16, gbc_lRMTally_16);
		
		JLabel lRMDiffTally_16 = new JLabel("0");
		lRMDiffTally_16.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_16 = new GridBagConstraints();
		gbc_lRMDiffTally_16.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_16.gridx = 9;
		gbc_lRMDiffTally_16.gridy = 4;
		pTallyButtons.add(lRMDiffTally_16, gbc_lRMDiffTally_16);
		
		JButton bRM_3 = new JButton("3");
		bRM_3.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_3 = new GridBagConstraints();
		gbc_bRM_3.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_3.gridx = 1;
		gbc_bRM_3.gridy = 6;
		pTallyButtons.add(bRM_3, gbc_bRM_3);
		
		JLabel lRMDiffTally_3 = new JLabel("0");
		lRMDiffTally_3.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_3 = new GridBagConstraints();
		gbc_lRMDiffTally_3.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_3.gridx = 3;
		gbc_lRMDiffTally_3.gridy = 6;
		pTallyButtons.add(lRMDiffTally_3, gbc_lRMDiffTally_3);
		
		JLabel lRMTally_10 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_10 = new GridBagConstraints();
		gbc_lRMTally_10.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_10.gridx = 5;
		gbc_lRMTally_10.gridy = 6;
		pTallyButtons.add(lRMTally_10, gbc_lRMTally_10);
		
		JLabel lRMDiffTally_10 = new JLabel("0");
		lRMDiffTally_10.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_10 = new GridBagConstraints();
		gbc_lRMDiffTally_10.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_10.gridx = 6;
		gbc_lRMDiffTally_10.gridy = 6;
		pTallyButtons.add(lRMDiffTally_10, gbc_lRMDiffTally_10);
		
		JButton bRM_17 = new JButton("17");
		bRM_17.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_17 = new GridBagConstraints();
		gbc_bRM_17.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_17.gridx = 7;
		gbc_bRM_17.gridy = 6;
		pTallyButtons.add(bRM_17, gbc_bRM_17);
		
		JLabel lRMTally_17 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_17 = new GridBagConstraints();
		gbc_lRMTally_17.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_17.gridx = 8;
		gbc_lRMTally_17.gridy = 6;
		pTallyButtons.add(lRMTally_17, gbc_lRMTally_17);
		
		JLabel lRMDiffTally_17 = new JLabel("0");
		lRMDiffTally_17.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_17 = new GridBagConstraints();
		gbc_lRMDiffTally_17.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_17.gridx = 9;
		gbc_lRMDiffTally_17.gridy = 6;
		pTallyButtons.add(lRMDiffTally_17, gbc_lRMDiffTally_17);
		
		JButton bRM_4 = new JButton("4");
		bRM_4.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_4 = new GridBagConstraints();
		gbc_bRM_4.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_4.gridx = 1;
		gbc_bRM_4.gridy = 8;
		pTallyButtons.add(bRM_4, gbc_bRM_4);
		
		JLabel lRMDiffTally_4 = new JLabel("0");
		lRMDiffTally_4.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_4 = new GridBagConstraints();
		gbc_lRMDiffTally_4.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_4.gridx = 3;
		gbc_lRMDiffTally_4.gridy = 8;
		pTallyButtons.add(lRMDiffTally_4, gbc_lRMDiffTally_4);
		
		JLabel lRMTally_11 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_11 = new GridBagConstraints();
		gbc_lRMTally_11.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_11.gridx = 5;
		gbc_lRMTally_11.gridy = 8;
		pTallyButtons.add(lRMTally_11, gbc_lRMTally_11);
		
		JLabel lRMDiffTally_11 = new JLabel("0");
		lRMDiffTally_11.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_11 = new GridBagConstraints();
		gbc_lRMDiffTally_11.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_11.gridx = 6;
		gbc_lRMDiffTally_11.gridy = 8;
		pTallyButtons.add(lRMDiffTally_11, gbc_lRMDiffTally_11);
		
		JButton bRM_18 = new JButton("18");
		bRM_18.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_18 = new GridBagConstraints();
		gbc_bRM_18.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_18.gridx = 7;
		gbc_bRM_18.gridy = 8;
		pTallyButtons.add(bRM_18, gbc_bRM_18);
		
		JLabel lRMTally_18 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_18 = new GridBagConstraints();
		gbc_lRMTally_18.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_18.gridx = 8;
		gbc_lRMTally_18.gridy = 8;
		pTallyButtons.add(lRMTally_18, gbc_lRMTally_18);
		
		JLabel lRMDiffTally_18 = new JLabel("0");
		lRMDiffTally_18.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_18 = new GridBagConstraints();
		gbc_lRMDiffTally_18.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_18.gridx = 9;
		gbc_lRMDiffTally_18.gridy = 8;
		pTallyButtons.add(lRMDiffTally_18, gbc_lRMDiffTally_18);
		
		JButton bRM_5 = new JButton("5");
		bRM_5.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_5 = new GridBagConstraints();
		gbc_bRM_5.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_5.gridx = 1;
		gbc_bRM_5.gridy = 10;
		pTallyButtons.add(bRM_5, gbc_bRM_5);
		
		JLabel lRMDiffTally_5 = new JLabel("0");
		lRMDiffTally_5.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_5 = new GridBagConstraints();
		gbc_lRMDiffTally_5.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_5.gridx = 3;
		gbc_lRMDiffTally_5.gridy = 10;
		pTallyButtons.add(lRMDiffTally_5, gbc_lRMDiffTally_5);
		
		JLabel lRMTally_12 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_12 = new GridBagConstraints();
		gbc_lRMTally_12.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_12.gridx = 5;
		gbc_lRMTally_12.gridy = 10;
		pTallyButtons.add(lRMTally_12, gbc_lRMTally_12);
		
		JLabel lRMDiffTally_12 = new JLabel("0");
		lRMDiffTally_12.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_12 = new GridBagConstraints();
		gbc_lRMDiffTally_12.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_12.gridx = 6;
		gbc_lRMDiffTally_12.gridy = 10;
		pTallyButtons.add(lRMDiffTally_12, gbc_lRMDiffTally_12);
		
		JButton bRM_19 = new JButton("19");
		bRM_19.setEnabled(false);
		bRM_19.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_19 = new GridBagConstraints();
		gbc_bRM_19.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_19.gridx = 7;
		gbc_bRM_19.gridy = 10;
		pTallyButtons.add(bRM_19, gbc_bRM_19);
		
		JLabel lRMTally_19 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_19 = new GridBagConstraints();
		gbc_lRMTally_19.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_19.gridx = 8;
		gbc_lRMTally_19.gridy = 10;
		pTallyButtons.add(lRMTally_19, gbc_lRMTally_19);
		
		JLabel lRMDiffTally_19 = new JLabel("0");
		lRMDiffTally_19.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_19 = new GridBagConstraints();
		gbc_lRMDiffTally_19.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_19.gridx = 9;
		gbc_lRMDiffTally_19.gridy = 10;
		pTallyButtons.add(lRMDiffTally_19, gbc_lRMDiffTally_19);
		
		JButton bRM_6 = new JButton("6");
		bRM_6.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_6 = new GridBagConstraints();
		gbc_bRM_6.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_6.gridx = 1;
		gbc_bRM_6.gridy = 12;
		pTallyButtons.add(bRM_6, gbc_bRM_6);
		
		JLabel lRMDiffTally_6 = new JLabel("0");
		lRMDiffTally_6.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_6 = new GridBagConstraints();
		gbc_lRMDiffTally_6.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_6.gridx = 3;
		gbc_lRMDiffTally_6.gridy = 12;
		pTallyButtons.add(lRMDiffTally_6, gbc_lRMDiffTally_6);
		
		JLabel lRMTally_13 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_13 = new GridBagConstraints();
		gbc_lRMTally_13.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_13.gridx = 5;
		gbc_lRMTally_13.gridy = 12;
		pTallyButtons.add(lRMTally_13, gbc_lRMTally_13);
		
		JLabel lRMDiffTally_13 = new JLabel("0");
		lRMDiffTally_13.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_13 = new GridBagConstraints();
		gbc_lRMDiffTally_13.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_13.gridx = 6;
		gbc_lRMDiffTally_13.gridy = 12;
		pTallyButtons.add(lRMDiffTally_13, gbc_lRMDiffTally_13);
		
		JButton bRM_20 = new JButton("20");
		bRM_20.setEnabled(false);
		bRM_20.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_20 = new GridBagConstraints();
		gbc_bRM_20.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_20.gridx = 7;
		gbc_bRM_20.gridy = 12;
		pTallyButtons.add(bRM_20, gbc_bRM_20);
		
		JLabel lRMTally_20 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_20 = new GridBagConstraints();
		gbc_lRMTally_20.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_20.gridx = 8;
		gbc_lRMTally_20.gridy = 12;
		pTallyButtons.add(lRMTally_20, gbc_lRMTally_20);
		
		JLabel lRMDiffTally_20 = new JLabel("0");
		lRMDiffTally_20.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_20 = new GridBagConstraints();
		gbc_lRMDiffTally_20.insets = new Insets(0, 0, 5, 5);
		gbc_lRMDiffTally_20.gridx = 9;
		gbc_lRMDiffTally_20.gridy = 12;
		pTallyButtons.add(lRMDiffTally_20, gbc_lRMDiffTally_20);
		
		JButton bRM_7 = new JButton("7");
		bRM_7.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_7 = new GridBagConstraints();
		gbc_bRM_7.insets = new Insets(0, 0, 0, 5);
		gbc_bRM_7.gridx = 1;
		gbc_bRM_7.gridy = 14;
		pTallyButtons.add(bRM_7, gbc_bRM_7);
		
		JButton bRM_8 = new JButton("8");
		bRM_8.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_8 = new GridBagConstraints();
		gbc_bRM_8.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_8.gridx = 4;
		gbc_bRM_8.gridy = 2;
		pTallyButtons.add(bRM_8, gbc_bRM_8);
		
		JButton bRM_9 = new JButton("9");
		bRM_9.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_9 = new GridBagConstraints();
		gbc_bRM_9.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_9.gridx = 4;
		gbc_bRM_9.gridy = 4;
		pTallyButtons.add(bRM_9, gbc_bRM_9);
		
		JButton bRM_10 = new JButton("10");
		bRM_10.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_10 = new GridBagConstraints();
		gbc_bRM_10.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_10.gridx = 4;
		gbc_bRM_10.gridy = 6;
		pTallyButtons.add(bRM_10, gbc_bRM_10);
		
		JButton bRM_11 = new JButton("11");
		bRM_11.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_11 = new GridBagConstraints();
		gbc_bRM_11.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_11.gridx = 4;
		gbc_bRM_11.gridy = 8;
		pTallyButtons.add(bRM_11, gbc_bRM_11);
		
		JButton bRM_12 = new JButton("12");
		bRM_12.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_12 = new GridBagConstraints();
		gbc_bRM_12.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_12.gridx = 4;
		gbc_bRM_12.gridy = 10;
		pTallyButtons.add(bRM_12, gbc_bRM_12);
		
		JButton bRM_13 = new JButton("13");
		bRM_13.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_13 = new GridBagConstraints();
		gbc_bRM_13.insets = new Insets(0, 0, 5, 5);
		gbc_bRM_13.gridx = 4;
		gbc_bRM_13.gridy = 12;
		pTallyButtons.add(bRM_13, gbc_bRM_13);
		
		JLabel lRMDiffTally_7 = new JLabel("0");
		lRMDiffTally_7.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_7 = new GridBagConstraints();
		gbc_lRMDiffTally_7.insets = new Insets(0, 0, 0, 5);
		gbc_lRMDiffTally_7.gridx = 3;
		gbc_lRMDiffTally_7.gridy = 14;
		pTallyButtons.add(lRMDiffTally_7, gbc_lRMDiffTally_7);
		
		JButton bRM_14 = new JButton("14");
		bRM_14.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_14 = new GridBagConstraints();
		gbc_bRM_14.insets = new Insets(0, 0, 0, 5);
		gbc_bRM_14.gridx = 4;
		gbc_bRM_14.gridy = 14;
		pTallyButtons.add(bRM_14, gbc_bRM_14);
		
		JLabel lRMTally_14 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_14 = new GridBagConstraints();
		gbc_lRMTally_14.insets = new Insets(0, 0, 0, 5);
		gbc_lRMTally_14.gridx = 5;
		gbc_lRMTally_14.gridy = 14;
		pTallyButtons.add(lRMTally_14, gbc_lRMTally_14);
		
		JLabel lRMTally_1 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_1 = new GridBagConstraints();
		gbc_lRMTally_1.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_1.gridx = 2;
		gbc_lRMTally_1.gridy = 2;
		pTallyButtons.add(lRMTally_1, gbc_lRMTally_1);
						
		JLabel lRMTally_2 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_2 = new GridBagConstraints();
		gbc_lRMTally_2.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_2.gridx = 2;
		gbc_lRMTally_2.gridy = 4;
		pTallyButtons.add(lRMTally_2, gbc_lRMTally_2);
		
		JLabel lRMTally_3 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_3 = new GridBagConstraints();
		gbc_lRMTally_3.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_3.gridx = 2;
		gbc_lRMTally_3.gridy = 6;
		pTallyButtons.add(lRMTally_3, gbc_lRMTally_3);
		
		JLabel lRMTally_4 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_4 = new GridBagConstraints();
		gbc_lRMTally_4.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_4.gridx = 2;
		gbc_lRMTally_4.gridy = 8;
		pTallyButtons.add(lRMTally_4, gbc_lRMTally_4);
	
		JLabel lRMTally_5 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_5 = new GridBagConstraints();
		gbc_lRMTally_5.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_5.gridx = 2;
		gbc_lRMTally_5.gridy = 10;
		pTallyButtons.add(lRMTally_5, gbc_lRMTally_5);
		
		JLabel lRMTally_6 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_6 = new GridBagConstraints();
		gbc_lRMTally_6.insets = new Insets(0, 0, 5, 5);
		gbc_lRMTally_6.gridx = 2;
		gbc_lRMTally_6.gridy = 12;
		pTallyButtons.add(lRMTally_6, gbc_lRMTally_6);	
	
		JLabel lRMTally_7 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_7 = new GridBagConstraints();
		gbc_lRMTally_7.insets = new Insets(0, 0, 0, 5);
		gbc_lRMTally_7.gridx = 2;
		gbc_lRMTally_7.gridy = 14;
		pTallyButtons.add(lRMTally_7, gbc_lRMTally_7);
		
		JLabel lRMDiffTally_14 = new JLabel("0");
		lRMDiffTally_14.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_14 = new GridBagConstraints();
		gbc_lRMDiffTally_14.insets = new Insets(0, 0, 0, 5);
		gbc_lRMDiffTally_14.gridx = 6;
		gbc_lRMDiffTally_14.gridy = 14;
		pTallyButtons.add(lRMDiffTally_14, gbc_lRMDiffTally_14);
		
		JButton bRM_21 = new JButton("21");
		bRM_21.setEnabled(false);
		bRM_21.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc_bRM_21 = new GridBagConstraints();
		gbc_bRM_21.insets = new Insets(0, 0, 0, 5);
		gbc_bRM_21.gridx = 7;
		gbc_bRM_21.gridy = 14;
		pTallyButtons.add(bRM_21, gbc_bRM_21);	
		
		JLabel lRMTally_21 = new JLabel("0");
		GridBagConstraints gbc_lRMTally_21 = new GridBagConstraints();
		gbc_lRMTally_21.insets = new Insets(0, 0, 0, 5);
		gbc_lRMTally_21.gridx = 8;
		gbc_lRMTally_21.gridy = 14;
		pTallyButtons.add(lRMTally_21, gbc_lRMTally_21);
		
		JLabel lRMDiffTally_21 = new JLabel("0");
		lRMDiffTally_21.setEnabled(false);
		GridBagConstraints gbc_lRMDiffTally_21 = new GridBagConstraints();
		gbc_lRMDiffTally_21.insets = new Insets(0, 0, 0, 5);
		gbc_lRMDiffTally_21.gridx = 9;
		gbc_lRMDiffTally_21.gridy = 14;
		pTallyButtons.add(lRMDiffTally_21, gbc_lRMDiffTally_21);
		
		// Store references to buttons and labels corresponding to the room numbers.
		mpRoommates.put(1, new RoommateGUI(bRM_1, lRMTally_1, lRMDiffTally_1));
		mpRoommates.put(2, new RoommateGUI(bRM_2, lRMTally_2, lRMDiffTally_2));
		mpRoommates.put(3, new RoommateGUI(bRM_3, lRMTally_3, lRMDiffTally_3));
		mpRoommates.put(4, new RoommateGUI(bRM_4, lRMTally_4, lRMDiffTally_4));
		mpRoommates.put(5, new RoommateGUI(bRM_5, lRMTally_5, lRMDiffTally_5));
		mpRoommates.put(6, new RoommateGUI(bRM_6, lRMTally_6, lRMDiffTally_6));
		mpRoommates.put(7, new RoommateGUI(bRM_7, lRMTally_7, lRMDiffTally_7));
		mpRoommates.put(8, new RoommateGUI(bRM_8, lRMTally_8, lRMDiffTally_8));
		mpRoommates.put(9, new RoommateGUI(bRM_9, lRMTally_9, lRMDiffTally_9));
		mpRoommates.put(10, new RoommateGUI(bRM_10, lRMTally_10, lRMDiffTally_10));
		mpRoommates.put(11, new RoommateGUI(bRM_11, lRMTally_11, lRMDiffTally_11));
		mpRoommates.put(12, new RoommateGUI(bRM_12, lRMTally_12, lRMDiffTally_12));
		mpRoommates.put(13, new RoommateGUI(bRM_13, lRMTally_13, lRMDiffTally_13));
		mpRoommates.put(14, new RoommateGUI(bRM_14, lRMTally_14, lRMDiffTally_14));
		mpRoommates.put(15, new RoommateGUI(bRM_15, lRMTally_15, lRMDiffTally_15));
		mpRoommates.put(16, new RoommateGUI(bRM_16, lRMTally_16, lRMDiffTally_16));
		mpRoommates.put(17, new RoommateGUI(bRM_17, lRMTally_17, lRMDiffTally_17));
		mpRoommates.put(18, new RoommateGUI(bRM_18, lRMTally_18, lRMDiffTally_18));
		mpRoommates.put(19, new RoommateGUI(bRM_19, lRMTally_19, lRMDiffTally_19));
		mpRoommates.put(20, new RoommateGUI(bRM_20, lRMTally_20, lRMDiffTally_20));
		mpRoommates.put(21, new RoommateGUI(bRM_21, lRMTally_21, lRMDiffTally_21));
		
		pQuotes = new MarqueePanel(100, 1);
		pQuotes.setWrap(true);
		pQuotes.setWrapAmount(0);
		
		pLeft.add(pQuotes, BorderLayout.SOUTH);
		
		ArrayList<String> quotes = quoteParser.getQuotes();
		frame.pack();
		
		for (String quote : quotes) {
			pQuotes.add(new JLabel(quote));
			
			// Add spacing between quotes
			pQuotes.add(Box.createRigidArea(new Dimension(pQuotes.getWidth(), 0)));
		}
				
		initializeRightPanel();
		initializeLeftPanel();	
	}
	
	/**
	 * Initialize the left panel.
	 */
	private void initializeLeftPanel() {
		
		// Get names for and add functionality to buttons
		Map<Integer, Integer> tallies = BeerHandler.getCurrentTallies();
		
		for (Map.Entry<Integer, RoommateGUI> entry : mpRoommates.entrySet())
		{
			RoommateGUI rmGui = (RoommateGUI) entry.getValue();
			JButton btn = rmGui.getTallyButton();
			JLabel tdL = rmGui.getDiffTallyLabel();
			JLabel ttL = rmGui.getTotalTallyLabel();
			int roomNumber = (int) entry.getKey();

			if (tallies.containsKey(roomNumber)) {
				ttL.setText(Integer.toString(tallies.get(roomNumber)));
			}
			
			if (names.containsKey(roomNumber)) {
				btn.setText(names.get(roomNumber));
			}
			
			btn.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (btn.isEnabled()) {
						if (!tdL.isEnabled()) {
							tdL.setEnabled(true);
							tdL.setForeground(new Color(0, 153, 0));
							tdL.setFont(tdL.getFont().deriveFont(24f));
						}
						
						if (!bApplyTally.isEnabled()) {
							bApplyTally.setEnabled(true);
							bCancelTally.setEnabled(true);
						}
		
						int currentDiff = Integer.parseInt(tdL.getText()) + 1;
						BeerHandler.addTally(roomNumber);
						tdL.setText(Integer.toString(currentDiff));		
					}
				}
			});
		}		
	}
	
	/**
	 * Initialize the right panel.
	 */
	private void initializeRightPanel() {
		pRight.setLayout(new BorderLayout(0, 0));
		
		pHistory = new ImagePane();
		pHistory.setPreferredSize(new Dimension(screen.width, (int) (screen.height*0.3)));
		pRight.add(pHistory, BorderLayout.NORTH);
		pHistory.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		pHistory.add(scrollPane, BorderLayout.CENTER);
		
		taHistory = new TransparentTextArea();
		taHistory.setEditable(false);
		taHistory.setForeground(new Color(204, 0, 0));
		taHistory.setLineWrap(true);
		scrollPane.setViewportView(taHistory);
		
		pRightButtons = new JPanel();
		pRight.add(pRightButtons, BorderLayout.CENTER);
		
		// Auto-generated with WindowBuilder
		GridBagLayout gbl_pRightButtons = new GridBagLayout();
		gbl_pRightButtons.columnWidths = new int[]{0, 0};
		gbl_pRightButtons.rowHeights = new int[]{0, 1, 0, 0};
		gbl_pRightButtons.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pRightButtons.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		pRightButtons.setLayout(gbl_pRightButtons);
		
		bApplyTally = new JButton("Apply Tallies");
		bApplyTally.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {				
				applyTallies();
			}
		});
		bApplyTally.setPreferredSize(new Dimension(300, 150));
		bApplyTally.setEnabled(false);
		GridBagConstraints gbc_bApplyTally = new GridBagConstraints();
		gbc_bApplyTally.insets = new Insets(10, 0, 5, 0);
		gbc_bApplyTally.fill = GridBagConstraints.BOTH;
		gbc_bApplyTally.gridx = 0;
		gbc_bApplyTally.gridy = 0;
		pRightButtons.add(bApplyTally, gbc_bApplyTally);
		
		JButton bButton_2 = new JButton("Placeholder");
		bButton_2.setPreferredSize(new Dimension(300, 150));
		bButton_2.setEnabled(false);
		GridBagConstraints gbc_bButton_2 = new GridBagConstraints();
		gbc_bButton_2.fill = GridBagConstraints.BOTH;
		gbc_bButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_bButton_2.gridx = 0;
		gbc_bButton_2.gridy = 1;
		pRightButtons.add(bButton_2, gbc_bButton_2);
		
		bCancelTally = new JButton("Cancel");
		bCancelTally.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				BeerHandler.resetTallies();
				resetDiff();
			}
		});
		bCancelTally.setPreferredSize(new Dimension(300, 150));
		bCancelTally.setEnabled(false);
		GridBagConstraints gbc_bCancelTally = new GridBagConstraints();
		gbc_bCancelTally.fill = GridBagConstraints.BOTH;
		gbc_bCancelTally.gridx = 0;
		gbc_bCancelTally.gridy = 2;
		pRightButtons.add(bCancelTally, gbc_bCancelTally);
	}
	
	/**
	 * Apply the tallies.
	 */
	private void applyTallies() {
		updateHistory();
		BeerHandler.applyTallies();		
		
		Map<Integer, Integer> tallies = BeerHandler.getCurrentTallies();
		
		for (Map.Entry<Integer, Integer> entry : tallies.entrySet()) {
			int roomNumber = entry.getKey();
			String tally = Integer.toString(entry.getValue());
			
			mpRoommates.get(roomNumber).getTotalTallyLabel().setText(tally);
		}
			
		resetDiff();	
	}
	
	/**
	 * Reset the temporary tally count.
	 */
	private void resetDiff() {
		bApplyTally.setEnabled(false);
		bCancelTally.setEnabled(false);
		
		for (Map.Entry<Integer, RoommateGUI> entry : mpRoommates.entrySet()) {
			RoommateGUI rmGui = entry.getValue();
			JLabel tdL = rmGui.getDiffTallyLabel();
			
			tdL.setText("0");
			tdL.setForeground(Color.BLACK);
			tdL.setEnabled(false);
			tdL.setFont(tdL.getFont().deriveFont(12f));	
		}	
	}
	
	/**
	 * Update the history log.
	 */
	private void updateHistory() {
		Map<Integer, Integer> tallies = BeerHandler.getTempTally();
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String time = dateFormat.format(new Date());

		for (Map.Entry<Integer, Integer> entry : tallies.entrySet()) {
			String text = time + ": ";
			if (entry.getValue() == 1) {
				text += names.get(entry.getKey()) + " heeft 1 biertje geturfd.\n";
			}
			else {
				text += names.get(entry.getKey()) + " heeft " + entry.getValue();
				text += " biertjes geturfd.\n";
			}
			taHistory.append(text);
			if (entry.getValue() == 7) {
				taHistory.append("Waarvoor hulde!\n");
			}
		}
	}
}
