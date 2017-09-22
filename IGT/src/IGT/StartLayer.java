package IGT;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JLabel;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;

public class StartLayer extends JPanel {
	private static final long serialVersionUID = 646385095896724474L;
	
	private JTextField subjectNumberTextField;
	private JTextField subjectNameTextField;
	private JFormattedTextField mainBlockTextField;
	private JFormattedTextField practiceBlockTextField;
	private JFormattedTextField trialOnEachBlockTextField;
	
	/**
	 * Create the panel.
	 */
	public StartLayer() {
		MainFrame mainFrame = MainFrame.getInstance();
		
		//setting 값을 불러옴
		
		int defaultPracticeBlock = mainFrame.prefs.getInt("practice", 1);
		int defaultMainBlock = mainFrame.prefs.getInt("main", 5);
		int defaultTrialOnEachBlock = mainFrame.prefs.getInt("trial", 20);
		
		setLayout(new BorderLayout(0, 0));

		// 메뉴바
		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);

		JMenu mnIGT = new JMenu("IGT");
		menuBar.add(mnIGT);
		
		JMenuItem menuItemAbout = new JMenuItem("About");
		menuItemAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, 
						"<html>" + 
						"Iowa Gambling Task - " + mainFrame.programVersion + "<br>" + 
						"<br>" + 
						"Developer<br>" +
						"Lee Hui-Jong<br>" +
						"Email : gmlwhdtjd@naver.com<br>" +
						"GitHub : https://github.com/gmlwhdtjd<br>" +
						"<br>" +
						"This Program is distributed under<br>" +
						"the terms of the GNU Lesser General Public Licence (LGPL).<br>" +
						"<br>" + 
						"\"Jfree Chart\" provided by \"JFree.org\" is applied to this program.<br>" + 
						"<br>" +
						"\"Nanum font\" provided by \"Naver\" is applied to this program.<br>" + 
						"프로그램에는 네이버에서 제공한 나눔글꼴이 적용되어 있습니다.<br>" + 
						"<br>" + 
						"Copyright © 2017. Lee Hui-Jong All rights reserved.<br>" + 
						"</html>", "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		mnIGT.add(menuItemAbout);

		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnIGT.add(menuItemExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHowToMake = new JMenuItem("Way of measuring");
		mntmHowToMake.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, 
						"<html>" + 
						"측정 방법<br>" + 
						"<br>" + 
						"손익: A와 B카드 패의 경우 매 10장마다 $1000을 따고 $1250을 잃게 되는 반면,<br>" +
						"&nbsp &nbsp; &nbsp; &nbsp;C와 D카드 패에서는 매 10장마다 $500을 따고 $250을 잃음.<br>" +
						"&nbsp &nbsp; &nbsp; &nbsp;결과적으로 A와 B 카드는 매 10장마다 250$의 손실이 있고,<br>" +
						"&nbsp &nbsp; &nbsp; &nbsp;C와 D 카드는 매 10장마다 250$의 이익이 발생함.<br>" +
						"<br>" + 
						"측정: 의사결정 능력은 유리한 카드를 선택한 횟수에서 불리한 카드를 선택한 횟수를 뺀 <br>" +
						"&nbsp &nbsp; &nbsp; &nbsp;네트점수([C+D]-[A+B])로 측정함. <br>" +
						"&nbsp &nbsp; &nbsp; &nbsp;네트점수는 전체 100시행에서 산출되는 총 네트점수와 전체 시행을 <br>" +
						"&nbsp &nbsp; &nbsp; &nbsp;20시행씩 다섯 블록으로 나누어 각 블록에서 산출되는 블록별 네트점수로 측정함. <br>" +
						"&nbsp &nbsp; &nbsp; &nbsp;IGT의 손상 기준은 10점으로 제시함.<br>" +
						"</html>", "help", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		mnHelp.add(mntmHowToMake);
		// 메뉴바 끝

		// 메인 설정 Panel

		JPanel centerPanel = new JPanel();
		add(centerPanel, BorderLayout.WEST);
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_centerPanel.columnWidths = new int[] { 20, 100, 90, 140, 90, 20};
		gbl_centerPanel.rowHeights = new int[] {30, 30, 30, 30, 15, 30, 5};
		centerPanel.setLayout(gbl_centerPanel);

		JLabel subjectNumberLabel = new JLabel("피 실험자 번호 :");
		subjectNumberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_subjectNumberLabel = new GridBagConstraints();
		gbc_subjectNumberLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_subjectNumberLabel.insets = new Insets(0, 0, 5, 5);
		gbc_subjectNumberLabel.gridx = 1;
		gbc_subjectNumberLabel.gridy = 0;
		centerPanel.add(subjectNumberLabel, gbc_subjectNumberLabel);

		subjectNumberTextField = new JTextField();
		GridBagConstraints gbc_subjectNumberTextField = new GridBagConstraints();
		gbc_subjectNumberTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_subjectNumberTextField.insets = new Insets(0, 0, 5, 5);
		gbc_subjectNumberTextField.gridx = 2;
		gbc_subjectNumberTextField.gridy = 0;
		centerPanel.add(subjectNumberTextField, gbc_subjectNumberTextField);
		
		JLabel subjectNameLabel = new JLabel("피 실험자 이름 :");
		subjectNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_subjectNameLabel = new GridBagConstraints();
		gbc_subjectNameLabel.anchor = GridBagConstraints.EAST;
		gbc_subjectNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_subjectNameLabel.gridx = 1;
		gbc_subjectNameLabel.gridy = 1;
		centerPanel.add(subjectNameLabel, gbc_subjectNameLabel);
		
		subjectNameTextField = new JTextField();
		GridBagConstraints gbc_subjectNameTextField = new GridBagConstraints();
		gbc_subjectNameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_subjectNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_subjectNameTextField.gridx = 2;
		gbc_subjectNameTextField.gridy = 1;
		centerPanel.add(subjectNameTextField, gbc_subjectNameTextField);
		
		NumberFormat numberFormatter = NumberFormat.getNumberInstance();
		numberFormatter.setParseIntegerOnly(true);
		numberFormatter.setGroupingUsed(false);

		JLabel practiceBlockLabel = new JLabel("연습 블록 수 :");
		practiceBlockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_practiceBlockLabel = new GridBagConstraints();
		gbc_practiceBlockLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_practiceBlockLabel.insets = new Insets(0, 0, 5, 5);
		gbc_practiceBlockLabel.gridx = 1;
		gbc_practiceBlockLabel.gridy = 3;
		centerPanel.add(practiceBlockLabel, gbc_practiceBlockLabel);

		practiceBlockTextField = new JFormattedTextField(numberFormatter);
		practiceBlockTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		practiceBlockTextField.setValue(new Integer(defaultPracticeBlock));
	
		GridBagConstraints gbc_practiceBlockTextField = new GridBagConstraints();
		gbc_practiceBlockTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_practiceBlockTextField.insets = new Insets(0, 0, 5, 5);
		gbc_practiceBlockTextField.gridx = 2;
		gbc_practiceBlockTextField.gridy = 3;
		centerPanel.add(practiceBlockTextField, gbc_practiceBlockTextField);

		JLabel mainBlockLabel = new JLabel("메인 블록 수 :");
		mainBlockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_mainBlockLabel = new GridBagConstraints();
		gbc_mainBlockLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_mainBlockLabel.insets = new Insets(0, 0, 5, 5);
		gbc_mainBlockLabel.gridx = 3;
		gbc_mainBlockLabel.gridy = 3;
		centerPanel.add(mainBlockLabel, gbc_mainBlockLabel);

		mainBlockTextField = new JFormattedTextField(numberFormatter);
		mainBlockTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		mainBlockTextField.setValue(defaultMainBlock);

		GridBagConstraints gbc_mainBlockTextField = new GridBagConstraints();
		gbc_mainBlockTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_mainBlockTextField.insets = new Insets(0, 0, 5, 5);
		gbc_mainBlockTextField.gridx = 4;
		gbc_mainBlockTextField.gridy = 3;
		centerPanel.add(mainBlockTextField, gbc_mainBlockTextField);

		JLabel trialOnEachBlockLabel = new JLabel("블록당 시행 수 :");
		trialOnEachBlockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_trialOnEachBlockLabel = new GridBagConstraints();
		gbc_trialOnEachBlockLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_trialOnEachBlockLabel.insets = new Insets(0, 0, 5, 5);
		gbc_trialOnEachBlockLabel.gridx = 1;
		gbc_trialOnEachBlockLabel.gridy = 5;
		centerPanel.add(trialOnEachBlockLabel, gbc_trialOnEachBlockLabel);

		trialOnEachBlockTextField = new JFormattedTextField(numberFormatter);
		trialOnEachBlockTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		trialOnEachBlockTextField.setValue(defaultTrialOnEachBlock);
		GridBagConstraints gbc_trialOnEachBlockTextField = new GridBagConstraints();
		gbc_trialOnEachBlockTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_trialOnEachBlockTextField.insets = new Insets(0, 0, 5, 5);
		gbc_trialOnEachBlockTextField.gridx = 2;
		gbc_trialOnEachBlockTextField.gridy = 5;
		centerPanel.add(trialOnEachBlockTextField, gbc_trialOnEachBlockTextField);
		// 설정

		// Start바
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 10));
		add(buttonPanel, BorderLayout.SOUTH);

		JButton startTestButton = new JButton("Start Test");

		startTestButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.setLoadingLayer();

				new SwingWorker<Void, Void>() {
					
					int settingValues[] = new int[3];

					@Override
					protected Void doInBackground() throws Exception {
						
						settingValues[0] = ((Number) practiceBlockTextField.getValue()).intValue();
						settingValues[1] = ((Number) mainBlockTextField.getValue()).intValue();
						settingValues[2] = ((Number) trialOnEachBlockTextField.getValue()).intValue();
						
						mainFrame.prefs.putInt("practice", settingValues[0]);
						mainFrame.prefs.putInt("main", settingValues[1]);
						mainFrame.prefs.putInt("trial", settingValues[2]);

						return null;
					}

					@Override
					protected void done() {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								try {
									JFrame testFrame = new TestFrame(subjectNumberTextField.getText(), subjectNameTextField.getText(), settingValues);
									testFrame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}.execute();
			}
		});

		buttonPanel.add(startTestButton);
		// Start바 끝
	}
}