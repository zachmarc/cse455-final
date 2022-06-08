package final_project;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Can't call the constructor for this class, should just use the static objects that are created
 * 
 * @author Zachary
 *
 */
public class Piece {
	public static Piece WHITE_KING = new Piece(true, "king");
	public static Piece WHITE_QUEEN = new Piece(true, "queen");
	public static Piece WHITE_BISHOP = new Piece(true, "bishop");
	public static Piece WHITE_KNIGHT = new Piece(true, "knight");
	public static Piece WHITE_ROOK = new Piece(true, "rook");
	public static Piece WHITE_PAWN = new Piece(true, "pawn");
	
	public static Piece BLACK_KING = new Piece(false, "king");
	public static Piece BLACK_QUEEN = new Piece(false, "queen");
	public static Piece BLACK_BISHOP = new Piece(false, "bishop");
	public static Piece BLACK_KNIGHT = new Piece(false, "knight");
	public static Piece BLACK_ROOK = new Piece(false, "rook");
	public static Piece BLACK_PAWN = new Piece(false, "pawn");
	
	private boolean white;
	private String type;
	private int pv;
	private Image sprite;
	
	private Piece(boolean white, String type) {
		this.white = white;
		this.type = type;
		try {
			BufferedImage all = ImageIO.read(Piece.class.getResource("chess.png"));
			
			if (white) {
				if (type.equals("king")) {
					pv = 500;
					sprite = all.getSubimage(0, 0, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				} else if (type.equals("queen")) {
					pv = 9;
					sprite = all.getSubimage(200, 0, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				} else if (type.equals("bishop")) {
					pv = 3;
					sprite = all.getSubimage(400, 0, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				} else if (type.equals("knight")) {
					pv = 3;
					sprite = all.getSubimage(600, 0, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				} else if (type.equals("rook")) {
					pv = 5;
					sprite = all.getSubimage(800, 0, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				} else if (type.equals("pawn")) {
					pv = 1;
					sprite = all.getSubimage(1000, 0, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				}
			} else {
				if (type.equals("king")) {
					pv = 500;
					sprite = all.getSubimage(0, 200, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				} else if (type.equals("queen")) {
					pv = 9;
					sprite = all.getSubimage(200, 200, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				} else if (type.equals("bishop")) {
					pv = 3;
					sprite = all.getSubimage(400, 200, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				} else if (type.equals("knight")) {
					pv = 3;
					sprite = all.getSubimage(600, 200, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				} else if (type.equals("rook")) {
					pv = 5;
					sprite = all.getSubimage(800, 200, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				} else if (type.equals("pawn")) {
					pv = 1;
					sprite = all.getSubimage(1000, 200, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns whether or not this piece is white
	 * 
	 * @return true if this piece is white, false if it is black
	 */
	public boolean isWhite() {
		return white;
	}
	
	/**
	 * Returns the type of piece this is. Options are king, queen, bishop, rook, knight, pawn
	 * 
	 * @return the type of piece this is
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns the point value for this piece
	 * 
	 * @return the point value for this piece
	 */
	public int getPointValue() {
		return pv;
	}
	
	/**
	 * Returns the image representation of this piece
	 * 
	 * @return the image representation of this piece
	 */
	public Image getImage() {
		return sprite;
	}
	
	/**
	 * Returns the char representation of this piece.
	 * 
	 * @return the char representation of this piece
	 */
	public char toChar() {
		if (white) {
			if (type.equals("king")) {
				return '1';
			}
			if (type.equals("queen")) {
				return '2';
			}
			if (type.equals("bishop")) {
				return '3';
			}
			if (type.equals("knight")) {
				return '4';
			}
			if (type.equals("rook")) {
				return '5';
			}
			return '6';
		}
		if (type.equals("king")) {
			return 'a';
		}
		if (type.equals("queen")) {
			return 'b';
		}
		if (type.equals("bishop")) {
			return 'c';
		}
		if (type.equals("knight")) {
			return 'd';
		}
		if (type.equals("rook")) {
			return 'e';
		}
		return 'f';
	}
}
