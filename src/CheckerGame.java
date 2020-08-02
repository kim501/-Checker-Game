import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The CheckerGame application that works as a 2-player game with GUI 
 * @author Thuy 'Kim' Ha
 * April 30, 2020
 */
public class CheckerGame extends JFrame implements MouseListener, ActionListener{
	
	//Fields
	private boolean turnBlack, captureMove, mustJump;
	private CheckerBoard cb;
	private JLabel statusLabel;
	private int picks;
	private CheckerPiece tbm, dest;
	private JOptionPane aboutPane, rulesPane;
	
	private static char[][] boardStatus = new char[][] {
		{'e', 'b', 'e', 'b', 'e', 'b', 'e', 'b'},
		{'b', 'e', 'b', 'e', 'b', 'e', 'b', 'e'},
		{'e', 'b', 'e', 'b', 'e', 'b', 'e', 'b'},
		{'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e'},
		{'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e'},
		{'r', 'e', 'r', 'e', 'r', 'e', 'r', 'e'},
		{'e', 'r', 'e', 'r', 'e', 'r', 'e', 'r'},
		{'r', 'e', 'r', 'e', 'r', 'e', 'r', 'e'},
	};
	
	/**
	 * Default constructor for the class
	 */
	public CheckerGame() {
		//Initializes fields and components
		picks = 0;
		turnBlack = true; mustJump = false;
		setSize(515, 595);
		cb = new CheckerBoard(boardStatus);
		addMouseEvent(cb);
		JMenuBar menuBar = new JMenuBar();
		JPanel statusPanel = new JPanel(new GridLayout(2,1));
		aboutPane = new JOptionPane("About");

		
		//Set up the status bar
		statusLabel = new JLabel("New game! Black starts first.", JLabel.CENTER);
		JLabel informationLabel = new JLabel("This game was developed by Aly Ha.", JLabel.CENTER);
		statusPanel.add(statusLabel);
		statusPanel.add(informationLabel);
		
		//Game menu
		JMenu gameMenu = new JMenu("Game");
		JMenuItem newItem = new JMenuItem("New");
		newItem.addActionListener(this);
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(this);
		gameMenu.add(newItem);
		gameMenu.add(exitItem);
		
		
		//Help menu
		JMenu helpMenu = new JMenu("Help");
		JMenuItem rulesItem = new JMenuItem("Checker Game Rules");
		rulesItem.addActionListener(this);
		JMenuItem aboutItem = new JMenuItem("About Checker Game App");
		aboutItem.addActionListener(this);
		helpMenu.add(rulesItem);
		helpMenu.add(aboutItem);
	
		menuBar.add(gameMenu);
		menuBar.add(helpMenu);
		
		//Add components to the frame
		setJMenuBar(menuBar);
		add(cb, BorderLayout.CENTER);
		add(statusPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);	
	}

	//Main method
	public static void main(String[] args) {
		CheckerGame cg = new CheckerGame();
	}

	/**
	 * Gets the status label
	 * @return the status label
	 */
	public JLabel getStatusLabel() {
		return statusLabel;
	}

	/**
	 * Sets the the checker to be moved
	 * @param toBeMoved the checker to be moved to set
	 */
	public void setToBeMoved(CheckerPiece toBeMoved) {
		if (toBeMoved == null) {
			statusLabel.setText("You haven't picked a checker. Please pick a black one.");
			return;
		}
		switch (toBeMoved.validToBeMoved(turnBlack)) {
		case 2: statusLabel.setText("No checker to be moved. Please pick another one"); break;
		case 3: statusLabel.setText("It's black turn. Please pick a black checker."); break;
		case 4: statusLabel.setText("It's red turn. Please pick a red checker."); break;
		case 1: this.tbm = toBeMoved; picks = 1;
			if (turnBlack) statusLabel.setText("A black checker was picked");
			else statusLabel.setText("A red checker was picked");
		}
	}

	/**
	 * Sets the destination square, if can't, then displays message according to the error code
	 * @param destination the destination square to set
	 */
	public void setDestination(CheckerPiece dest) {
		if (dest == null) {
			statusLabel.setText("You haven't picked a checker. Please pick a black one.");
			return;
		}
		switch (dest.validDestination(tbm, cb.getBoardStatus())){
		case 2: statusLabel.setText("The square is not empty. Please make another move"); picks = 0; break;
		case 3: statusLabel.setText("Can only move diagonal. Please make another move"); picks = 0; break;
		case 4: statusLabel.setText("Cannot move horizontally. Please make another move"); picks = 0; break;
		case 5: statusLabel.setText("Cannot move backward. Please make another move"); picks = 0; break;
		case 6: statusLabel.setText("Cannot move more than one space. Please make another move"); picks = 0; break;
		case 1:
			this.dest = dest; picks = 2;
			if (dest.isACapture(tbm, cb.getBoardStatus())) captureMove = true;
		}
	}
	
	/**
	 * Captures a checker between the checker to be moved and the destination square
	 * @param first the checker to be moved
	 * @param second the destination square
	 */
	private void capture(CheckerPiece first, CheckerPiece second) {
		if (first.getStatus() == 'r' || first.getStatus() == 'q') cb.setBlack(cb.getBlack() - 1);
		if (first.getStatus() == 'b' || first.getStatus() == 'k') cb.setRed(cb.getRed() - 1);
		//Removes the captured checker
		cb.setCheckerPiece((first.getRow() + second.getRow())/2, 
				(first.getCol() + second.getCol())/2,'e');
	}

	/**
	 * Adds mouseEvent to every CheckerPiece object in the panel
	 * @param cb the checker board that has all the checker pieces
	 */
	private void addMouseEvent(CheckerBoard cb) {
		for (int i = 0; i < cb.getBoardStatus().length; i++) {
			for (int j = 0; j < cb.getBoardStatus().length; j++)
				((CheckerPiece) cb.getComponent(i * 8 + j)).addMouseListener(this);
			
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		CheckerPiece cp = (CheckerPiece) e.getComponent();
		statusLabel.setText(cb.toString());
		if (picks == 0) setToBeMoved(cp);
		else if (picks == 1) setDestination(cp);
		if (picks == 2 && tbm != null && dest != null) {
			if (mustJump() && !captureMove) {
				statusLabel.setText("You must make a jump if you can! Please make a jump move");
				resetFields();
			}	
			else {
				moveChecker(tbm, dest); resetFields();
				if (turnBlack) statusLabel.setText(cb.toString() + "[Black turn]");
				else statusLabel.setText(cb.toString() + "[Red turn]");
			}
		}
		if (cb.notMoveable()) endGame();

	}
	
	/**
	 * Moves the checker (captures if it's capturing)
	 * @param first the checker to be moved
	 * @param second the destination square
	 */
	private void moveChecker(CheckerPiece first, CheckerPiece second) {
		//Captures if it's capturing
		if (captureMove) 
			capture(first, second);
		//Moves the checker to the new destination
		cb.setCheckerPiece(second.getRow(), second.getCol(), first.getStatus());
		cb.setCheckerPiece(first.getRow(), first.getCol(),'e');
		cb.setCheckersState();
	}
	
	/**
	 * Displays the winner and disables all moves afterwards
	 */
	private void endGame() {
		if (turnBlack) statusLabel.setText("Red won!");
		else statusLabel.setText("Black won!");
		cb.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New")) reset();
		if (e.getActionCommand().equals("Exit")) dispose();
		if (e.getActionCommand().equals("Checker Game Rules")) 
			JOptionPane.showMessageDialog(this, "For more information, use the link:\n"+ 
		"https://www.wikihow.com/Play-Checkers", "Rules", JOptionPane.INFORMATION_MESSAGE);
		if (e.getActionCommand().equals("About Checker Game App")) 
			JOptionPane.showMessageDialog(this, "Aly Ha, hatk@miamioh.edu, Miami University", 
					"About", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Resets the fields to the original state
	 */
	private void resetFields() {
		if ((captureMove && dest.isCapturable()) || (dest.isKing() && dest.isCapturable())) {
			picks = 1; tbm = dest; dest = null;
		}
		else {
			if (!mustJump || captureMove) turnBlack = !turnBlack; 
			picks = 0; tbm = null; dest = null;	
		}
		mustJump = mustJump();
		captureMove = false;
	}
	
	/**
	 * Check if a capture move can be made
	 * @return true if a capture move can be moved, false otherwise
	 */
	private boolean mustJump() {
		if (turnBlack && cb.getBlackCanCapture() != 0) return true;
		if (!turnBlack && cb.getRedCanCapture() != 0) return true;
		return false;
	}
	
	/**
	 * Resets the checker board and all the related fields
	 */
	private void reset() {
		cb.reset(boardStatus);
		statusLabel.setText("New Game! Black starts first.");
		mustJump = false; captureMove = false; tbm = null; dest = null; picks = 0; turnBlack = true;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
