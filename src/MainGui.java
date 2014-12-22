/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Main GUI class, handles most of the GUI aspects of the application.
 */

import java.awt.*;

import javax.swing.*;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class MainGui implements ActionListener {
	private JFrame frame;
	
	/**
	 * Main split panel
	 */
	private JSplitPane spMain;
	
	/**
	 * Tally button panel, containing the roommates and tally count.
	 */
	private JPanel pTallyButtons;
	
	/**
	 * Card manager panel, handles the visibility of the panels.
	 */
	private JPanel pCardManager;
	
	/**
	 * Card layout, used to switch between panels
	 */
	private CardLayout cardLayout;
	
	/**
	 * Password panel
	 */
	private JPanel pPassword;
	
	/**
	 * Password field, self-explanatory.
	 */
	private JPasswordField passwordField;
	
	/**
	 * Quotes panel, scrolling text from right to left
	 */	
	private MarqueePanel pQuotes;
	
	/**
	 * Left panel, containing the tally buttons and scrolling text.
	 */
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
	 * Generic confirmation panel.
	 */
	private JPanel pConfirmation;
	
	/**
	 * Generic confirmation header.
	 */
	private JLabel lConfirmationHeader;
	
	/**
	 * Generic confirmation sub header.
	 */
	private JLabel lConfirmationSub;
	
	/**
	 * Warning panel for removing a temporary group.
	 */
	private JPanel pRemoveTTWarning;
	
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
	
	/**
	 * Active room mates, names by room number
	 */
	private Map<Integer, String> names;
	
	/**
	 * Active room mates, roommate id by room number
	 */
	private Map<Integer, Integer> roommateIds;
	
	/**
	 * New list confirmation button
	 */
	private JButton bNewListYes;
	
	/**
	 * Remove temporary tally group buttons
	 */
	private JButton bRemoveTempTally_1;
	private JButton bRemoveTempTally_2;
	private JButton bRemoveTempTally_3;
	
	/**
	 * 'Room number' of the temporary tally group to be removed
	 */
	private int roomNumberRemoveTempTally;
	
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
		
		// Set up card layout
		pCardManager = new JPanel();
		pCardManager.setBounds(0, 0, screen.width, screen.height);
		frame.getContentPane().add(pCardManager);
		pCardManager.setLayout(new CardLayout(0, 0));
		cardLayout = (CardLayout)(pCardManager.getLayout());
			
		// Split the pane in two sides, left the buttons, right the history log and apply button
		spMain = new JSplitPane();
		pCardManager.add(spMain, "main");
		
		// Divider settings
		spMain.setEnabled(false);
		spMain.setDividerLocation((int) (screen.width * 0.66));
		spMain.setDividerSize(0);
		
		// Create and initialize generic confirmation panel
		initializeConfirmationPanel();
		
		// Create and initialize right panel
		initializeRightPanel();
		
		// Create and initialize left panel	
		initializeLeftPanel();	
								
		// Create and initialize password panel
		initializePassPanel();		
		
		// Create and initialize options panel
		initializeOptionsPanel();
		
		// Create and initialize new list panel
		initializeNewListPanel();
		
		// Create and initialize password change panel
		initializeNewPassPanel();
		
		// Create and initialize temporary tally panel
		initializeTempTallyPanel();
	}
	
	/**
	 * Initialize the confirmation panel.
	 */
	private void initializeConfirmationPanel() {
		pConfirmation = new JPanel();
		GridBagLayout gbl_pConfirmation = new GridBagLayout();
		pConfirmation.setLayout(gbl_pConfirmation);
		pCardManager.add(pConfirmation, "confirmation");
		
		lConfirmationHeader = new JLabel("");
		lConfirmationHeader.setFont(lConfirmationHeader.getFont().deriveFont(24f));
		GridBagConstraints gbc_lConfirmationHeader = new GridBagConstraints();
		gbc_lConfirmationHeader.gridx = 0;
		gbc_lConfirmationHeader.gridy = 0;
		pConfirmation.add(lConfirmationHeader, gbc_lConfirmationHeader);
		
		lConfirmationSub = new JLabel("");
		lConfirmationSub.setFont(lConfirmationSub.getFont().deriveFont(18f));
		GridBagConstraints gbc_lConfirmationSub = new GridBagConstraints();
		gbc_lConfirmationSub.gridx = 0;
		gbc_lConfirmationSub.gridy = 1;
		pConfirmation.add(lConfirmationSub, gbc_lConfirmationSub);
		
		JButton bConfirmationBack = new JButton("Terug");
		bConfirmationBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "main");
			}
		});
		bConfirmationBack.setPreferredSize(new Dimension(300, 100));
		GridBagConstraints gbc_bConfirmationBack = new GridBagConstraints();
		gbc_bConfirmationBack.insets = new Insets(10, 0, 0, 0);
		gbc_bConfirmationBack.gridx = 0;
		gbc_bConfirmationBack.gridy = 2;
		pConfirmation.add(bConfirmationBack, gbc_bConfirmationBack);
	}
	
	/**
	 * Initialize the left panel.
	 */
	private void initializeLeftPanel() {
		pLeft = new JPanel();
		spMain.setLeftComponent(pLeft);
		pLeft.setLayout(new BorderLayout(0, 0));
		
		pQuotes = new MarqueePanel(100, 1);
		pQuotes.setWrap(true);
		pQuotes.setWrapAmount(0);
		
		pLeft.add(pQuotes, BorderLayout.SOUTH);
		
		ArrayList<String> quotes = QuoteParser.getQuotes();
		frame.pack();
		
		for (String quote : quotes) {
			pQuotes.add(new JLabel(quote));
			
			// Add spacing between quotes
			pQuotes.add(Box.createRigidArea(new Dimension(pQuotes.getWidth(), 0)));
		}
		
		initializeTallyPanel();
	}
	
	/**
	 * Initialize the right panel.
	 */
	private void initializeRightPanel() {
		pRight = new JPanel();
		pRight.setLayout(new BorderLayout(0, 0));	
		spMain.setRightComponent(pRight);	
		
		pHistory = new ImagePane();
		pHistory.setLayout(new BorderLayout(0, 0));
		pRight.add(pHistory, BorderLayout.NORTH);
		
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
		
		bApplyTally = new JButton("Turfen");
		bApplyTally.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		
		JButton bOptions = new JButton("Opties");				
		bOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "password");
			}
		});
		
		bOptions.setPreferredSize(new Dimension(300, 150));
		GridBagConstraints gbc_bOptions = new GridBagConstraints();
		gbc_bOptions.fill = GridBagConstraints.BOTH;
		gbc_bOptions.insets = new Insets(0, 0, 5, 0);
		gbc_bOptions.gridx = 0;
		gbc_bOptions.gridy = 1;
		pRightButtons.add(bOptions, gbc_bOptions);
		
		bCancelTally = new JButton("Annuleren");
		bCancelTally.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
	 * Initialize tally panel
	 */
	private void initializeTallyPanel() {
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
		
		// Get names for and add functionality to buttons
		refresh();
		
		for (Map.Entry<Integer, RoommateGUI> entry : mpRoommates.entrySet()) {;
			RoommateGUI rmGui = entry.getValue();
			JButton btn = rmGui.getTallyButton();
			JLabel tdL = rmGui.getDiffTallyLabel();
			JLabel ttL = rmGui.getTotalTallyLabel();
			int roomNumber = entry.getKey();
			
			// Prevents the labels from fucking up the layout.
			tdL.setPreferredSize(new Dimension(40, 20));
			ttL.setPreferredSize(new Dimension(40, 20));
			
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
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
			});
		}		
	}
	
	/**
	 * Initialize password panel
	 */
	private void initializePassPanel() {
		pPassword = new JPanel();
		pCardManager.add(pPassword, "password");
		
		JLabel lWrongPass = new JLabel("Onjuist wachtwoord!");
		lWrongPass.setForeground(Color.RED);
		lWrongPass.setVisible(false);
		GridBagConstraints gbc_lWrongPass = new GridBagConstraints();
		gbc_lWrongPass.insets = new Insets(10, 0, 0, 0);
		gbc_lWrongPass.gridx = 1;
		gbc_lWrongPass.gridy = 2;
		
		
		GridBagLayout gbl_pPassword = new GridBagLayout();
		pPassword.setLayout(gbl_pPassword);
		
		JLabel lPassword = new JLabel("Wachtwoord");
		GridBagConstraints gbc_lPassword = new GridBagConstraints();
		gbc_lPassword.insets = new Insets(0, 0, 10, 5);
		pPassword.add(lPassword, gbc_lPassword);
		
		passwordField = new JPasswordField(10);
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (Security.checkPassword(passwordField.getPassword())) {			
						cardLayout.show(pCardManager, "options");
					}
					else {
						passwordField.requestFocus();
						lWrongPass.setVisible(true);
					}
					
					passwordField.setText("");
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cardLayout.show(pCardManager, "main");
				}
			}
		});
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 10, 0);
		pPassword.add(passwordField, gbc_passwordField);
		
		JButton bCancelPass = new JButton("Terug");
		bCancelPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "main");
			}
		});
		GridBagConstraints gbc_bCancelPass = new GridBagConstraints();
		gbc_bCancelPass.insets = new Insets(0, 0, 0, 5);
		gbc_bCancelPass.gridx = 0;
		gbc_bCancelPass.gridy = 1;
		pPassword.add(bCancelPass, gbc_bCancelPass);
		
		JButton bEnterPass = new JButton("OK");
		bEnterPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Security.checkPassword(passwordField.getPassword())) {			
					cardLayout.show(pCardManager, "options");					
				}
				else {
					passwordField.requestFocus();
					lWrongPass.setVisible(true);
				}
				
				passwordField.setText("");
			}
		});
		GridBagConstraints gbc_bEnterPass = new GridBagConstraints();
		gbc_bEnterPass.gridx = 1;
		gbc_bEnterPass.gridy = 1;
		pPassword.add(bEnterPass, gbc_bEnterPass);
		
		pPassword.add(lWrongPass, gbc_lWrongPass);
		
		pPassword.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				lWrongPass.setVisible(false);
				passwordField.requestFocus();
				passwordField.setText("");
			}
		});
	}
	
	/**
	 * Initialize options panel
	 */
	private void initializeOptionsPanel() {
		JPanel pOptions = new JPanel();
		pCardManager.add(pOptions, "options");
		GridBagLayout gbl_pOptions = new GridBagLayout();
		pOptions.setLayout(gbl_pOptions);
		
		JButton bNewList = new JButton("Nieuwe lijst maken");
		bNewList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "newList");
			}
		});
		bNewList.setPreferredSize(new Dimension(250, 100));
		GridBagConstraints gbc_bNewList = new GridBagConstraints();
		gbc_bNewList.fill = GridBagConstraints.HORIZONTAL;
		gbc_bNewList.anchor = GridBagConstraints.NORTH;
		gbc_bNewList.insets = new Insets(0, 0, 10, 0);
		gbc_bNewList.gridx = 0;
		gbc_bNewList.gridy = 0;
		pOptions.add(bNewList, gbc_bNewList);
		
		JButton bNewRoommate = new JButton("Nieuwe huisgenoot");
		bNewRoommate.setPreferredSize(new Dimension(250, 100));
		GridBagConstraints gbc_bNewRoommate = new GridBagConstraints();
		gbc_bNewRoommate.fill = GridBagConstraints.HORIZONTAL;
		gbc_bNewRoommate.anchor = GridBagConstraints.NORTH;
		gbc_bNewRoommate.insets = new Insets(0, 0, 10, 0);
		gbc_bNewRoommate.gridx = 0;
		gbc_bNewRoommate.gridy = 1;
		pOptions.add(bNewRoommate, gbc_bNewRoommate);
		
		JButton bInternMove = new JButton("Interne verhuizing");
		bInternMove.setPreferredSize(new Dimension(250, 100));
		GridBagConstraints gbc_bInternMove = new GridBagConstraints();
		gbc_bInternMove.fill = GridBagConstraints.HORIZONTAL;
		gbc_bInternMove.insets = new Insets(0, 0, 10, 0);
		gbc_bInternMove.gridx = 0;
		gbc_bInternMove.gridy = 2;
		pOptions.add(bInternMove, gbc_bInternMove);
		
		JButton bTempTally = new JButton("Extra turfers");
		bTempTally.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "tempTally");
			}
		});
		bTempTally.setPreferredSize(new Dimension(250, 100));
		GridBagConstraints gbc_bTempTally = new GridBagConstraints();
		gbc_bTempTally.fill = GridBagConstraints.HORIZONTAL;
		gbc_bTempTally.insets = new Insets(0, 0, 10, 0);
		gbc_bTempTally.gridx = 0;
		gbc_bTempTally.gridy = 3;
		pOptions.add(bTempTally, gbc_bTempTally);
				
		JButton bNewPassword = new JButton("Nieuw wachtwoord");
		bNewPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "newPass");
			}
		});	
		bNewPassword.setPreferredSize(new Dimension(250, 100));
		GridBagConstraints gbc_bNewPassword = new GridBagConstraints();
		gbc_bNewPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_bNewPassword.insets = new Insets(0, 0, 10, 0);
		gbc_bNewPassword.gridx = 0;
		gbc_bNewPassword.gridy = 4;
		pOptions.add(bNewPassword, gbc_bNewPassword);
		
		JButton bOptionsBack = new JButton("Terug");
		bOptionsBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "main");
			}
		});		
		bOptionsBack.setPreferredSize(new Dimension(250, 100));
		GridBagConstraints gbc_bOptionsBack = new GridBagConstraints();
		gbc_bOptionsBack.fill = GridBagConstraints.HORIZONTAL;
		gbc_bOptionsBack.insets = new Insets(0, 0, 10, 0);
		gbc_bOptionsBack.gridx = 0;
		gbc_bOptionsBack.gridy = 5;
		pOptions.add(bOptionsBack, gbc_bOptionsBack);	
	}

	/**
	 * Initialize new list panel
	 */
	private void initializeNewListPanel() {
		JPanel pNewList = new JPanel();
		pCardManager.add(pNewList, "newList");
		GridBagLayout gbl_pNewList = new GridBagLayout();
		pNewList.setLayout(gbl_pNewList);
		
		JLabel lNewListHeader = new JLabel("Dit gaat een nieuwe lijst creëeren, waardoor de vorige "
				+ "lijst zal sluiten!");
		lNewListHeader.setFont(lNewListHeader.getFont().deriveFont(24f));
		GridBagConstraints gbc_lNewListHeader = new GridBagConstraints();
		gbc_lNewListHeader.gridx = 0;
		gbc_lNewListHeader.gridy = 0;
		pNewList.add(lNewListHeader, gbc_lNewListHeader);
		
		JLabel lNewListSub = new JLabel("Weet je het zeker?");
		lNewListSub.setFont(lNewListSub.getFont().deriveFont(18f));
		GridBagConstraints gbc_lNewListSub = new GridBagConstraints();
		gbc_lNewListSub.gridx = 0;
		gbc_lNewListSub.gridy = 1;
		pNewList.add(lNewListSub, gbc_lNewListSub);
		
		JPanel pNewListConfirm = new JPanel();
		pNewListConfirm.setLayout(new FlowLayout(FlowLayout.LEADING, 30, 30));
		GridBagConstraints gbc_pNewListConfirm = new GridBagConstraints();
		gbc_pNewListConfirm.gridx = 0;
		gbc_pNewListConfirm.gridy = 2;
		pNewList.add(pNewListConfirm, gbc_pNewListConfirm);

		bNewListYes = new JButton("Ja");
		bNewListYes.addActionListener(this);
		bNewListYes.setPreferredSize(new Dimension(300, 100));
		
		JButton bNewListNo = new JButton("Nee");
		bNewListNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "options");
			}
		});
		bNewListNo.setPreferredSize(new Dimension(300, 100));
		
		pNewListConfirm.add(bNewListYes);
		pNewListConfirm.add(bNewListNo);
	}
	
	/**
	 * Initialize new password panel
	 */
	private void initializeNewPassPanel() {
		JPanel pNewPass = new JPanel();
		pCardManager.add(pNewPass, "newPass");
		GridBagLayout gbl_pNewPass = new GridBagLayout();
		pNewPass.setLayout(gbl_pNewPass);
		
		JPanel pNewPassConfirm = new JPanel();
		GridBagLayout gbl_pNewPassConfirm = new GridBagLayout();
		pNewPassConfirm.setLayout(gbl_pNewPassConfirm);
		pCardManager.add(pNewPassConfirm, "newPassConfirm");
		
		JPanel pNewPassSub = new JPanel();
		GridBagLayout gbl_pNewPassSub = new GridBagLayout();
		pNewPassSub.setLayout(gbl_pNewPassSub);
		GridBagConstraints gbc_pNewPassSub = new GridBagConstraints();
		gbc_pNewPassSub.gridx = 0;
		gbc_pNewPassSub.gridy = 0;
		pNewPass.add(pNewPassSub, gbc_pNewPassSub);
				
		JLabel lNewPass = new JLabel("Nieuw wachtwoord");
		GridBagConstraints gbc_lNewPass = new GridBagConstraints();
		gbc_lNewPass.anchor = GridBagConstraints.EAST;
		gbc_lNewPass.insets = new Insets(0, 0, 5, 10);
		gbc_lNewPass.gridx = 0;
		gbc_lNewPass.gridy = 0;
		pNewPassSub.add(lNewPass, gbc_lNewPass);
		
		JPasswordField newPass = new JPasswordField(10);
		JPasswordField newPassConfirm = new JPasswordField(10);
		
		JLabel lNotMatching = new JLabel("Wachtwoorden zijn niet gelijk aan elkaar!");
		lNotMatching.setForeground(Color.RED);
		lNotMatching.setVisible(false);
		JLabel lInvalidSize = new JLabel("Wachtwoord moet tussen de 4 en 16 karakters zijn!");
		lInvalidSize.setForeground(Color.RED);
		lInvalidSize.setVisible(false);
		
		newPass.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cardLayout.show(pCardManager, "options");
				}
			}
		});
		
		GridBagConstraints gbc_newPass = new GridBagConstraints();
		gbc_newPass.insets = new Insets(0, 0, 5, 0);
		gbc_newPass.gridx = 1;
		gbc_newPass.gridy = 0;
		pNewPassSub.add(newPass, gbc_newPass);
		
		JLabel lNewPassConfirm = new JLabel("Herhaal nieuw wachtwoord");
		GridBagConstraints gbc_lNewPassConfirm = new GridBagConstraints();
		gbc_lNewPassConfirm.anchor = GridBagConstraints.EAST;
		gbc_lNewPassConfirm.insets = new Insets(0, 0, 5, 10);
		gbc_lNewPassConfirm.gridx = 0;
		gbc_lNewPassConfirm.gridy = 1;
		pNewPassSub.add(lNewPassConfirm, gbc_lNewPassConfirm);
			
		newPassConfirm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {	
					lInvalidSize.setVisible(false);
					lNotMatching.setVisible(false);
					
					if (Arrays.equals(newPass.getPassword(), newPassConfirm.getPassword())) {
						if(newPass.getPassword().length > 4 && newPass.getPassword().length < 16 ) {
							Security.newPassword(newPass.getPassword());
							
							String confirmationHeader = "Het nieuwe wachtwoord is aangemaakt!";
							String confirmationSub = "";
							confirmationPanel(confirmationHeader, confirmationSub);
						}
						else {
							lInvalidSize.setVisible(true);
						}
					}
					else {
						lNotMatching.setVisible(true);
					}

					newPass.requestFocus();
					newPass.setText("");
					newPassConfirm.setText("");
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cardLayout.show(pCardManager, "options");
				}
			}
		});
		GridBagConstraints gbc_newPassConfirm = new GridBagConstraints();
		gbc_newPassConfirm.insets = new Insets(0, 0, 5, 0);
		gbc_newPassConfirm.gridx = 1;
		gbc_newPassConfirm.gridy = 1;
		pNewPassSub.add(newPassConfirm, gbc_newPassConfirm);
		
		JButton bCancelNewPass = new JButton("Terug");
		bCancelNewPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lInvalidSize.setVisible(false);
				lNotMatching.setVisible(false);
				cardLayout.show(pCardManager, "options");
			}
		});
		GridBagConstraints gbc_bCancelNewPass = new GridBagConstraints();
		gbc_bCancelNewPass.insets = new Insets(0, 0, 0, 5);
		gbc_bCancelNewPass.gridx = 0;
		gbc_bCancelNewPass.gridy = 2;
		pNewPassSub.add(bCancelNewPass, gbc_bCancelNewPass);
		
		JButton bEnterNewPass = new JButton("OK");
		bEnterNewPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lInvalidSize.setVisible(false);
				lNotMatching.setVisible(false);
				if (Arrays.equals(newPass.getPassword(), newPassConfirm.getPassword())) {
					if(newPass.getPassword().length > 4 && newPass.getPassword().length < 16 ) {
						Security.newPassword(newPass.getPassword());
					}
					else {
						lInvalidSize.setVisible(true);
					}
				}
				else {
					lNotMatching.setVisible(true);
				}

				newPass.requestFocus();
				newPass.setText("");
				newPassConfirm.setText("");
			}
		});
		GridBagConstraints gbc_bEnterNewPass = new GridBagConstraints();
		gbc_bEnterNewPass.gridx = 1;
		gbc_bEnterNewPass.gridy = 2;
		pNewPassSub.add(bEnterNewPass, gbc_bEnterNewPass);
		
		
		GridBagConstraints gbc_lNotMatching = new GridBagConstraints();
		gbc_lNotMatching.insets = new Insets(0, 0, 5, 0);
		gbc_lNotMatching.gridx = 0;
		gbc_lNotMatching.gridy = 1;
		pNewPass.add(lNotMatching, gbc_lNotMatching);
		
		GridBagConstraints gbc_lInvalidSize = new GridBagConstraints();
		gbc_lInvalidSize.insets = new Insets(0, 0, 5, 0);
		gbc_lInvalidSize.gridx = 0;
		gbc_lInvalidSize.gridy = 2;
		pNewPass.add(lInvalidSize, gbc_lInvalidSize);
		
		pNewPass.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				newPass.requestFocus();
				lInvalidSize.setVisible(false);
				lNotMatching.setVisible(false);
				newPass.setText("");
				newPassConfirm.setText("");
			}
		});
	}
	
	/**
	 * Initialize temporary tally panel
	 */
	private void initializeTempTallyPanel() {
		JPanel pTempTally = new JPanel();
		GridBagLayout gbl_pTempTally = new GridBagLayout();
		pTempTally.setLayout(gbl_pTempTally);
		pCardManager.add(pTempTally, "tempTally");
		
		JButton bNewTempTally = new JButton("Nieuw extra turfgroep");
		bNewTempTally.setEnabled(false);
		bNewTempTally.setPreferredSize(new Dimension(250, 100));
		bNewTempTally.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "newTempTally");
			}
		});	
		GridBagConstraints gbc_bNewTempTally = new GridBagConstraints();
		gbc_bNewTempTally.fill = GridBagConstraints.HORIZONTAL;
		gbc_bNewTempTally.insets = new Insets(0, 0, 10, 0);
		gbc_bNewTempTally.gridx = 0;
		gbc_bNewTempTally.gridy = 0;
		pTempTally.add(bNewTempTally, gbc_bNewTempTally);
		
		JButton bRemoveTempTally = new JButton("Sluit een extra turfgroep");
		bRemoveTempTally.setEnabled(false);
		bRemoveTempTally.setPreferredSize(new Dimension(250, 100));
		bRemoveTempTally.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "removeTempTally");
			}
		});	
		GridBagConstraints gbc_bRemoveTempTally = new GridBagConstraints();
		gbc_bRemoveTempTally.fill = GridBagConstraints.HORIZONTAL;
		gbc_bRemoveTempTally.insets = new Insets(0, 0, 10, 0);
		gbc_bRemoveTempTally.gridx = 0;
		gbc_bRemoveTempTally.gridy = 1;
		pTempTally.add(bRemoveTempTally, gbc_bRemoveTempTally);
		
		JButton bTempTallyBack = new JButton("Terug");
		bTempTallyBack.setPreferredSize(new Dimension(250, 100));
		bTempTallyBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "options");
			}
		});	
		GridBagConstraints gbc_bTempTallyBack = new GridBagConstraints();
		gbc_bTempTallyBack.fill = GridBagConstraints.HORIZONTAL;
		gbc_bTempTallyBack.insets = new Insets(0, 0, 10, 0);
		gbc_bTempTallyBack.gridx = 0;
		gbc_bTempTallyBack.gridy = 2;
		pTempTally.add(bTempTallyBack, gbc_bTempTallyBack);
		
		pTempTally.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				if (names.containsKey(19) || names.containsKey(20) || names.containsKey(21)) {
					bRemoveTempTally.setEnabled(true);
				}
				else {
					bRemoveTempTally.setEnabled(false);
				}
				
				if (!(names.containsKey(19) && names.containsKey(20) && names.containsKey(21))) {
					bNewTempTally.setEnabled(true);
				}
				else {
					bNewTempTally.setEnabled(false);
				}
			}
		});
			
		
		// New temporary tally panel
		JPanel pNewTempTally = new JPanel();
		pNewTempTally.setLayout(new GridBagLayout());
		pCardManager.add(pNewTempTally, "newTempTally");

		JLabel lInvalidName = new JLabel("Ongeldige naam!");
		lInvalidName.setForeground(Color.RED);
		lInvalidName.setVisible(false);
		
		JLabel lNewTempTallyHeader = new JLabel("Geef een naam op");
		lNewTempTallyHeader.setFont(lNewTempTallyHeader.getFont().deriveFont(24f));	
		GridBagConstraints gbc_lNewTempTallyHeader = new GridBagConstraints();
		gbc_lNewTempTallyHeader.insets = new Insets(0, 0, 10, 0);
		gbc_lNewTempTallyHeader.gridx = 0;
		gbc_lNewTempTallyHeader.gridy = 0;
		pNewTempTally.add(lNewTempTallyHeader, gbc_lNewTempTallyHeader);
		
		JTextField tNewTempTally = new JTextField(16);
		tNewTempTally.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String name = tNewTempTally.getText();
					
					if (Security.isValid(name)) {	
						addNewTempTally(name);
					}
					else {
						lInvalidName.setVisible(true);
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cardLayout.show(pCardManager, "tempTally");
				}
			}
		});
		GridBagConstraints gbc_tNewTempTally = new GridBagConstraints();
		gbc_tNewTempTally.gridx = 0;
		gbc_tNewTempTally.gridy = 1;
		pNewTempTally.add(tNewTempTally, gbc_tNewTempTally);
		
		JPanel pNewTempTallyButtons = new JPanel();
		pNewTempTallyButtons.setLayout(new FlowLayout(FlowLayout.LEADING, 30, 30));
		GridBagConstraints gbc_pNewTempTallyButtons = new GridBagConstraints();
		gbc_pNewTempTallyButtons.gridx = 0;
		gbc_pNewTempTallyButtons.gridy = 2;
		pNewTempTally.add(pNewTempTallyButtons, gbc_pNewTempTallyButtons);
		
		JButton bNewTempTallyBack = new JButton("Terug");
		bNewTempTallyBack.setPreferredSize(new Dimension(150, 50));
		bNewTempTallyBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "tempTally");
			}
		});			
		JButton bNewTempTallyOk = new JButton("Ok");
		bNewTempTallyOk.setPreferredSize(new Dimension(150, 50));
		bNewTempTallyOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = tNewTempTally.getText();

				if (Security.isValid(name)) {	
					addNewTempTally(name);
				}
				else {
					lInvalidName.setVisible(true);
				}		
			}
		});
		
		pNewTempTallyButtons.add(bNewTempTallyBack);
		pNewTempTallyButtons.add(bNewTempTallyOk);
		
		GridBagConstraints gbc_lInvalidName = new GridBagConstraints();
		gbc_lInvalidName.gridx = 0;
		gbc_lInvalidName.gridy = 3;
		pNewTempTally.add(lInvalidName, gbc_lInvalidName);
		
		pNewTempTally.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				lInvalidName.setVisible(false);
				tNewTempTally.requestFocus();
				tNewTempTally.setText("");
			}
		});
		
		// Remove temporary tally panel
		JPanel pRemoveTempTally = new JPanel();
		pRemoveTempTally.setLayout(new GridBagLayout());
		pCardManager.add(pRemoveTempTally, "removeTempTally");
		
		JLabel lRemoveTempTallyHeader = new JLabel("Kies een groep");
		lRemoveTempTallyHeader.setFont(lRemoveTempTallyHeader.getFont().deriveFont(24f));
		GridBagConstraints gbc_lRemoveTempTallyHeader = new GridBagConstraints();
		gbc_lRemoveTempTallyHeader.insets = new Insets(0, 0, 10, 0);
		gbc_lRemoveTempTallyHeader.gridx = 1;
		gbc_lRemoveTempTallyHeader.gridy = 0;
		pRemoveTempTally.add(lRemoveTempTallyHeader, gbc_lRemoveTempTallyHeader);
		
		bRemoveTempTally_1 = new JButton();
		bRemoveTempTally_1.addActionListener(this);
		bRemoveTempTally_1.setPreferredSize(new Dimension(200, 100));
		bRemoveTempTally_1.setEnabled(false);
		GridBagConstraints gbc_bRemoveTempTally_1 = new GridBagConstraints();
		gbc_bRemoveTempTally_1.insets = new Insets(0, 0, 5, 5);
		gbc_bRemoveTempTally_1.gridx = 0;
		gbc_bRemoveTempTally_1.gridy = 1;
		pRemoveTempTally.add(bRemoveTempTally_1, gbc_bRemoveTempTally_1);
		
		bRemoveTempTally_2 = new JButton();
		bRemoveTempTally_2.addActionListener(this);
		bRemoveTempTally_2.setPreferredSize(new Dimension(200, 100));
		bRemoveTempTally_2.setEnabled(false);
		GridBagConstraints gbc_bRemoveTempTally_2 = new GridBagConstraints();
		gbc_bRemoveTempTally_2.insets = new Insets(0, 0, 5, 5);
		gbc_bRemoveTempTally_2.gridx = 1;
		gbc_bRemoveTempTally_2.gridy = 1;
		pRemoveTempTally.add(bRemoveTempTally_2, gbc_bRemoveTempTally_2);
		
		bRemoveTempTally_3 = new JButton();
		bRemoveTempTally_3.addActionListener(this);
		bRemoveTempTally_3.setPreferredSize(new Dimension(200, 100));
		bRemoveTempTally_3.setEnabled(false);
		GridBagConstraints gbc_bRemoveTempTally_3 = new GridBagConstraints();
		gbc_bRemoveTempTally_3.insets = new Insets(0, 0, 5, 0);
		gbc_bRemoveTempTally_3.gridx = 2;
		gbc_bRemoveTempTally_3.gridy = 1;
		pRemoveTempTally.add(bRemoveTempTally_3, gbc_bRemoveTempTally_3);
		
		JButton bRemoveTempTallyBack = new JButton("Terug");
		bRemoveTempTallyBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "tempTally");
			}			
		});
		bRemoveTempTallyBack.setPreferredSize(new Dimension(200, 100));
		GridBagConstraints gbc_bRemoveTempTallyBack = new GridBagConstraints();
		gbc_bRemoveTempTallyBack.insets = new Insets(0, 0, 5, 0);
		gbc_bRemoveTempTallyBack.gridx = 1;
		gbc_bRemoveTempTallyBack.gridy = 2;
		pRemoveTempTally.add(bRemoveTempTallyBack, gbc_bRemoveTempTallyBack);	
		
		pRemoveTempTally.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				if (names.containsKey(19)) {
					bRemoveTempTally_1.setEnabled(true);
					bRemoveTempTally_1.setText(names.get(19));
				}
				else {
					bRemoveTempTally_1.setEnabled(false);
					bRemoveTempTally_1.setText("1");
				}
				
				if (names.containsKey(20)) {
					bRemoveTempTally_2.setEnabled(true);
					bRemoveTempTally_2.setText(names.get(20));
				}
				else {
					bRemoveTempTally_2.setEnabled(false);
					bRemoveTempTally_2.setText("2");
				}
				
				if (names.containsKey(21)) {
					bRemoveTempTally_3.setEnabled(true);
					bRemoveTempTally_3.setText(names.get(21));
				}
				else {
					bRemoveTempTally_3.setEnabled(false);
					bRemoveTempTally_3.setText("3");
				}
			}
		});
		
		// Remove temp tally warning panel
		pRemoveTTWarning = new JPanel();
		pCardManager.add(pRemoveTTWarning, "removeTempTallyWarning");
		pRemoveTTWarning.setLayout(new GridBagLayout());	
	}
	
	private void removeTempTallyWarningPanel() {
		pRemoveTTWarning.removeAll();
		String name = names.get(roomNumberRemoveTempTally);
		
		JLabel lHeader = new JLabel("Dit zal de groep '" + name + "' verwijderen!");
		lHeader.setFont(lHeader.getFont().deriveFont(24f));
		GridBagConstraints gbc_lHeader = new GridBagConstraints();
		gbc_lHeader.gridx = 0;
		gbc_lHeader.gridy = 0;
		pRemoveTTWarning.add(lHeader, gbc_lHeader);
		
		JLabel lSub = new JLabel("Weet je het zeker?");
		lSub.setFont(lSub.getFont().deriveFont(18f));
		GridBagConstraints gbc_lSub = new GridBagConstraints();
		gbc_lSub.gridx = 0;
		gbc_lSub.gridy = 1;
		pRemoveTTWarning.add(lSub, gbc_lSub);
		
		JPanel pConfirm = new JPanel();
		pConfirm.setLayout(new FlowLayout(FlowLayout.LEADING, 30, 30));
		GridBagConstraints gbc_pConfirm = new GridBagConstraints();
		gbc_pConfirm.gridx = 0;
		gbc_pConfirm.gridy = 2;
		pRemoveTTWarning.add(pConfirm, gbc_pConfirm);

		JButton bYes = new JButton("Ja");
		bYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = names.get(roomNumberRemoveTempTally);
				RoommateHandler.removeTempTally(roomNumberRemoveTempTally);
				refresh();			
				String header = "De groep '" + name + "' is verwijderd!";
				
				confirmationPanel(header, "");
			}
		});
		bYes.setPreferredSize(new Dimension(300, 100));
		
		JButton bNo = new JButton("Nee");
		bNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pCardManager, "removeTempTally");
			}
		});
		bNo.setPreferredSize(new Dimension(300, 100));
		
		pConfirm.add(bYes);
		pConfirm.add(bNo);
		
		cardLayout.show(pCardManager, "removeTempTallyWarning");
	}
	
	private void addNewTempTally(String name) {
		int roomNumber = RoommateHandler.addTempTally(name);
		refresh();
		
		String header = "Een nieuwe turfgroep is toegevoegd!";
		String sub = "'" + name + "' is toegevoegd op plaats plaats " + roomNumber;
		
		confirmationPanel(header, sub);
	}

	
	/**
	 * Populates the confirmation panel.
	 * @param header	The header
	 * @param sub		The sub header
	 */
	private void confirmationPanel(String header, String sub) {
		lConfirmationHeader.setText(header);
		if (sub.equals("")) {
			lConfirmationSub.setVisible(false);
		}
		else {
			lConfirmationSub.setVisible(true);
			lConfirmationSub.setText(sub);
		}
		cardLayout.show(pCardManager, "confirmation");
	}
	
	/**
	 * Apply the tallies.
	 */
	private void applyTallies() {
		updateHistory();
		BeerHandler.applyTallies();		
		refresh();	
	}
	
	/**
	 * Reset the temporary tally count.
	 */
	private void resetDiff() {
		BeerHandler.resetTallies();
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
				taHistory.append("                       Waarvoor hulde!\n");
			}
		}
		taHistory.append("---------------------------------------------------------\n");
	}
	
	private void refresh() {
		RoommateHandler.refresh();
		resetDiff();
		names = RoommateHandler.getNames();
		roommateIds = RoommateHandler.getRoommateIds();
		
		Map<Integer, Integer> tallies = BeerHandler.getCurrentTallies();
		for (Map.Entry<Integer, RoommateGUI> entry : mpRoommates.entrySet()) {
			int roomNumber = entry.getKey();
			RoommateGUI rmGui = entry.getValue();
			JButton btn = rmGui.getTallyButton();
			JLabel ttL = rmGui.getTotalTallyLabel();
			
			if (roommateIds.containsKey(roomNumber)) {
				int roommateId = roommateIds.get(roomNumber);
				
				if (tallies.containsKey(roommateId)) {
					ttL.setText(Integer.toString(tallies.get(roommateId)));
				}
				else {
					ttL.setText("0");
				}
				
				if (names.containsKey(roomNumber)) {
					btn.setText(names.get(roomNumber));
					btn.setEnabled(true);
				}
			}
			else {
				btn.setText(Integer.toString(roomNumber));
				btn.setEnabled(false);
				ttL.setText("0");
			}
			
			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// New list confirmation button
		if (e.getSource() == bNewListYes) {
			String fileName = ExcelHandler.writeToExcelFile();
			
			// Close temporary tally groups
			RoommateHandler.removeTempTally(19);
			RoommateHandler.removeTempTally(20);
			RoommateHandler.removeTempTally(21);
			
			BeerHandler.newList();

			refresh();			
			
			String successHeader = "Een nieuwe lijst is gecreëerd!";
			String successSub = "Een Excel bestand is aangemaakt als " + fileName;

			confirmationPanel(successHeader, successSub);
		}
		// Remove temporary tally group buttons
		else if (e.getSource() == bRemoveTempTally_1) {
			roomNumberRemoveTempTally = 19;
			removeTempTallyWarningPanel();
		}
		else if (e.getSource() == bRemoveTempTally_2) {
			roomNumberRemoveTempTally = 20;
			removeTempTallyWarningPanel();
		}
		else if (e.getSource() == bRemoveTempTally_3) {
			roomNumberRemoveTempTally = 21;
			removeTempTallyWarningPanel();
		}
	}
}
