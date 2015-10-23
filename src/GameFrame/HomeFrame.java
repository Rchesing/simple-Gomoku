package GameFrame;

import javax.swing.*;

import GameFunction.Function;
import java.awt.event.*;
import java.awt.*;

/**
 * 
 * @author RcheColy
 *
 */
public class HomeFrame extends JFrame {
	private Function chessBoard;
	private JPanel toolbar;

	public static JButton startButton, backButton, exitButton, giveupButton,
			ruleButton;

	public static JMenuBar menuBar;
	private JMenu sysMenu;
	public static JMenuItem startMenuItem, exitMenuItem, backMenuItem,
			giveupMenuItem, ruleMenuItem;

	private JMenu sysMenu2;
	public static JMenuItem backgroundMenuItem, timeMenuItem;

	public HomeFrame() {
		setTitle("Gomoku");
		chessBoard = new Function();

		menuBar = new JMenuBar();
		sysMenu = new JMenu("Menu");
		startMenuItem = new JMenuItem("Play again");
		exitMenuItem = new JMenuItem("Exit");
		backMenuItem = new JMenuItem("Undo");
		giveupMenuItem = new JMenuItem("Admin defeat");
		ruleMenuItem = new JMenuItem("About");

		sysMenu.add(startMenuItem);
		sysMenu.add(backMenuItem);
		sysMenu.add(giveupMenuItem);
		sysMenu.add(ruleMenuItem);
		sysMenu.add(exitMenuItem);
		sysMenu2 = new JMenu("Settings");
		backgroundMenuItem = new JMenuItem("Background");
		timeMenuItem = new JMenuItem("Times");
		sysMenu2.add(timeMenuItem);
		sysMenu2.add(backgroundMenuItem);

		MyListener lis = new MyListener();
		startMenuItem.addActionListener(lis);
		backMenuItem.addActionListener(lis);
		giveupMenuItem.addActionListener(lis);
		exitMenuItem.addActionListener(lis);
		ruleMenuItem.addActionListener(lis);
		backgroundMenuItem.addActionListener(lis);
		timeMenuItem.addActionListener(lis);

		menuBar.add(sysMenu);
		menuBar.add(sysMenu2);
		menuBar.setBackground(new Color(215, 193, 107));

		sysMenu.setFont(new Font("Arial", Font.PLAIN, 25));
		sysMenu2.setFont(new Font("Arial", Font.PLAIN, 25));
		setJMenuBar(menuBar);

		startMenuItem.setBackground(new Color(215, 193, 107));
		startMenuItem.setFont(new Font("Arial", Font.PLAIN, 25));
		exitMenuItem.setBackground(new Color(215, 193, 107));
		exitMenuItem.setFont(new Font("Arial", Font.PLAIN, 25));
		backMenuItem.setBackground(new Color(215, 193, 107));
		backMenuItem.setFont(new Font("Arial", Font.PLAIN, 25));
		giveupMenuItem.setBackground(new Color(215, 193, 107));
		giveupMenuItem.setFont(new Font("Arial", Font.PLAIN, 25));
		ruleMenuItem.setBackground(new Color(215, 193, 107));
		ruleMenuItem.setFont(new Font("Arial", Font.PLAIN, 25));
		backgroundMenuItem.setBackground(new Color(215, 193, 107));
		backgroundMenuItem.setFont(new Font("Arial", Font.PLAIN, 25));
		timeMenuItem.setBackground(new Color(215, 193, 107));
		timeMenuItem.setFont(new Font("Arial", Font.PLAIN, 25));

		toolbar = new JPanel();
		startButton = new JButton("Play again");
		backButton = new JButton("Undo");
		exitButton = new JButton("Exit");
		giveupButton = new JButton("Admit defeat");
		ruleButton = new JButton("About");
		ruleButton.setBackground(new Color(215, 193, 107));
		startButton.setBackground(new Color(215, 193, 107));
		giveupButton.setBackground(new Color(215, 193, 107));
		exitButton.setBackground(new Color(215, 193, 107));
		backButton.setBackground(new Color(215, 193, 107));
		ruleButton.setFont(new Font("Arial", Font.PLAIN, 25));
		startButton.setFont(new Font("Arial", Font.PLAIN, 25));
		backButton.setFont(new Font("Arial", Font.PLAIN, 25));
		exitButton.setFont(new Font("Arial", Font.PLAIN, 25));
		giveupButton.setFont(new Font("Arial", Font.PLAIN, 25));

		toolbar.setLayout(new GridLayout(5, 1));
		toolbar.add(startButton);
		toolbar.add(backButton);
		toolbar.add(giveupButton);
		toolbar.add(ruleButton);
		toolbar.add(exitButton);

		startButton.addActionListener(lis);
		backButton.addActionListener(lis);
		exitButton.addActionListener(lis);
		giveupButton.addActionListener(lis);
		ruleButton.addActionListener(lis);

		add(toolbar, BorderLayout.EAST);
		add(chessBoard);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(750, 670);
		setResizable(false);
		setLocationRelativeTo(null);
	}

	private class MyListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			if (obj == startMenuItem || obj == startButton) {
				chessBoard.restartGame();
			} else if (obj == exitMenuItem || obj == exitButton) {
				System.exit(0);
			} else if (obj == backMenuItem || obj == backButton) {
				chessBoard.goback();
			} else if (obj == giveupMenuItem || obj == giveupButton) {
				chessBoard.lose();
			} else if (obj == ruleMenuItem || obj == ruleButton) {
				chessBoard.rules();
			} else if (obj == backgroundMenuItem) {
				chessBoard.backgroundChange();
			} else if (obj == timeMenuItem) {
				chessBoard.setTime();
			}
		}
	}

	public static void main(String[] args) {
		HomeFrame f = new HomeFrame();
		f.setVisible(true);
	}
}