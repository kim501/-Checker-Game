import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A class that extends the JPanel to represents a checker board that
 * draws the entire checker board by drawing individual checker squares
 * @author Thuy 'Kim' Ha
 * April 30, 2020
 */
public class CheckerBoard extends JPanel{

	private int black, red, blackCanCapture, redCanCapture;
	private char[][] boardStatus;

	
	/**
	 * Construct using inputs to create a CheckerBoard object and draw the checker board
	 * @param boardStatus the 2D array that shows the status (b, r, or e) of the checker
	 * squares by rows and columns
	 */
	public CheckerBoard(char[][] boardStatus) {
		black = 12; red = 12;
		this.boardStatus = new char[8][8];
		setLayout(new GridLayout(8, 8));
		for(int i = 0; i < boardStatus.length; i++) {
			for (int j = 0; j < boardStatus[i].length; j++) {
				this.boardStatus[i][j] = boardStatus[i][j];
				add(new CheckerPiece(i, j, boardStatus[i][j]));
			}
		}
		setCheckersState();
	}
	
	/**
	 * Sets the boardStatus field
	 * @param boardStatus a 2D arrays of characters ('b', 'r', or 'e') to set
	 */
	public void setBoardStatus(char[][] boardStatus) {
		this.boardStatus = boardStatus;
	}
	
	/**
	 * Sets the status of a checker square
	 * @param row the row of the checker square to set
	 * @param col the column of the checker square to set
	 * @param status the status of the checker square to set
	 */
	public void setCheckerPiece(int row, int col, char status) {
		boardStatus[row][col] = status;
		((CheckerPiece) this.getComponent(row * 8 + col)).setStatus(status);
	}

	/**
	 * Gets the number of black checkers that can make a capture move
	 * @return the number of black checkers that can make a capture move
	 */
	public int getBlackCanCapture() {
		return blackCanCapture;
	}

	/**
	 * Gets the number of red checkers that can make a capture move
	 * @return the number of red checkers that can make a capture move
	 */
	public int getRedCanCapture() {
		return redCanCapture;
	}

	/**
	 * Gets the number of black checkers on the board
	 * @return the number of black checkers on the board
	 */
	public int getBlack() {
		return black;
	}

	/**
	 * Sets the number of black checkers on the board
	 * @param black the numbers of black checkers to set
	 */
	public void setBlack(int black) {
		this.black = black;
	}

	/**
	 * Gets the number of red checkers on the board
	 * @return the number of red checkers
	 */
	public int getRed() {
		return red;
	}

	/**
	 * Sets the number of red checkers on the board
	 * @param red the red to set
	 */
	public void setRed(int red) {
		this.red = red;
	}
	
	/**
	 * Gets the boardStatus field
	 * @return the boardStatus
	 */
	public char[][] getBoardStatus() {
		return boardStatus;
	}
	
	/**
	 * Gets the CheckerPiece object using the row and column of that checker square
	 * @param row the row of the checker square
	 * @param col the column of the checker square
	 * @return the CheckerPiece object that associated with the square
	 */
	public CheckerPiece getCheckerPiece(int row, int col) {
		return ((CheckerPiece) this.getComponent(row * 8 + col));
	}
	
	/**
	 * Resets the entire checker board back to the initial state
	 * @param boardStatus the 2D arrays that have the original status of the checker squares
	 */
	public void reset(char[][] boardStatus) {
		black = 12; red = 12;
		for(int i = 0; i < boardStatus.length; i++) {
			for (int j = 0; j < boardStatus[i].length; j++) {
				//Changes the status of the square if it's not the same as the beginning
				if (boardStatus[i][j] != this.boardStatus[i][j])
					setCheckerPiece(i, j, boardStatus[i][j]); 
			}
		}
		setCheckersState();
	}
	
	/**
	 * Checks if any more moves can be made
	 * @return true if no more moves can be made, false otherwise
	 */
	public boolean notMoveable() {
		if (black == 0) return true;
		if (red == 0) return true;
		int redMoveable = 0, blackMoveable = 0;
		for (Component c : getComponents()) {
			CheckerPiece cp = (CheckerPiece) c;
			if (cp.isMoveable()) {
				if (cp.getStatus() == 'b' || cp.getStatus() == 'k') blackMoveable++;
				if (cp.getStatus() == 'r' || cp.getStatus() == 'q') redMoveable++;
			}
		}
		return redMoveable == 0 || blackMoveable == 0;
	}
	
	/**
	 * Sets the captureable and moveable fields of every CheckePiece object
	 */
	public void setCheckersState(){
		blackCanCapture = 0; redCanCapture = 0;
		for (Component c : getComponents()) {
			CheckerPiece cp = (CheckerPiece) c;
			cp.setCapturable(captureable(cp.getRow(), cp.getCol()));
			cp.setMoveable(moveable(cp.getRow(), cp.getCol()));
			if ((cp.getStatus() == 'b' || cp.getStatus() == 'k') && cp.isCapturable()) blackCanCapture++;
			if ((cp.getStatus() == 'r' || cp.getStatus() == 'q') && cp.isCapturable()) redCanCapture++;
		}
	}
	
	/**
	 * Checks if a black checker is moveable
	 * @param row the row of the black checker
	 * @param col the column of the black checker
	 * @return true if the black checker is moveable, false otherwise
	 */
	private boolean blackMoveable(int row, int col) {
		if (blackCaptureable(row, col)) return true;
		if (row + 1 <= 7) {
			if (col - 1 >=  0 && boardStatus[row+1][col-1] == 'e') return true;
			if (col + 1 <=  7 && boardStatus[row+1][col+1] == 'e') return true;
		}
		return false;
	}
	
	/**
	 * Checks if a red checker is moveable
	 * @param row the row of the red checker
	 * @param col the column of the red checker
	 * @return true if the red checker is moveable, false otherwise
	 */
	private boolean redMoveable(int row, int col) {
		if (redCaptureable(row, col)) return true;
		if (row - 1 >= 0) {
			if (col - 1 >=  0 && boardStatus[row-1][col-1] == 'e') return true;
			if (col + 1 <=  7 && boardStatus[row-1][col+1] == 'e') return true;
		}
		return false;
	}
	
	/**
	 * Checks if a red checker can make a capture move
	 * @param row the row of the red checker 
	 * @param col the column of the red checker
	 * @return true if the red checker can make capture move, false otherwise
	 */
	private boolean redCaptureable(int row, int col) {
		if (row - 2 >= 0) {
			if (col - 2 >=  0 && boardStatus[row-2][col-2] == 'e' && boardStatus[row-1][col-1] == 'b') {
				redCanCapture++;
				return true;
			}
			if (col + 2 <=  7 && boardStatus[row-2][col+2] == 'e' && boardStatus[row-1][col+1] == 'b') {
				redCanCapture++;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if a black checker can make a capture move
	 * @param row the row of the black checker 
	 * @param col the column of the black checker
	 * @return true if the black checker can make capture move, false otherwise
	 */
	private boolean blackCaptureable(int row, int col) {
		if (row + 2 <= 7) {
			if (col - 2 >=  0 && boardStatus[row+2][col-2] == 'e' && boardStatus[row+1][col-1] == 'r') return true;
			if (col + 2 <=  7 && boardStatus[row+2][col+2] == 'e' && boardStatus[row+1][col+1] == 'r') return true;
		}
		return false;
		
	}
	
	/**
	 * Checks if a king checker can make a capture move
	 * @param row the row of the king checker
	 * @param col the column of the king checker
	 * @return true if the king checker can make a capture move, false otherwise
	 */
	private boolean kingCaptureable(int row, int col) {
		return blackCaptureable(row, col) || redCaptureable(row, col);
	}
   
   /**
    * Checks if a king checker is moveable
    * @param row the row of the king checker
    * @param col the column of the king checker
    * @return true if a king checker is moveable, false otherwise
    */
   private boolean kingMoveable(int row, int col) {
	   return blackMoveable(row, col) || redMoveable(row, col);
   }
	
	/**
	 * Checks if a checker can make a capture move
	 * @param row the row of the checker
	 * @param col the column of the checker
	 * @return true if the checker can make a capture move, false otherwise
	 */
	boolean captureable(int row, int col) {
		if (boardStatus[row][col] == 'b') return blackCaptureable(row, col);
		if (boardStatus[row][col] == 'r') return redCaptureable(row, col);
		if (boardStatus[row][col] == 'k' || boardStatus[row][col] == 'q') 
			return kingCaptureable(row, col); 
		return false;
	}
	
	/**
	 * Checks is a checker is move able
	 * @param row the row of the checker
	 * @param col the column of the checker
	 * @return true if the checker is moveable, false otherwise
	 */
	public boolean moveable(int row, int col) {
		if (boardStatus[row][col] == 'b') return blackMoveable(row, col);
		if (boardStatus[row][col] == 'r') return redMoveable(row, col);
		if (boardStatus[row][col] == 'k' || boardStatus[row][col] == 'q') 
			return kingMoveable(row, col);
		return false;
	}

	
	@Override
	public String toString() {
		return "Black: " + black + ", Red: " + red + " ";
	}
}
