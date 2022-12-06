package AI;

public class Piece {

	// describe pieces properties
	public static final int BIG = 4;
	public static final int MEDIUM = 3;
	public static final int SMALL = 2;
	public static final int TINY = 1;

	// describe piece(red or black)
	public static final int RED = -1;
	public static final int BLACK = 1;

	/**
	 * piece size
	 */
	private int size;

	/**
	 * type of piece
	 */
	private int color;

	public Piece(int size, int color) {
		this.size = size;
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public int getColor() {
		return color;
	}

}
