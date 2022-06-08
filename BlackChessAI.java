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
 * This AI can only play the black, uses minimax, with board-based memory
 * 
 * @author Zachary
 *
 */
public class BlackChessAI {
	private Chess chessBoard;
	private List<String> log;
	private Map<String, Point> memory;
	private static final String FILE_NAME = "BlackAIMemory.txt";
	
	/**
	 * Creates a chess AI for the black side from the given chess board, loads up the memory from the file
	 * 
	 * @param chessBoard the chess board
	 */
	public BlackChessAI(Chess chessBoard) {
		this.chessBoard = chessBoard;
		this.log = new ArrayList<>();
		this.memory = new HashMap<>();
		
		try {
			File file = new File(FILE_NAME);
			Scanner scanner = new Scanner(file);
		      while (scanner.hasNextLine()) {
		        String gameLog = scanner.nextLine();
		        String[] boardsText = gameLog.split(" ");
		        int victory = 0;
		        int scale = 1;
		        int mod = 10;
		        if (boardsText[0].equals("W")) {
        			victory = 1;
        		}
		        for (int i = 1; i < boardsText.length; i++) {
		        	String boardText = boardsText[i];
		        	if (boardText.length() == 64) {
		        		if (i % mod == 0) {
		        			scale++;
		        			mod *= 3;
		        		}
		        		if (memory.containsKey(boardText)) {
			        		memory.get(boardText).y += (1 * scale);
			        		memory.get(boardText).x += (victory * scale);
			        	} else {
			        		memory.put(boardText, new Point(1 + victory, 2));
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
		log.add(chessBoard.toString());
		Move move = allBlackMoves(chessBoard).get(max(chessBoard, null, 3, -2000, 2000));
		chessBoard.move(move);
		log.add(chessBoard.toString());
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
			FileWriter fw = new FileWriter(FILE_NAME, true);
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
		List<Move> moves = allBlackMoves(temp);
		
		int[] values = new int[moves.size()];
		
		// leaf node
		if (layers == 0) {
			int value = temp.getPointsBlack() - temp.getPointsWhite();
			temp.copy(backup);
			return value;
		}
		
		// get all the min values from the children of this node
		int max = -2000;
		for (int i = 0; i < moves.size(); i++) {
			values[i] = min(temp, moves.get(i), layers - 1, alpha, beta);
			if (values[i] > beta) {
				break;
			}
			alpha = Math.max(alpha, values[i]);
		}
		
		Random r = new Random();
		
		// find the maximum value from the children
		for (int i = 0; i < values.length; i++) {
			if (values[i] > max) {
				max = values[i];
			}
		}
		
		// this means we have to return an index, not just the max, so much work needs to be done
		if (move == null) {
			int index = -1;
			for (int i = 0; i < values.length; i++) {
				if (values[i] == max) {
					if (index == -1) {
						// set our index to the first max
						index = i;
					} else {
						// decide to pick the new max based on the odds of the new max and the odds of the old
						// as well as their location in the array
						Move iMove = moves.get(i);
						Move indexMove = moves.get(index);
						temp.move(iMove);
						double iChance;
						if (!memory.containsKey(temp.toString())) {
							iChance = (((values.length - i) * 1.0) / values.length);
						} else {
							iChance = ((values.length - i) / values.length) * (memory.get(temp.toString()).x / memory.get(temp.toString()).y);
						}
						temp.copy(backup);
						
						temp.move(indexMove);
						double indexChance;
						if (!memory.containsKey(temp.toString())) {
							indexChance = (((values.length - index) * 1.0) / values.length);
						} else {
							indexChance = (((values.length - index) * 1.0) / values.length) * 
									(memory.get(temp.toString()).x / memory.get(temp.toString()).y);
						}
						
						if (r.nextDouble() < iChance / (iChance + indexChance)) {
							index = i;
						}
					}
				}
			}
			
			return index;
		}
		
		temp.copy(backup);
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
		List<Move> moves = allWhiteMoves(temp);
		
		int[] values = new int[moves.size()];
		if (layers == 0) {
			int value = temp.getPointsBlack() - temp.getPointsWhite();
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
