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
 * This AI can only play the white, uses minimax, with no memory
 * This was just to test BlackChessAI on, so it doesn't fix the 
 * doing and undoing a move forever problem, but when played against BlackChessAI, 
 * it works perfectly fine because BlackChessAI doesn't have that problem
 * 
 * @author Zachary
 *
 */
public class WhiteChessAI {
	private Chess chessBoard;
	private List<String> log;
	private Map<String, Point> memory;
	//private static final String FILE_NAME = "BlackAIMemory.txt";
	
	/**
	 * Creates a chess AI for the white side from the given chess board
	 * 
	 * @param chessBoard the chess board
	 */
	public WhiteChessAI(Chess chessBoard) {
		this.chessBoard = chessBoard;
	}
	
	/**
	 * Makes a move on the chess board based on minimax only
	 */
	public void makeMove() {
		Move move = allWhiteMoves(chessBoard).get(max(chessBoard, null, 3, -2000, 2000));
		chessBoard.move(move);
	}
	
	/**
	 * Sets the new chess board
	 * 
	 * @param victory Whether or not this AI won (irrelevant)
	 * @param chessBoard the new chessboard to replace the old one with
	 */
	public void gameOver(boolean victory, Chess chessBoard) {
		this.chessBoard = chessBoard;
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
	 * otherwise returns the max value. Does no memory checking.
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
		List<Move> moves = allWhiteMoves(temp);
		
		int[] values = new int[moves.size()];
		// leaf node
		if (layers == 0) {
			int value = temp.getPointsWhite() - temp.getPointsBlack();
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
		
		Random r = new Random();
		for (int i = 0; i < values.length; i++) {
			if (values[i] > max) {
				max = values[i];
			}
		}
		
		//System.out.println(memory);
		if (move == null) {
			int index = -1;
			for (int i = 0; i < moves.size(); i++) {
				if (values[i] == max) {
					if (index == -1) {
						index = i;
					} else {
						double iChance = (((values.length - i) * 1.0) / values.length);
						double indexChance = (((values.length - index) * 1.0) / values.length);
						
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
		List<Move> moves = allBlackMoves(temp);
		
		int[] values = new int[moves.size()];
		if (layers == 0) {
			int value = temp.getPointsWhite() - temp.getPointsBlack();
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
