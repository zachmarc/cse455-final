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
 * This AI can only play the white, randomly selects a move, with move-based memory
 * 
 * @author Zachary
 *
 */
public class RandomAI {
	private Chess chessBoard;
	private List<Move> log;
	private Map<Move, Point> memory;
	
	/**
	 * Creates the AI on the given chess board, loads up the memory from the file
	 * The odds of each move are capped at 1/8 so that the odds aren't too low
	 * otherwise this AI would never choose a move from low odds since it never wins
	 * 
	 * @param chessBoard the chess board
	 */
	public RandomAI(Chess chessBoard) {
		this.chessBoard = chessBoard;
		this.memory = new HashMap<>();
		this.log = new ArrayList<>();
		
		try {
			File file = new File("RandomMemory.txt");
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
		        			if (memory.get(move).x * 8 > memory.get(move).y);
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
	 * Makes a random move. If it chooses a move it has made before, checks its memory 
	 * for the odds of that move, if it doesn't meet the odds with a random check, chooses a new
	 * move and repeats. 
	 */
	public void makeMove() {
		//Chess testBoard = new Chess(chessBoard);
		List<Move> moves = allWhiteMoves(chessBoard);
		Random r = new Random();
		int index;
		while (true) {
			index = r.nextInt(moves.size());
			if (memory.containsKey(moves.get(index))) {
				if (r.nextDouble() < memory.get(moves.get(index)).x / memory.get(moves.get(index)).y) {
					break;
				}
				continue;
			}
			break;
		}
		Move move = moves.get(r.nextInt(moves.size()));
		log.add(move);
		chessBoard.move(move);
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
	 * Writes the information of the game to disk
	 * 
	 * @param victory Whether or not this AI won
	 * @param chessBoard the new chessboard to replace the old one with
	 */
	public void gameOver(boolean victory, Chess chessBoard) {
		this.chessBoard = chessBoard;
		
		this.chessBoard = chessBoard;
		try {
			FileWriter fw = new FileWriter("RandomMemory.txt", true);
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
}
