import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

/**
 * A class that represents a checker square on the board and draws the
 * appropriate square that matches it status
 * @author Thuy 'Kim' Ha
 * April 30, 2020
 */
public class CheckerPiece extends JComponent {
	
	//Fields
	private char status;
	private int row, col;
	private final int SIDE = 60, RADIUS = 40;
	private boolean capturable, moveable;
	
	/**
	 * Construct a CheckerPiece object using the inputs
	 * @param row the row of the checker square (0 index)
	 * @param col the column of the checker square (0 index)
	 * @param status the status of the checker square (e, b, or r)
	 */
	public CheckerPiece(int row, int col, char status) {
		if (row < 0 || row > 7) 
			throw new IllegalArgumentException();
		if (col < 0 || col > 7) 
			throw new IllegalArgumentException();
		if (status != 'r' && status != 'b' && status != 'e' && status != 'k' && status != 'q') 
			throw new IllegalArgumentException();
		if ((col + row) % 2 == 0 && status != 'e')
			throw new IllegalArgumentException();
		if ((col + row) % 2 == 1 && status == 'e' && (row < 3 || row > 4))
			throw new IllegalArgumentException();
		this.row = row;
		this.col = col;
		this.status = status;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if ((col + row) % 2 == 0) g.setColor(Color.WHITE);
		else g.setColor(Color.GREEN);
		
		g.fillRect(0, 0, SIDE, SIDE);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, SIDE, SIDE);
		
		if (status == 'e') return;
		if (status == 'b' || status == 'k') g.setColor(Color.BLACK);
		if (status == 'r' || status == 'q') g.setColor(Color.RED);
		
			
		g.fillOval(10, 10, RADIUS, RADIUS);
		g.setColor(Color.BLACK);
		if (this.isKing()) g.setColor(Color.YELLOW);
		g.drawOval(10, 10, RADIUS, RADIUS);
		
	}

	/**
	 * Returns the status of the checker square
	 * @return the status
	 */
	public char getStatus() {
		return status;
	}

	/**
	 * Gets the status of the checker square
	 * @param status the status to set
	 */
	public void setStatus(char status) {
		this.status = status;
		repaint();
	}

	/**
	 * Gets the row of the checker square (0 index)
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Gets the column of the checker square (0 index)
	 * @return the col
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Gets the captrueable field of the checker piece
	 * @return the capturable field
	 */
	public boolean isCapturable() {
		return capturable;
	}

	/**
	 * Sets the captureable field of the checker set
	 * @param capturable the value for the capturable field to set
	 */
	public void setCapturable(boolean capturable) {
		this.capturable = capturable;
	}

	/**
	 * Gets the moveable field of the checker piece
	 * @return the value of the moveable 
	 */
	public boolean isMoveable() {
		return moveable;
	}

	/**
	 * Sets the moveable field of the checker piece
	 * @param moveable the value of the moveable field to set
	 */
	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	
	
	/**
	 * Check if a checker it's a king by checking the row of the checker
	 * @return true if the checker is a king, false otherwise
	 */
	public boolean isKing() {
		if (status == 'b' & row == 7) {
			status = 'k';
			return true;
		}
		if (status == 'r' & row == 0) {
			status = 'q';
			return true;
		}
		if (status == 'k' || status == 'q') return true;
		return false;
	}
	
	/**
	 * Checks if a CheckerPiece object is valid to be the moved. It's not valid when the CheckerPiece is empty,
	 * or has the wrong color for the turn 
	 * @param turnBlack boolean value shows whose turn it is
	 * @return 1 if the checker piece is valid to be moved, false 2, 3, 4 otherwise
	 */
	public int validToBeMoved (boolean turnBlack) {
		if (status == 'e') return 2;
		if (turnBlack && (status == 'r' || status == 'q')) return 3;
		if (!turnBlack && (status == 'b' || status == 'k')) return 4;
		return 1;
	}
	
	/**
	 * Checks if a CheckerPiece object is valid to be the destination. 
	 * @param tbm the checker piece that will be moved
	 * @param boardStatus the board status 
	 * @return 1 if the checker piece is valid to be the destination; 2, 3, 4, 5, 6 if not
	 */
	public int validDestination (CheckerPiece tbm, char[][] boardStatus) {
		if (status != 'e') return 2;
		if ((row + col) % 2 == 0) return 3;
		if (tbm.getRow() == this.row) return 4;
		if (!tbm.isKing() && !isForward(tbm)) return 5;
		if (!isACapture(tbm, boardStatus) && !isValidMove(tbm)) return 6;
		return 1;
	}
	
	/**
	 * Checks if the current checker is capturing another checker or not
	 * @param first the checker to be moved
	 * @param second the destination square
	 * @return true if a checker is being captured, false otherwise
	 */
	public boolean isACapture(CheckerPiece tbm, char[][] boardStatus) {
		if (Math.abs(row - tbm.getRow()) != 2) return false;
		if (Math.abs(col - tbm.getCol()) != 2) return false;
		char mid = boardStatus[(tbm.getRow() + row)/2][(tbm.getCol() + col)/2];
		if (mid == status || mid == 'e') return false;
		return true;
	}
	
	/**
	 * Checks if the move being made is valid or not (without capturing)
	 * @param first the checker to be moved
	 * @param second the destination square
	 * @return true if a checker is being captured, false otherwise
	 */
	private boolean isValidMove(CheckerPiece tbm) {
		if (Math.abs(tbm.getRow() - row) != 1) return false;
		if (Math.abs(tbm.getCol() - col) != 1) return false;
		return true;
	}
	
	/**
	 * Checks if the destination is considered as forward comparing to the checker
	 * that will be moved
	 * @param tbm the checker that will be moved
	 * @return true if it's forward, false otherwise
	 */
	public boolean isForward(CheckerPiece tbm) {
		if (tbm.getStatus() == 'b' && row > tbm.getRow()) return true;
		if (tbm.getStatus() == 'r' && row < tbm.getRow()) return true;
		return false;
		
	}

	@Override
	public String toString() {
		return "CheckerPiece [status=" + status + ", row=" + row + ", col=" + col + ", capturable=" + capturable
				+ ", moveable=" + moveable + "]";
	}
	
}
