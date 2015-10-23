package GameFunction;

import javax.swing.*;

import GameFrame.HomeFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class Function extends JPanel implements MouseListener, Runnable {

	JRadioButton DEFAULT, BIYUSHI, CAOHUANG, CHENSHA, CHUNLV, JIANGHUANG,
			LUHUI, NAILV; // the button of setting background's color
	Chessman[] chessConfig = new Chessman[(Chessboard.ROWS + 1) * (Chessboard.COLS + 1)];
	boolean isBlack = true;//Black plays first
	boolean gameOver = false;
	int chessCount = 0; // the number of chessman
	int xIndex, yIndex;
	int maxTime = 0;//the start time
	String blackMessage = "Unlimited";
	String whiteMessage = "Unlimited";
	int blackTime = 0;
	int whiteTime = 0;
	Thread timer = new Thread(this);

	public Function() {
		setBackground(new Color(215, 193, 107));// default color
		timer.start();
		timer.suspend();
		addMouseListener(this);
		addMouseMotionListener(new MouseMotionListener() { 
			public void mouseDragged(MouseEvent e) {
			}

			public void mouseMoved(MouseEvent e) {
				int x1 = (e.getX() - Chessboard.MARGIN + Chessboard.GRID_SPAN / 2)
						/ Chessboard.GRID_SPAN;
				// Convert the coordinates of the mouse click into the grid index
				int y1 = (e.getY() - Chessboard.MARGIN + Chessboard.GRID_SPAN / 2)
						/ Chessboard.GRID_SPAN;
				// Game is over
				// Fall outside the board
				//There are already a chessman in the location
				if (x1 < 0 || x1 > Chessboard.ROWS || y1 < 0 || y1 > Chessboard.COLS
						|| gameOver || findChess(x1, y1))
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				else
					setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draw the chessboard
		for (int i = 0; i <= Chessboard.ROWS; i++) { // draw rows
			g.drawLine(Chessboard.MARGIN, Chessboard.MARGIN + i * Chessboard.GRID_SPAN,
					Chessboard.MARGIN + Chessboard.COLS * Chessboard.GRID_SPAN,
					Chessboard.MARGIN + i * Chessboard.GRID_SPAN);
		}
		for (int i = 0; i <= Chessboard.COLS; i++) {// draw cols
			g.drawLine(Chessboard.MARGIN + i * Chessboard.GRID_SPAN, Chessboard.MARGIN,
					Chessboard.MARGIN + i * Chessboard.GRID_SPAN, Chessboard.MARGIN
							+ Chessboard.ROWS * Chessboard.GRID_SPAN);
		}
		g.drawRect(30, 545, 210, 40);
		g.drawRect(300, 545, 210, 40);
		g.setFont(new Font("Arial", 0, 20));	
		g.drawString("BlackTime:" + blackMessage, 40, 570);
		g.drawString("WhiteTime:" + whiteMessage, 310, 570);
		// draw the chessman

		for (int i = 0; i < chessCount; i++) {
			int xPosition = chessConfig[i].getX() * Chessboard.GRID_SPAN
					+ Chessboard.MARGIN; // X coordinates of grid intersection
			int yPosition = chessConfig[i].getY() * Chessboard.GRID_SPAN
					+ Chessboard.MARGIN;// Y coordinates of grid intersection
			g.setColor(chessConfig[i].getColor()); 
			g.fillOval(xPosition - Chessman.DIAMETER / 2, yPosition
					- Chessman.DIAMETER / 2, Chessman.DIAMETER, Chessman.DIAMETER);
			if (i == chessCount - 1) { // the last chessman
				g.setColor(Color.red);
				g.drawRect(xPosition - Chessman.DIAMETER / 2, yPosition
						- Chessman.DIAMETER / 2, Chessman.DIAMETER, Chessman.DIAMETER);
			}
		}

	}

	public void mousePressed(MouseEvent e) {

		if (gameOver == true)
			return;

		String colorName = isBlack ? "Black" : "White";

		xIndex = (e.getX() - Chessboard.MARGIN + Chessboard.GRID_SPAN / 2)
				/ Chessboard.GRID_SPAN; 
		yIndex = (e.getY() - Chessboard.MARGIN + Chessboard.GRID_SPAN / 2)
				/ Chessboard.GRID_SPAN;


		if (xIndex < 0 || xIndex > Chessboard.ROWS || yIndex < 0
				|| yIndex > Chessboard.COLS)
			return;

		if (findChess(xIndex, yIndex))
			return;

		Chessman ch = new Chessman(xIndex, yIndex, isBlack ? Color.black
				: Color.white);
		chessConfig[chessCount++] = ch;
		repaint();
		if (isWin()) {
			timer.suspend();
			// GameOver
			String msg = String.format("Congratulations£¬%s is win£¡", colorName);
			JOptionPane.showMessageDialog(this, msg, "End",
					JOptionPane.INFORMATION_MESSAGE);
			gameOver = true;
		}
		if (225 == chessCount && !isWin()) {
			timer.suspend();
			String msg = "The game ended in a tie!";
			JOptionPane.showMessageDialog(null, msg, "Deuce",
					JOptionPane.INFORMATION_MESSAGE);
			gameOver = true;

		}
		isBlack = !isBlack;
	}

	public void mouseClicked(MouseEvent e) {
	} 

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	} 

	private boolean findChess(int x, int y) {
		for (Chessman c : chessConfig) {
			if (c != null && c.getX() == x && c.getY() == y)
				return true;
		}
		return false;
	}

	//judge winner
	private boolean isWin() {
		int currentCount = 1; 
		//judge horizontally
		for (int x = xIndex - 1; x >= 0; x--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, yIndex, c) != null) {
				currentCount++;
			} else
				break;
		}

		for (int x = xIndex + 1; x <= Chessboard.ROWS; x++) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, yIndex, c) != null) {
				currentCount++;
			} else
				break;
		}
		if (currentCount >= 5) {
			return true;
		} else
			currentCount = 1;

		
		//judge vertically
		for (int y = yIndex - 1; y >= 0; y--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(xIndex, y, c) != null) {
				currentCount++;
			} else
				break;
		}

		for (int y = yIndex + 1; y <= Chessboard.ROWS; y++) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(xIndex, y, c) != null) {
				currentCount++;
			} else
				break;
		}
		if (currentCount >= 5) {
			return true;
		} else
			currentCount = 1;


		//judge diagonally
		for (int x = xIndex + 1, y = yIndex - 1; y >= 0 && x <= Chessboard.COLS; x++, y--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				currentCount++;
			} else
				break;
		}

		for (int x = xIndex - 1, y = yIndex + 1; y <= Chessboard.ROWS && x >= 0; x--, y++) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				currentCount++;
			} else
				break;
		}
		if (currentCount >= 5) {
			return true;
		} else
			currentCount = 1;



		for (int x = xIndex - 1, y = yIndex - 1; y >= 0 && x >= 0; x--, y--) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				currentCount++;
			} else
				break;
		}

		for (int x = xIndex + 1, y = yIndex + 1; y <= Chessboard.ROWS
				&& x <= Chessboard.COLS; x++, y++) {
			Color c = isBlack ? Color.black : Color.white;
			if (getChess(x, y, c) != null) {
				currentCount++;
			} else
				break;
		}
		if (currentCount >= 5) {
			return true;
		} else
			currentCount = 1;

		return false;
	}

	private Chessman getChess(int xIndex, int yIndex, Color color) {
		for (Chessman c : chessConfig) {
			if (c != null && c.getX() == xIndex && c.getY() == yIndex
					&& c.getColor() == color)
				return c;
		}
		return null;
	}

	public void restartGame() {
		for (int i = 0; i < chessConfig.length; i++)
			chessConfig[i] = null;
		isBlack = true;
		gameOver = false;
		chessCount = 0;
		blackTime = maxTime;
		whiteTime = maxTime;
		if (blackTime == 0&&whiteTime == 0) {
		}
		else timer.resume();
		repaint();

	}

	// undo the last chessman
	public void goback() {
		if (chessCount == 0)
			return;
		if (gameOver == true)
			return;

		chessConfig[chessCount - 1] = null;
		chessCount--;
		if (chessCount > 0) {
			xIndex = chessConfig[chessCount - 1].getX();
			yIndex = chessConfig[chessCount - 1].getY();
		}
		isBlack = !isBlack;
		repaint();
	}

	// Dimension
	public Dimension getPreferredSize() {
		return new Dimension(
				Chessboard.MARGIN * 2 + Chessboard.GRID_SPAN * Chessboard.COLS,
				Chessboard.MARGIN * 2 + Chessboard.GRID_SPAN * Chessboard.ROWS);
	}

	// admit defeat
	public void lose() {

		String msg = null;
		if (gameOver == true)
			return;
		timer.suspend();
		if (chessCount % 2 == 1) {
			msg = "The white admits defeat,the black is win!";
		} else
			msg = "The black admits defeat,the white is win!";
		JOptionPane.showMessageDialog(null, msg, "Competition results",
				JOptionPane.INFORMATION_MESSAGE);
		gameOver = true;
		repaint();
	}

	public void rules() {
		String msg = "The rules of the gomoku:The gomuku is played by two players."
				+ "One\nplayer uses white chessmans and other uses black chessmans.\n"
				+ "The chessboard is made up of 15 vertical lines and 15 horizontal\n"
				+ "lines.The chessmans are put on the crosses and moved along the \n"
				+ "lines."
				+ "The winner is the first player to get an unbroken row of five \n"
				+ "stones horizontally, vertically, or diagonally."
				
				+ "\nBlack plays first£¡"
				+ "\nThank you for your support.If you can optimize my codes,I'll always\n"
				+ "be indebted to you for what you've done!"
				+ "\nAuthor£ºRcheColy";
		JOptionPane.showMessageDialog(null, msg, "About",
				JOptionPane.INFORMATION_MESSAGE);
		repaint();
	}

	public void run() {
		while (!gameOver) {
			if (maxTime > 0) {
				while (true) {
					if (isBlack) {
						blackTime--;
						if (blackTime == 0) {
							int i = JOptionPane.showConfirmDialog(this,
									"Black is out of time,white is win!" + "\nPlay again?", "Warning",
									JOptionPane.YES_NO_OPTION);
							if (i == JOptionPane.YES_OPTION) {
								repaint();
								restartGame();
								
							} else {
								gameOver = true;
								timer.suspend();
							}
						}
					} else {
						whiteTime--;
						if (whiteTime == 0) {
							int i = JOptionPane.showConfirmDialog(this,
									"White is out of time,black is win!" + "\nPlay again?", "Warning",
									JOptionPane.YES_NO_OPTION);
							if (i == JOptionPane.YES_OPTION) {
								repaint();
								restartGame();
								
							} else {
								gameOver = true;
								timer.suspend();
							}
						}
					}
					if (0 != maxTime) {
						blackMessage = blackTime / 3600 + ":"
								+ (blackTime / 60 - blackTime / 3600 * 60)
								+ ":" + (blackTime - blackTime / 60 * 60);
						whiteMessage = whiteTime / 3600 + ":"
								+ (whiteTime / 60 - whiteTime / 3600 * 60)
								+ ":" + (whiteTime - whiteTime / 60 * 60);
					}
					repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void setTime() {
		String input = JOptionPane
				.showInputDialog("Please input the game's maximum time (unit: min), if you input 0, it means unlimited:");
		if (input == null)
			return;
		if (isInteger(input) == true)
			while (isInteger(input)) {
				maxTime = Integer.parseInt(input) * 60;
				if (maxTime < 0) {
					JOptionPane.showMessageDialog(null, "You don't input the correct number of time in the dialog",
							"Message", JOptionPane.PLAIN_MESSAGE);
				} else
					break;
				input = JOptionPane
						.showInputDialog("Please input the game's maximum time (unit: min) again, if you input 0, it means unlimited:");
				if (input == null)
					return;
			}
		else {
			while (isInteger(input) == false) {
				JOptionPane.showMessageDialog(null, "You don't input the correct number of time in the dialog", "Message",
						JOptionPane.PLAIN_MESSAGE);
				input = JOptionPane
						.showInputDialog("Please input the game's maximum time (unit: min) again, if you input 0, it means unlimited:");
				if (input == null)
					return;
			}
			if (isInteger(input) == true)
				while (isInteger(input)) {
					maxTime = Integer.parseInt(input) * 60;
					if (maxTime < 0) {
						JOptionPane.showMessageDialog(null,
								"You don't input the correct number of time in the dialog", "Message",
								JOptionPane.PLAIN_MESSAGE);
					} else
						break;
					input = JOptionPane
							.showInputDialog("Please input the game's maximum time (unit: min) again, if you input 0, it means unlimited:");
					if (input == null)
						return;
				}
		}
		if (maxTime == 0) {
			int result = JOptionPane
					.showConfirmDialog(this, "The time of game is set sucessfully!Game begins?");
			if (result == 0) {
				blackMessage = "Unlimited";
				whiteMessage = "Unlimited";
				restartGame();
				timer.suspend();
				repaint();
			}
		} else if (maxTime > 0) {
			int result = JOptionPane.showConfirmDialog(this,
					"The time of game is set sucessfully!Game begins?");
			if (result == 0) {
				blackMessage = maxTime / 3600 + ":"
						+ (maxTime / 60 - maxTime / 3600 * 60) + ":"
						+ (maxTime - maxTime / 60 * 60);
				whiteMessage = maxTime / 3600 + ":"
						+ (maxTime / 60 - maxTime / 3600 * 60) + ":"
						+ (maxTime - maxTime / 60 * 60);
				blackTime = maxTime;
				whiteTime = maxTime;
				repaint();
				restartGame();
			}
		}
	}

	public void backgroundChange() {
		JFrame frame = new JFrame();
		frame.setSize(300, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Background Setting");

		JPanel Buttons = new JPanel(); 
		Buttons.setLayout(new GridLayout(4, 2)); 
		Buttons.add(DEFAULT = new JRadioButton("Default")); // 215 193 107
		Buttons.add(BIYUSHI = new JRadioButton("Biyushi")); // 86 149 151
		Buttons.add(CAOHUANG = new JRadioButton("Caohuang")); // 219 206 84
		Buttons.add(CHENSHA = new JRadioButton("Cengsha")); // 175 94 83
		Buttons.add(CHUNLV = new JRadioButton("Chunlv")); // 227 239 209
		Buttons.add(JIANGHUANG = new JRadioButton("Jhuang")); // 180 148 54
		Buttons.add(LUHUI = new JRadioButton("Luhui")); // 169 176 143
		Buttons.add(NAILV = new JRadioButton("Nailv")); // 175 200 186
		frame.add(Buttons);
		DEFAULT.setFont(new Font("Arial", Font.PLAIN, 25));
		BIYUSHI.setFont(new Font("Arial", Font.PLAIN, 25));
		CAOHUANG.setFont(new Font("Arial", Font.PLAIN, 25));
		CHENSHA.setFont(new Font("Arial", Font.PLAIN, 25));
		CHUNLV.setFont(new Font("Arial", Font.PLAIN, 25));
		JIANGHUANG.setFont(new Font("Arial", Font.PLAIN, 25));
		LUHUI.setFont(new Font("Arial", Font.PLAIN, 25));
		NAILV.setFont(new Font("Arial", Font.PLAIN, 25));

		ButtonGroup group = new ButtonGroup();
		group.add(DEFAULT);
		group.add(BIYUSHI);
		group.add(CAOHUANG);
		group.add(CHENSHA);
		group.add(CHUNLV);
		group.add(JIANGHUANG);
		group.add(LUHUI);
		group.add(NAILV);
		NAILV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackground(new Color(175, 200, 186));
				HomeFrame.startButton.setBackground(new Color(175, 200,
						186));
				HomeFrame.backButton.setBackground(new Color(175, 200,
						186));
				HomeFrame.exitButton.setBackground(new Color(175, 200,
						186));
				HomeFrame.giveupButton.setBackground(new Color(175, 200,
						186));
				HomeFrame.ruleButton.setBackground(new Color(175, 200,
						186));
				HomeFrame.startMenuItem.setBackground(new Color(175,
						200, 186));
				HomeFrame.exitMenuItem.setBackground(new Color(175, 200,
						186));
				HomeFrame.backMenuItem.setBackground(new Color(175, 200,
						186));
				HomeFrame.giveupMenuItem.setBackground(new Color(175,
						200, 186));
				HomeFrame.ruleMenuItem.setBackground(new Color(175, 200,
						186));
				HomeFrame.menuBar
						.setBackground(new Color(175, 200, 186));
				HomeFrame.backgroundMenuItem.setBackground(new Color(
						175, 200, 186));
				HomeFrame.timeMenuItem.setBackground(new Color(175, 200,
						186));
				repaint();
			}
		});
		LUHUI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackground(new Color(169, 176, 143));
				HomeFrame.startButton.setBackground(new Color(169, 176,
						143));
				HomeFrame.backButton.setBackground(new Color(169, 176,
						143));
				HomeFrame.exitButton.setBackground(new Color(169, 176,
						143));
				HomeFrame.giveupButton.setBackground(new Color(169, 176,
						143));
				HomeFrame.ruleButton.setBackground(new Color(169, 176,
						143));
				HomeFrame.startMenuItem.setBackground(new Color(169,
						176, 143));
				HomeFrame.exitMenuItem.setBackground(new Color(169, 176,
						143));
				HomeFrame.backMenuItem.setBackground(new Color(169, 176,
						143));
				HomeFrame.giveupMenuItem.setBackground(new Color(169,
						176, 143));
				HomeFrame.ruleMenuItem.setBackground(new Color(169, 176,
						143));
				HomeFrame.menuBar
						.setBackground(new Color(169, 176, 143));
				HomeFrame.backgroundMenuItem.setBackground(new Color(
						169, 176, 143));
				HomeFrame.timeMenuItem.setBackground(new Color(169, 176,
						143));
				repaint();
			}
		});
		JIANGHUANG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackground(new Color(180, 148, 54));
				HomeFrame.startButton.setBackground(new Color(180, 148,
						54));
				HomeFrame.backButton.setBackground(new Color(180, 148,
						54));
				HomeFrame.exitButton.setBackground(new Color(180, 148,
						54));
				HomeFrame.giveupButton.setBackground(new Color(180, 148,
						54));
				HomeFrame.ruleButton.setBackground(new Color(180, 148,
						54));
				HomeFrame.startMenuItem.setBackground(new Color(180,
						148, 54));
				HomeFrame.exitMenuItem.setBackground(new Color(180, 148,
						54));
				HomeFrame.backMenuItem.setBackground(new Color(180, 148,
						54));
				HomeFrame.giveupMenuItem.setBackground(new Color(180,
						148, 54));
				HomeFrame.ruleMenuItem.setBackground(new Color(180, 148,
						54));
				HomeFrame.menuBar.setBackground(new Color(180, 148, 54));
				HomeFrame.backgroundMenuItem.setBackground(new Color(
						180, 148, 54));
				HomeFrame.timeMenuItem.setBackground(new Color(180, 148,
						54));
				repaint();
			}
		});
		CHUNLV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackground(new Color(227, 239, 209));
				HomeFrame.startButton.setBackground(new Color(227, 239,
						209));
				HomeFrame.backButton.setBackground(new Color(227, 239,
						209));
				HomeFrame.exitButton.setBackground(new Color(227, 239,
						209));
				HomeFrame.giveupButton.setBackground(new Color(227, 239,
						209));
				HomeFrame.ruleButton.setBackground(new Color(227, 239,
						209));
				HomeFrame.startMenuItem.setBackground(new Color(227,
						239, 209));
				HomeFrame.exitMenuItem.setBackground(new Color(227, 239,
						209));
				HomeFrame.backMenuItem.setBackground(new Color(227, 239,
						209));
				HomeFrame.giveupMenuItem.setBackground(new Color(227,
						239, 209));
				HomeFrame.ruleMenuItem.setBackground(new Color(227, 239,
						209));
				HomeFrame.menuBar
						.setBackground(new Color(227, 239, 209));
				HomeFrame.backgroundMenuItem.setBackground(new Color(
						227, 239, 209));
				HomeFrame.timeMenuItem.setBackground(new Color(227, 239,
						209));
				repaint();
			}
		});
		CHENSHA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackground(new Color(175, 94, 83));
				HomeFrame.startButton.setBackground(new Color(175, 94,
						83));
				HomeFrame.backButton
						.setBackground(new Color(175, 94, 83));
				HomeFrame.exitButton
						.setBackground(new Color(175, 94, 83));
				HomeFrame.giveupButton.setBackground(new Color(175, 94,
						83));
				HomeFrame.ruleButton
						.setBackground(new Color(175, 94, 83));
				HomeFrame.startMenuItem.setBackground(new Color(175, 94,
						83));
				HomeFrame.exitMenuItem.setBackground(new Color(175, 94,
						83));
				HomeFrame.backMenuItem.setBackground(new Color(175, 94,
						83));
				HomeFrame.giveupMenuItem.setBackground(new Color(175,
						94, 83));
				HomeFrame.ruleMenuItem.setBackground(new Color(175, 94,
						83));
				HomeFrame.menuBar.setBackground(new Color(175, 94, 83));
				HomeFrame.backgroundMenuItem.setBackground(new Color(
						175, 94, 83));
				HomeFrame.timeMenuItem.setBackground(new Color(175, 94,
						83));
				repaint();
			}
		});
		CAOHUANG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackground(new Color(219, 206, 84));
				HomeFrame.startButton.setBackground(new Color(219, 206,
						84));
				HomeFrame.backButton.setBackground(new Color(219, 206,
						84));
				HomeFrame.exitButton.setBackground(new Color(219, 206,
						84));
				HomeFrame.giveupButton.setBackground(new Color(219, 206,
						84));
				HomeFrame.ruleButton.setBackground(new Color(219, 206,
						84));
				HomeFrame.startMenuItem.setBackground(new Color(219,
						206, 84));
				HomeFrame.exitMenuItem.setBackground(new Color(219, 206,
						84));
				HomeFrame.backMenuItem.setBackground(new Color(219, 206,
						84));
				HomeFrame.giveupMenuItem.setBackground(new Color(219,
						206, 84));
				HomeFrame.ruleMenuItem.setBackground(new Color(219, 206,
						84));
				HomeFrame.menuBar.setBackground(new Color(219, 206, 84));
				HomeFrame.backgroundMenuItem.setBackground(new Color(
						219, 206, 84));
				HomeFrame.timeMenuItem.setBackground(new Color(219, 206,
						84));
				repaint();
			}
		});
		BIYUSHI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackground(new Color(86, 149, 151));
				HomeFrame.startButton.setBackground(new Color(86, 149,
						151));
				HomeFrame.backButton.setBackground(new Color(86, 149,
						151));
				HomeFrame.exitButton.setBackground(new Color(86, 149,
						151));
				HomeFrame.giveupButton.setBackground(new Color(86, 149,
						151));
				HomeFrame.ruleButton.setBackground(new Color(86, 149,
						151));
				HomeFrame.startMenuItem.setBackground(new Color(86, 149,
						151));
				HomeFrame.exitMenuItem.setBackground(new Color(86, 149,
						151));
				HomeFrame.backMenuItem.setBackground(new Color(86, 149,
						151));
				HomeFrame.giveupMenuItem.setBackground(new Color(86,
						149, 151));
				HomeFrame.ruleMenuItem.setBackground(new Color(86, 149,
						151));
				HomeFrame.menuBar.setBackground(new Color(86, 149, 151));
				HomeFrame.backgroundMenuItem.setBackground(new Color(86,
						149, 151));
				HomeFrame.timeMenuItem.setBackground(new Color(86, 149,
						151));
				repaint();
			}
		});
		DEFAULT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBackground(new Color(215, 193, 107));
				HomeFrame.startButton.setBackground(new Color(215, 193,
						107));
				HomeFrame.backButton.setBackground(new Color(215, 193,
						107));
				HomeFrame.exitButton.setBackground(new Color(215, 193,
						107));
				HomeFrame.giveupButton.setBackground(new Color(215, 193,
						107));
				HomeFrame.ruleButton.setBackground(new Color(215, 193,
						107));
				HomeFrame.startMenuItem.setBackground(new Color(215,
						193, 107));
				HomeFrame.exitMenuItem.setBackground(new Color(215, 193,
						107));
				HomeFrame.backMenuItem.setBackground(new Color(215, 193,
						107));
				HomeFrame.giveupMenuItem.setBackground(new Color(215,
						193, 107));
				HomeFrame.ruleMenuItem.setBackground(new Color(215, 193,
						107));
				HomeFrame.menuBar
						.setBackground(new Color(215, 193, 107));
				HomeFrame.backgroundMenuItem.setBackground(new Color(
						215, 193, 107));
				HomeFrame.timeMenuItem.setBackground(new Color(215, 193,
						107));
				repaint();
			}
		});
		frame.setVisible(true); 
	}
}