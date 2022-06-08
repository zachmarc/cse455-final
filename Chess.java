package final_project;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Chess {
	Piece[][] board = new Piece[8][8];
	private Point whiteKingLocation;
	private Point blackKingLocation;
	
	/**
	 * creates a game of chess where all the pieces are where they are supposed to be at the start of a game
	 * empty spaces are represented as null
	 */
	public Chess() {
		// set the white pieces
		for (int col = 0; col < 8; col++) {
			board[6][col] = Piece.WHITE_PAWN;
		}
		board[7][0] = Piece.WHITE_ROOK;
		board[7][1] = Piece.WHITE_KNIGHT;
		board[7][2] = Piece.WHITE_BISHOP;
		board[7][3] = Piece.WHITE_QUEEN;
		board[7][4] = Piece.WHITE_KING;
		board[7][5] = Piece.WHITE_BISHOP;
		board[7][6] = Piece.WHITE_KNIGHT;
		board[7][7] = Piece.WHITE_ROOK;
		whiteKingLocation = new Point(4, 7);
		
		// set the black pieces
		for (int col = 0; col < 8; col++) {
			board[1][col] = Piece.BLACK_PAWN;
		}
		board[0][0] = Piece.BLACK_ROOK;
		board[0][1] = Piece.BLACK_KNIGHT;
		board[0][2] = Piece.BLACK_BISHOP;
		board[0][3] = Piece.BLACK_QUEEN;
		board[0][4] = Piece.BLACK_KING;
		board[0][5] = Piece.BLACK_BISHOP;
		board[0][6] = Piece.BLACK_KNIGHT;
		board[0][7] = Piece.BLACK_ROOK;
		blackKingLocation = new Point(4, 0);
	}
	
	/**
	 * creates a copy game (identical)
	 * 
	 * @param chess the game to copy
	 */
	public Chess(Chess chess) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.board[i][j] = chess.board[i][j];
			}
		}
		this.blackKingLocation = chess.blackKingLocation;
		this.whiteKingLocation = chess.whiteKingLocation;
	}
	
	/**
	 * Copies chess to this
	 * 
	 * @param chess what the copy
	 */
	public void copy(Chess chess) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.board[i][j] = chess.board[i][j];
			}
		}
		this.blackKingLocation = chess.blackKingLocation;
		this.whiteKingLocation = chess.whiteKingLocation;
	}
	
	/**
	 * returns the matrix with the information about the pieces on the chess board
	 * 
	 * @return the chess board matrix
	 */
	public Piece[][] getBoard() {
		return board;
	}
	
	/**
	 * Makes the move designated by the passed in Move object
	 * 
	 * @param move the move to make
	 */
	public void move(Move move) {
		int x1 = move.getX1();
		int x2 = move.getX2();
		int y1 = move.getY1();
		int y2 = move.getY2();
		
		if (board[y1][x1] == null) {
			return;
		}
		board[y2][x2] = board[y1][x1];
		board[y1][x1] = null;
		
		Piece piece = board[y2][x2];
		if (piece.getType().equals("king")) {
			if (board[y2][x2].isWhite()) {
				whiteKingLocation.x = x2;
				whiteKingLocation.y = y2;
			} else {
				blackKingLocation.x = x2;
				blackKingLocation.y = y2;
			}
		}
		
		if (piece.getType().equals("pawn") && piece.isWhite() && y2 == 0) {
			board[y2][x2] = Piece.WHITE_QUEEN;
		}
		if (piece.getType().equals("pawn") && !piece.isWhite() && y2 == 7) {
			board[y2][x2] = Piece.BLACK_QUEEN;
		}
	}
	
	/**
	 * moves the piece located at (x1,y1) on the chess board to the position (x2, y2)
	 * this will override whatever is there. It is up to other methods to make sure this
	 * is a valid move. (x1, y1) is replaced with a null (empty)
	 * 
	 * @param x1 the x coordinate of the piece to move
	 * @param y1 the y coordinate of the piece to move
	 * @param x2 the x coordinate of the new location
	 * @param y2 the y coordinate of the new location
	 */
	public void move(int x1, int y1, int x2, int y2) {
		board[y2][x2] = board[y1][x1];
		board[y1][x1] = null;
		
		if (board[y2][x2].getType().equals("king")) {
			if (board[y2][x2].isWhite()) {
				whiteKingLocation.x = x2;
				whiteKingLocation.y = y2;
			} else {
				blackKingLocation.x = x2;
				blackKingLocation.y = y2;
			}
		}
	}
	
	/**
	 * Returns all possible moves that a piece at location (x, y) can move to.
	 * The moves are represented as Points containing the (x, y) information for
	 * the new location. This takes into account most of the chess rules
	 * Moves that put you in check can be taken, for example.
	 * 
	 * @param x the x value of the piece to get moves for
	 * @param y the y value of the piece to get moves for
	 * @return all the possible move locations
	 */
	public List<Move> getPossibleMoves(int x, int y) {
		Piece piece = board[y][x];
		
		if (piece == null) {
			return null;
		}
		
		if (piece.getType().equals("king")) {
			return getKingMoves(x, y);
		}
		if (piece.getType().equals("queen")) {
			return getQueenMoves(x, y);
		}
		if (piece.getType().equals("bishop")) {
			return getBishopMoves(x, y);
		}
		if (piece.getType().equals("knight")) {
			return getKnightMoves(x, y);
		}
		if (piece.getType().equals("rook")) {
			return getRookMoves(x, y);
		}
		if (piece.getType().equals("pawn")) {
			return getPawnMoves(x, y);
		}
		
		return null;
	}
	
	/**
	 * Returns all possible moves that a king at location (x, y) can move to.
	 * The moves are represented as Points containing the (x, y) information for
	 * the new location.
	 * 
	 * @param x the x value of the king to get moves for
	 * @param y the y value of the king to get moves for
	 * @return all the possible move locations
	 */
	public List<Move> getKingMoves(int x, int y) {
		List<Move> moves = new ArrayList<Move>();
		boolean white = board[y][x].isWhite();
		
		addPoint(moves, x, y, x+1, y+1, white);
		addPoint(moves, x, y, x, y+1, white);
		addPoint(moves, x, y, x-1, y+1, white);
		addPoint(moves, x, y, x+1, y, white);
		addPoint(moves, x, y, x+1, y-1, white);
		addPoint(moves, x, y, x-1, y, white);
		addPoint(moves, x, y, x, y-1, white);
		addPoint(moves, x, y, x-1, y-1, white);
		
		return moves;
	}
	
	/**
	 * Returns all possible moves that a queen at location (x, y) can move to.
	 * The moves are represented as Points containing the (x, y) information for
	 * the new location.
	 * 
	 * @param x the x value of the queen to get moves for
	 * @param y the y value of the queen to get moves for
	 * @return all the possible move locations
	 */
	public List<Move> getQueenMoves(int x, int y) {
		List<Move> moves = getBishopMoves(x, y);
		moves.addAll(getRookMoves(x, y));
		
		return moves;
	}
	
	/**
	 * Returns all possible moves that a bishop at location (x, y) can move to.
	 * The moves are represented as Points containing the (x, y) information for
	 * the new location.
	 * 
	 * @param x the x value of the bishop to get moves for
	 * @param y the y value of the bishop to get moves for
	 * @return all the possible move locations
	 */
	public List<Move> getBishopMoves(int x, int y) {
		List<Move> moves = new ArrayList<Move>();
		boolean white = board[y][x].isWhite();
		
		int i = 1;
		while (addPoint(moves, x, y, x+i, y+i, white)) {
			i++;
		}
		i = 1;
		while (addPoint(moves, x, y, x-i, y+i, white)) {
			i++;
		}
		i = 1;
		while (addPoint(moves, x, y, x+i, y-i, white)) {
			i++;
		}
		i = 1;
		while (addPoint(moves, x, y, x-i, y-i, white)) {
			i++;
		}
		
		return moves;
	}
	
	/**
	 * Returns all possible moves that a knight at location (x, y) can move to.
	 * The moves are represented as Points containing the (x, y) information for
	 * the new location.
	 * 
	 * @param x the x value of the knight to get moves for
	 * @param y the y value of the knight to get moves for
	 * @return all the possible move locations
	 */
	public List<Move> getKnightMoves(int x, int y) {
		List<Move> moves = new ArrayList<Move>();
		boolean white = board[y][x].isWhite();
		
		addPoint(moves, x, y, x+1, y-2, white);
		addPoint(moves, x, y, x+2, y-1, white);
		addPoint(moves, x, y, x+2, y+1, white);
		addPoint(moves, x, y, x+1, y+2, white);
		addPoint(moves, x, y, x-1, y+2, white);
		addPoint(moves, x, y, x-2, y+1, white);
		addPoint(moves, x, y, x-2, y-1, white);
		addPoint(moves, x, y, x-1, y-2, white);
		
		return moves;
	}

	/**
	 * Returns all possible moves that a rook at location (x, y) can move to.
	 * The moves are represented as Points containing the (x, y) information for
	 * the new location.
	 * 
	 * @param x the x value of the rook to get moves for
	 * @param y the y value of the rook to get moves for
	 * @return all the possible move locations
	 */
	public List<Move> getRookMoves(int x, int y) {
		List<Move> moves = new ArrayList<Move>();
		boolean white = board[y][x].isWhite();
		
		int i = 1;
		while (addPoint(moves, x, y, x+i, y, white)) {
			i++;
		}
		i = 1;
		while (addPoint(moves, x, y, x-i, y, white)) {
			i++;
		}
		i = 1;
		while (addPoint(moves, x, y, x, y+i, white)) {
			i++;
		}
		i = 1;
		while (addPoint(moves, x, y, x, y-i, white)) {
			i++;
		}
		
		return moves;
	}

	/**
	 * Returns all possible moves that a pawn at location (x, y) can move to.
	 * The moves are represented as Points containing the (x, y) information for
	 * the new location.
	 * 
	 * @param x the x value of the pawn to get moves for
	 * @param y the y value of the pawn to get moves for
	 * @return all the possible move locations
	 */
	public List<Move> getPawnMoves(int x, int y) {
		List<Move> moves = new ArrayList<Move>();
		boolean white = board[y][x].isWhite();
		
		if (white) {
			addPointPawn(moves, x, y, x, y-1);
			
			// double move for first pawn move
			if (y == 6 && moves.size() == 1) {
				addPointPawn(moves, x, y, x, y-2);
			}
			
			// to steal a diagonal-forward piece
			if (y > 0 && x > 0 && board[y-1][x-1] != null && !board[y-1][x-1].isWhite()) {
				moves.add(new Move(x, y, x-1, y-1));
			}
			if (y > 0 && x < 7 && board[y-1][x+1] != null && !board[y-1][x+1].isWhite()) {
				moves.add(new Move(x, y, x+1, y-1));
			}
		} else {
			addPointPawn(moves, x, y, x, y+1);
			
			// double move
			if (y == 1 && moves.size() == 1) {
				addPointPawn(moves, x, y, x, y+2);
			}
			
			// to steal
			if (y < 7 && x > 0 && board[y+1][x-1] != null && board[y+1][x-1].isWhite()) {
				moves.add(new Move(x, y, x-1, y+1));
			}
			if (y < 7 && x < 7 && board[y+1][x+1] != null && board[y+1][x+1].isWhite()) {
				moves.add(new Move(x, y, x+1, y+1));
			}
		}
		
		return moves;
	}
	
	/**
	 * Takes a list of possible moves and potentially adds a new point to if, if that is a valid location
	 * (not according to a specific piece, but in general, such as: you can't move a white piece to a location
	 * where another white piece is and you can't move pieces off the board). Returns whether or not an
	 * empty location was added to list (does not work for pawns). The return value is to help check
	 * for continuation since pieces can't move through other pieces.
	 * 
	 * @param list the list to add the new move to (if applicable)
	 * @param x1 the x location of the original piece
	 * @param y1 the y location of the original piece
	 * @param x2 the x location of the move to check/add
	 * @param y2 the y location of the move to check/add
	 * @param white true if the original piece is white, false if black
	 * @return returns true if an empty board location was added, false otherwise
	 *         (returns false if the move would capture an enemy piece)
	 */
	public boolean addPoint(List<Move> list, int x1, int y1, int x2, int y2, boolean white) {
		if (x2 >= 0 && x2 < 8 && y2 >= 0 && y2 < 8 && (board[y2][x2] == null || white != board[y2][x2].isWhite())) {
			list.add(new Move(x1, y1, x2, y2));
			return board[y2][x2] == null;
			
		}
		return false;
	}
	
	/**
	 * Takes a list of possible moves and potentially adds a new point to it. For pawns specifically because
	 * pawns cannot take an enemy piece directly in front of it. No return value is necessary for this method
	 * 
	 * @param list the list to add the new move to (if applicable)
	 * @param x1 the x location of the piece to move
	 * @param y1 the y location of the piece to move
	 * @param x2 the x location of the move to check/add
	 * @param y2 the y location of the move to check/add
	 */
	public void addPointPawn(List<Move> list, int x1, int y1, int x2, int y2) {
		if (x2 >= 0 && x2 < 8 && y2 >= 0 && y2 < 8 && board[y2][x2] == null) {
			list.add(new Move(x1, y1, x2, y2));
		}
	}
	
	/**
	 * Returns the total points of the white side
	 * 
	 * @return the total points of the white pieces
	 */
	public int getPointsWhite() {
		int points = 0;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece piece = board[i][j];
				if (piece != null && piece.isWhite()) {
					points += piece.getPointValue();
				}
			}
		}
		
		return points;
	}
	
	/**
	 * Returns the total points of the black side
	 * 
	 * @return the total points of the black pieces
	 */
	public int getPointsBlack() {
		int points = 0;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece piece = board[i][j];
				if (piece != null && !piece.isWhite()) {
					points += piece.getPointValue();
				}
			}
		}
		
		return points;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == null) {
					s.append('-');
				} else {
					s.append(board[i][j].toChar());
				}
			}
		}
		
		return s.toString();
	}
}
