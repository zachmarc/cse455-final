package final_project;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * This AI can play either the white or the black, uses minimax, with move-based memory
 * I basically gave up on this AI and started focusing on BlackChessAI, so this
 * still will often get into loops of doing and undoing a move forever
 * 
 * @author Zachary
 *
 */
public class ChessAI {
	private Chess chessBoard;
	private List<Move> log;
	private Map<Move, Point> memory;
	private boolean white;
	
	/**
	 * Creates a chess AI for a side from the given chess board, determined by white
	 *  and loads up the memory from the file
	 * 
	 * @param chessBoard the chess board
	 * @param white whether or not it is the white side or black
	 */
	public ChessAI(Chess chessBoard, boolean white) {
		this.chessBoard = chessBoard;
		this.log = new ArrayList<Move>();
		this.memory = new HashMap<>();
		this.white = white;
		
		try {
			File file = new File("MiniMaxMemory.txt");
			Scanner scanner = new Scanner(file);
		      while (scanner.hasNextLine()) {
		        String gameLog = scanner.nextLine();
		        String[] movesText = gameLog.split(" ");
		        int victory = 0;
		        if (movesText[0].equals("W")) {
        			victory = 1;
        		}
		        for (int i = 1; i < movesText.length; i++) {
		        	String[] moveText = movesText[i].split(",");
		        	if (moveText.length == 4) {
		        		Move move = new Move(Integer.parseInt(moveText[0]), Integer.parseInt(moveText[1]), Integer.parseInt(moveText[2]), Integer.parseInt(moveText[3]));
		        		if (memory.containsKey(move)) {
			        		memory.get(move).y++;
			        		memory.get(move).x += victory;
			        	} else {
			        		memory.put(move, new Point(1 + victory, 2));
			        	}
		        	}
		        	
		        }
		      }
		      scanner.close();
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    }
	}
	
	/**
	 * Makes a move on the chess board based on minimax with memory enhancement
	 */
	public void makeMove() {
		Move move;
		if (white) {
			move = allWhiteMoves(chessBoard).get(max(chessBoard, null, 4, -2000, 2000));
			System.out.println("white move");
		} else {
			move = allBlackMoves(chessBoard).get(max(chessBoard, null, 4, -2000, 2000));
			System.out.println("black move");
		}
		log.add(move);
		chessBoard.move(move);
	}
	
	/**
	 * Writes the information of the game to disk
	 * 
	 * @param victory Whether or not this AI won
	 * @param chessBoard the new chessboard to replace the old one with
	 */
	public void gameOver(boolean victory, Chess chessBoard) {
		this.chessBoard = chessBoard;
		try {
			FileWriter fw = new FileWriter("MiniMaxMemory.txt", true);
		    BufferedWriter writer = new BufferedWriter(fw);
		    if (victory) {
		    	writer.write("W ");
		    } else {
		    	writer.write("L ");
		    }
		    for (int i = 0; i < log.size(); i++) {
		    	writer.write(log.get(i).toString() + " ");
		    }
		    writer.write("\n");
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a list of every move the black side can make on the given chess board
	 * 
	 * @param chessBoard the current chess board to find every move on
	 * @return a list of every black move
	 */
	public List<Move> allBlackMoves(Chess chessBoard) {
		List<Move> moves = new ArrayList<>();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece piece = chessBoard.board[i][j];
				if (piece != null && !piece.isWhite()) {
					moves.addAll(chessBoard.getPossibleMoves(j, i));
				}
			}
		}
		
		return moves;
	}
	
	/**
	 * Returns a list of every move the white side can make on the given chess board
	 * 
	 * @param chessBoard the current chess board to find every move on
	 * @return a list of every white move
	 */
	public List<Move> allWhiteMoves(Chess chessBoard) {
		List<Move> moves = new ArrayList<>();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece piece = chessBoard.board[i][j];
				if (piece != null && piece.isWhite()) {
					moves.addAll(chessBoard.getPossibleMoves(j, i));
				}
			}
		}
		
		return moves;
	}
	
	/**
	 * Runs the maximizing function of the minimax. If move is null, returns the index of the selected move,
	 * otherwise returns the max value. Does memory checking.
	 * 
	 * @param chessBoard the chess board to check moves on
	 * @param move the previous move being made
	 * @param layers how deep to search the tree
	 * @param alpha the alpha cutoff
	 * @param beta the beta cutoff
	 * @return If move is null, returns the index of the selected move, otherwise returns the max value. 
	 */
	public int max(Chess chessBoard, Move move, int layers, int alpha, int beta) {
		Chess temp = new Chess(chessBoard);
		if (move != null) {
			temp.move(move);
		}
		Chess backup = new Chess(temp);
		List<Move> moves;
		if (!white) {
			moves = allBlackMoves(temp);
		} else {
			moves = allWhiteMoves(temp);
		}
		int[] values = new int[moves.size()];
		if (layers == 0) {
			int value;
			if (white) {
				value = temp.getPointsWhite() - temp.getPointsBlack();
			} else {
				value = temp.getPointsBlack() - temp.getPointsWhite();
			}
			temp.copy(backup);
			return value;
		}
		int max = -2000;
		for (int i = 0; i < moves.size(); i++) {
			values[i] = min(temp, moves.get(i), layers - 1, alpha, beta);
			if (values[i] > beta) {
				break;
			}
			alpha = Math.max(alpha, values[i]);
		}
		temp.copy(backup);
		
		Random r = new Random();
		for (int i = 0; i < values.length; i++) {
			// check the odds on the move
			if (values[i] > max && (memory.get(moves.get(i)) == null || 
					r.nextDouble() < memory.get(moves.get(i)).x / memory.get(moves.get(i)).y)) {
				max = values[i];
			}
		}
		if (move == null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i] == max) {
					return i;
				}
			}
		}
		
		return max;
	}
	
	/**
	 * Runs the minimizing function of the minimax. returns the min value. Does no memory checking
	 * 
	 * @param chessBoard the chess board to check moves on
	 * @param move the previous move being made
	 * @param layers how deep to search the tree
	 * @param alpha the alpha cutoff
	 * @param beta the beta cutoff
	 * @return the min value of the possible moves. 
	 */
	public int min(Chess chessBoard, Move move, int layers, int alpha, int beta) {
		Chess temp = new Chess(chessBoard);
		if (move != null) {
			temp.move(move);
		}
		Chess backup = new Chess(temp);
		List<Move> moves;
		if (white) {
			moves = allBlackMoves(temp);
		} else {
			moves = allWhiteMoves(temp);
		}
		int[] values = new int[moves.size()];
		if (layers == 0) {
			int value;
			if (white) {
				value = temp.getPointsWhite() - temp.getPointsBlack();
			} else {
				value = temp.getPointsBlack() - temp.getPointsWhite();
			}
			temp.copy(backup);
			return value;
		}
		for (int i = 0; i < moves.size(); i++) {
			values[i] = max(temp, moves.get(i), layers - 1, alpha, beta);
			if (values[i] < alpha) {
				break;
			}
			beta = Math.min(beta, values[i]);
		}
		temp.copy(backup);
		
		int min = 2000;
		for (int i = 0; i < values.length; i++) {
			if (values[i] < min) {
				min = values[i];
			}
		}
		if (move == null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i] == min) {
					return i;
				}
			}
		}
		
		return min;
	}
}
