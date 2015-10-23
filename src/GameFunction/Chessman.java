package GameFunction;

import java.awt.Color;

/*
 * the config of chessman
 */
public class Chessman {
	private int x; // Index x is abscissa in the chessboard
	private int y; // Index y is ordinate in the chessboard
	private Color color;
	public static final int DIAMETER = 30;// Diameter

	public Chessman(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	// get the index x
	public int getX() {
		return x;
	}

	// get the index y
	public int getY() {
		return y;
	}

	public Color getColor() {
		return color;
	}
}