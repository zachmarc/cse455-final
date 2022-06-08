package final_project;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessGame {
	static boolean selected;
	static int selectedX = -1;
	static int selectedY = -1;
	static List<Move> possibleMoves = new ArrayList<Move>();
	static Chess chessBoard = new Chess();
	static BlackChessAI blackAI = new BlackChessAI(chessBoard);
	static WhiteChessAI whiteAI = new WhiteChessAI(chessBoard);
	static boolean gameOver = false;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setBounds(0, 0, 512, 512);
		frame.setUndecorated(true);
		JPanel panel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				boolean white = true;
				for (int y = 0; y < 8; y++) {
					for (int x = 0; x < 8; x++) {
						if (y == selectedY && x == selectedX) {
							g.setColor(Color.GREEN);
						} else if (selected && canMove(x, y)) {
							g.setColor(Color.CYAN);
						} else if (white) {
							g.setColor(Color.WHITE);
						} else {
							g.setColor(Color.GRAY);
						}
						g.fillRect(x*64, y*64, 64, 64);
						white = !white;
					}
					white = !white;
				}
				
				for (int i = 0; i < chessBoard.board.length; i++) {
					for (int j = 0; j < chessBoard.board[0].length; j++) {
						if (chessBoard.board[i][j] != null) {
							g.drawImage(chessBoard.board[i][j].getImage(), j * 64, i * 64, this);
						}
					}
				}
			}
		};
		
		frame.add(panel);
		frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				 //TODO Auto-generated method stub
				int counter = 10;
				while (counter > 0) {
					whiteAI.makeMove();
					panel.paint(frame.getGraphics());
					if (chessBoard.getPointsBlack() <= 500) {
						System.out.println("white wins!");
						chessBoard = new Chess();
						whiteAI.gameOver(true, chessBoard);
						blackAI.gameOver(false, chessBoard);
						counter--;
					}
					blackAI.makeMove();
					panel.paint(frame.getGraphics());
					if (chessBoard.getPointsWhite() <= 500) {
						System.out.println("black wins!");
						chessBoard = new Chess();
						whiteAI.gameOver(false, chessBoard);
						blackAI.gameOver(true, chessBoard);
						counter--;
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// nothing
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (gameOver) {
					return;
				}
				int x = e.getX();
				int y = e.getY();
				
				x = x / 64;
				y = y / 64;
				if (canMove(x, y)) {
					// player makes a move
					chessBoard.move(selectedX, selectedY, x, y);
					selectedX = -1;
					selectedY = -1;
					selected = false;
					possibleMoves.clear();
					blackAI.makeMove();
					panel.paint(frame.getGraphics());
				} else if (chessBoard.board[y][x] == null || (x == selectedX && y == selectedY)) {
					// player deselects from the board
					selectedX = -1;
					selectedY = -1;
					selected = false;
					possibleMoves.clear();
					panel.paint(frame.getGraphics());
				} else if (chessBoard.board[y][x].isWhite()){
					// player selects a piece
					selectedX = x;
					selectedY = y;
					selected = true;
					possibleMoves = chessBoard.getPossibleMoves(selectedX, selectedY);
					panel.paint(frame.getGraphics());
				}
				
				if (chessBoard.getPointsWhite() < 500) {
					// black wins, reset game
					//gameOver = true;
					chessBoard = new Chess();
					blackAI.gameOver(true, chessBoard);
				} else if (chessBoard.getPointsBlack() < 500) {
					// white wins, reset game
					//gameOver = true;
					chessBoard = new Chess();
					blackAI.gameOver(false, chessBoard);
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// nothing
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// nothing
			}
			
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * Checks if the board location (x, y) is in the list of possible moves. Returns true if yes, false otherwise
	 * The list of possible moves is determined by which piece is currently selected.
	 * 
	 * @param x the x location of the potential move
	 * @param y the y location of the potential move
	 * @return if that location is a possible move
	 */
	public static boolean canMove(int x, int y) {
		for (int i = 0; i < possibleMoves.size(); i++) {
			if (possibleMoves.get(i).getX2() == x && possibleMoves.get(i).getY2() == y) {
				return true;
			}
		}
		
		return false;
	}
}
