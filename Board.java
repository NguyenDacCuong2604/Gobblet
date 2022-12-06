package AI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * game board, main class
 */
public class Board {
	/**
	 * row number
	 */
	public static final int ROWS = 4;
	/**
	 * col number
	 */
	public static final int COLS = 4;

	/**
	 * the number of unused cell
	 */
	public static final int UNUSED_NUM = 3;

	/**
	 * chess board background color
	 */
	public static final Color BACKGROUND = new Color(238, 238, 238);
	/**
	 * chess board foreground color
	 */
	public static final Color FOREGROUND = new Color(184, 202, 238);

	/**
	 * current player
	 */
	private int currentPlayer;

	/**
	 * middle 4*4 player
	 */
	private Cell[][] cells = new Cell[ROWS][COLS];

	/**
	 * put chess pieces on the used cell of black player
	 */
	private Cell[] blackUnusedCells = new Cell[UNUSED_NUM];
	/**
	 * put chess pieces on the used cell of red player
	 */
	private Cell[] redUnusedCells = new Cell[UNUSED_NUM];

	/**
	 * current the checked chess
	 */
	private Cell selectedCell;

	/**
	 * winner
	 */
	private int winner;

	// currents
	// start! new a board
	public Board() {
		// black chess move first
		currentPlayer = Piece.BLACK;

		selectedCell = null;

		// board 4x4 grid
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				cells[row][col] = new Cell(row, col);
			}
		}

		// black player put the piece which don't use
		for (int i = 0; i < blackUnusedCells.length; i++) {
			Cell cell = new Cell(i, COLS + 1);
			cell.push(new Piece(Piece.TINY, Piece.BLACK));
			cell.push(new Piece(Piece.SMALL, Piece.BLACK));
			cell.push(new Piece(Piece.MEDIUM, Piece.BLACK));
			cell.push(new Piece(Piece.BIG, Piece.BLACK));
			blackUnusedCells[i] = cell;
		}
		// red player put the piece which don't use
		for (int i = 0; i < redUnusedCells.length; i++) {
			Cell cell = new Cell(i, COLS);
			cell.push(new Piece(Piece.TINY, Piece.RED));
			cell.push(new Piece(Piece.SMALL, Piece.RED));
			cell.push(new Piece(Piece.MEDIUM, Piece.RED));
			cell.push(new Piece(Piece.BIG, Piece.RED));
			redUnusedCells[i] = cell;
		}
	}

	public Board(Board board) {
		for (int row = 0; row < board.cells.length; row++) {
			for (int col = 0; col < board.cells[row].length; col++) {
				cells[row][col] = new Cell(board.cells[row][col]);
			}
		}
		// black player put the piece which don't use
		for (int i = 0; i < board.blackUnusedCells.length; i++) {
			blackUnusedCells[i] = new Cell(board.blackUnusedCells[i]);
		}
		// red player put the piece which don't use
		for (int i = 0; i < board.redUnusedCells.length; i++) {
			redUnusedCells[i] = new Cell(board.redUnusedCells[i]);
		}
	}

	/**
	 * draw the chess board
	 */
	public void paint(double width, double height, Graphics2D g2) {

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		double adjustedWidth = width - 1.0;
		double adjustedHeight = height - 1.0;
		double cellWidth = adjustedWidth / (COLS + 2);
		double cellHeight = adjustedHeight / ROWS;

		// create board
		g2.setColor(BACKGROUND);
		g2.fill(new Rectangle2D.Double(0, 0, adjustedWidth, adjustedHeight));
		g2.setColor(FOREGROUND);
		for (int i = 0; i <= ROWS; i++) {
			Line2D hSeparator = new Line2D.Double(0, i * cellHeight, COLS * cellWidth, i * cellHeight);
			g2.draw(hSeparator);
		}
		for (int i = 0; i <= COLS; i++) {
			Line2D vSeparator = new Line2D.Double(i * cellWidth, 0, i * cellWidth, adjustedHeight);
			g2.draw(vSeparator);
		}

		// draw piece
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				cells[row][col].draw(cellWidth, cellHeight, g2);
			}
		}
		for (int i = 0; i < blackUnusedCells.length; i++) {
			blackUnusedCells[i].draw(cellWidth, cellHeight, g2);
		}
		for (int i = 0; i < redUnusedCells.length; i++) {
			redUnusedCells[i].draw(cellWidth, cellHeight, g2);
		}
	}

	/**
	 * find player all the positions that can move the list will contain the steps
	 * you can take
	 */
	public List<Move> getAllMoves(int player) {
		// list cell step
		List<Cell> ownCells = new ArrayList<Cell>();
		// count the steps in the chessboard
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				if (cells[row][col].isTopOwnBy(player)) {
					ownCells.add(cells[row][col]);
				}
			}
		}
		// count the steps outside the chessboard
		if (player == Piece.BLACK) {
			for (int i = 0; i < blackUnusedCells.length; i++) {
				if (blackUnusedCells[i].isTopOwnBy(player)) {
					ownCells.add(blackUnusedCells[i]);
				}
			}
		} else {
			for (int i = 0; i < redUnusedCells.length; i++) {
				if (redUnusedCells[i].isTopOwnBy(player)) {
					ownCells.add(redUnusedCells[i]);
				}
			}
		}

		// list move
		List<Move> moves = new ArrayList<Move>();
		for (Cell cell : ownCells) {
			for (int row = 0; row < cells.length; row++) {
				for (int col = 0; col < cells[row].length; col++) {
					if (cell.canMove(cells[row][col])) {
						// get it from the browsing list in the chessboard
						moves.add(new Move(cell.getRow(), cell.getCol(), cells[row][col].getRow(),
								cells[row][col].getCol()));
					}
				}
			}
		}

		return moves;
	}

	/**
	 * copy the new chess boards create a board when move
	 */
	public Board move(Move move) {
		int fromRow = move.getFromRow();
		int fromCol = move.getFromCol();
		int toRow = move.getToRow();
		int toCol = move.getToCol();

		// find the cell which the chess piece will move
		Cell fromCell = null;
		// piece is outside the chessboard

		// of red
		if (fromCol == COLS) {
			fromCell = redUnusedCells[fromRow];
		}
		// of black
		else if (fromCol == COLS + 1) {
			fromCell = blackUnusedCells[fromRow];
		}
		// piece is in the chessboard
		else {
			fromCell = cells[fromRow][fromCol];
		}

		// chess piece will move to cell
		Cell toCell = cells[toRow][toCol];

		// decide the chess piece can move fromCell to toCell or not
		if (fromCell.canMove(toCell)) {
			Board board = new Board(this);
			Cell newFromCell = null;
			if (fromCol == COLS) {
				newFromCell = board.redUnusedCells[fromRow];
			} else if (fromCol == COLS + 1) {
				newFromCell = board.blackUnusedCells[fromRow];
			} else {
				newFromCell = board.cells[fromRow][fromCol];
			}
			// get new from cell for new board
			newFromCell.move(board.cells[toRow][toCol]);
			return board;
		} else
			return null;
	}
	
	/**
	 * function evaluate to count the score
	 * redPieces   in one line the number of pieces of the AI player
	 * blackPieces in one line the number of pieces of the human player
	 * redBig      the line have the big chess of red side
	 * blackBig    the line have the big chess of black side
	 */
//	 Evaluate rules： 
//	 1.A line in the chess board, there is not chess piece, the grade is 0。 
//	 2.A line in the chess board, there is 1 red player piece(don’t has black player piece),grade is 1. 
//	 3. A line in the chess board, there is 2 red player(don’t has black player largest piece),grade is 10。 
//	 4. A line in the chess board, there is red player(don’t has black player largest piece),grade is 100. 
//	 5. A line in the chess board, there is red player(don’t has black player largest piece),grade is 1000. 
//	 6. A line in the chess board, there is 1 black player piece(don’t has black player piece),grade is 1. 
//	 7. A line in the chess board, there is 2 black player(don’t has red player largest piece),grade is 10。 
//	 8. A line in the chess board, there is black player(don’t has red player largest piece),grade is 100. 
//	 9.A line in the chess board, there is black player(don’t has red player largest piece),grade is 1000.
	private int computeScore(int redPieces, int blackPieces, boolean redBig, boolean blackBig) {
		if (redPieces == 0) { // only black chess piece
			return -(int) Math.pow(10, blackPieces);
		} else if (blackPieces == 0) { // only red chess piece
			return (int) Math.pow(10, redPieces);
		} else if (!redBig && blackPieces > 1) {
			return -(int) Math.pow(10, blackPieces);
		} else if (!blackBig && redPieces > 1) {
			return (int) Math.pow(10, redPieces);
		} else return 0;
	}

	//additionScore= 2^(number piece)
	public int heuristic() {
		int score = 0;
		int redPieces;
		int blackPieces;
		int additionScore;
		double factor = 2;
		boolean redBig;
		boolean blackBig;
		
		//check column
		for (int row = 0; row < ROWS; row++) {
			redPieces = 0;
			blackPieces = 0;
			additionScore = 0;
			redBig = false;
			blackBig = false;
			for (int col = 0; col < COLS; col++) {
				if (cells[row][col].isTopOwnBy(Piece.RED)) {
					redPieces++;
					if (cells[row][col].getTopPieceSize() == Piece.BIG) {
						redBig = true;
					}
					additionScore += (int) Math.pow(factor, cells[row][col].getTopPieceSize());
				} else if (cells[row][col].isTopOwnBy(Piece.BLACK)) {
					blackPieces++;
					if (cells[row][col].getTopPieceSize() == Piece.BIG) {
						blackBig = true;
					}
					additionScore -= (int) Math.pow(factor, cells[row][col].getTopPieceSize());
				}
			}
			score += (computeScore(redPieces, blackPieces, redBig, blackBig) + additionScore);
		}
		
		//check row
		for (int col = 0; col < COLS; col++) {
			redPieces = 0;
			blackPieces = 0;
			additionScore = 0;
			redBig = false;
			blackBig = false;
			for (int row = 0; row < ROWS; row++) {
				if (cells[row][col].isTopOwnBy(Piece.RED)) {
					redPieces++;
					if (cells[row][col].getTopPieceSize() == Piece.BIG) {
						redBig = true;
					}
					additionScore += (int) Math.pow(factor, cells[row][col].getTopPieceSize());
				} else if (cells[row][col].isTopOwnBy(Piece.BLACK)) {
					blackPieces++;
					if (cells[row][col].getTopPieceSize() == Piece.BIG) {
						blackBig = true;
					}
					additionScore -= (int) Math.pow(factor, cells[row][col].getTopPieceSize());
				}
			}
			score += (computeScore(redPieces, blackPieces, redBig, blackBig) + additionScore);
		}
		
		//check diagonal line
		redPieces = 0;
		blackPieces = 0;
		additionScore = 0;
		redBig = false;
		blackBig = false;
		for (int row = 0, col = 0; row < ROWS && col < COLS; row++, col++) {
			if (cells[row][col].isTopOwnBy(Piece.RED)) {
				redPieces++;
				if (cells[row][col].getTopPieceSize() == Piece.BIG) {
					redBig = true;
				}
				additionScore += (int) Math.pow(factor, cells[row][col].getTopPieceSize());
			} else if (cells[row][col].isTopOwnBy(Piece.BLACK)) {
				blackPieces++;
				if (cells[row][col].getTopPieceSize() == Piece.BIG) {
					blackBig = true;
				}
				additionScore -= (int) Math.pow(factor, cells[row][col].getTopPieceSize());
			}
		}
		score += (computeScore(redPieces, blackPieces, redBig, blackBig) + additionScore);

		redPieces = 0;
		blackPieces = 0;
		additionScore = 0;
		redBig = false;
		blackBig = false;
		for (int row = ROWS - 1, col = 0; row >= 0 && col < COLS; row--, col++) {
			if (cells[row][col].isTopOwnBy(Piece.RED)) {
				redPieces++;
				if (cells[row][col].getTopPieceSize() == Piece.BIG) {
					redBig = true;
				}
				additionScore += (int) Math.pow(factor, cells[row][col].getTopPieceSize());
			} else if (cells[row][col].isTopOwnBy(Piece.BLACK)) {
				blackPieces++;
				if (cells[row][col].getTopPieceSize() == Piece.BIG) {
					blackBig = true;
				}
				additionScore -= (int) Math.pow(factor, cells[row][col].getTopPieceSize());
			}
		}
		score += (computeScore(redPieces, blackPieces, redBig, blackBig) + additionScore);

		// unused chess piece include
		for (int i = 0; i < UNUSED_NUM; i++) {
			score += (int) Math.pow(factor, redUnusedCells[i].getTopPieceSize());
			score -= (int) Math.pow(factor, blackUnusedCells[i].getTopPieceSize());
		}

		return score;
	}

	
//		row, col
	public void select(int row, int col) {
		if (row >= 0 && row < ROWS && col >= 0 && col < COLS && cells[row][col].isTopOwnBy(currentPlayer)) { // 4x4 grid
			if (selectedCell != null) {
				selectedCell.setSelected(false);
			}
			cells[row][col].setSelected(true);
			selectedCell = cells[row][col];
		} else if (col == COLS + 1 && row >= 0 && row < UNUSED_NUM && blackUnusedCells[row].isTopOwnBy(currentPlayer)) { // black player
																													
			if (selectedCell != null) {
				selectedCell.setSelected(false);
			}
			blackUnusedCells[row].setSelected(true);
			selectedCell = blackUnusedCells[row];
		} else if (col == COLS && row >= 0 && row < UNUSED_NUM && redUnusedCells[row].isTopOwnBy(currentPlayer)) { // red player
																													
			if (selectedCell != null) {
				selectedCell.setSelected(false);
			}
			redUnusedCells[row].setSelected(true);
			selectedCell = redUnusedCells[row];
		}
	}

//	 move the checked piece
	public boolean move(int row, int col) {
		if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
			if (selectedCell != null && selectedCell.isTopOwnBy(currentPlayer)) { // player only can move own chess piece
																					
				if (selectedCell.canMove(cells[row][col])) {
					selectedCell.move(cells[row][col]);
					// after move the chess piece still is checked
					selectedCell.setSelected(false);
					selectedCell = cells[row][col];
					selectedCell.setSelected(true);

					// change player
					currentPlayer = currentPlayer == Piece.BLACK ? Piece.RED : Piece.BLACK;
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

//	  get current player

	public int getCurrentPlayer() {
		return currentPlayer;
	}

//	  change player
	public void switchCurrentPlayer() {
		currentPlayer = currentPlayer == Piece.BLACK ? Piece.RED : Piece.BLACK;
	}

//	  decide the player win or not, win return true, false return false
	public boolean hasWon() {
		// a line how many piece red player numbers
		int redPieces;
		// a line how many piece black player numbers
		int blackPieces;


		// 4 row
		for (int row = 0; row < ROWS; row++) {
			redPieces = 0;
			blackPieces = 0;
			for (int col = 0; col < COLS; col++) {
				if (cells[row][col].isTopOwnBy(Piece.RED)) {
					redPieces++;
				} else if (cells[row][col].isTopOwnBy(Piece.BLACK)) {
					blackPieces++;
				}
			}
			if (blackPieces == ROWS) {
				winner = Piece.BLACK;
				return true;
			} else if (redPieces == ROWS) {
				winner = Piece.RED;
				return true;
			}
		}

		// four column
		for (int col = 0; col < COLS; col++) {
			redPieces = 0;
			blackPieces = 0;
			for (int row = 0; row < ROWS; row++) {
				if (cells[row][col].isTopOwnBy(Piece.RED)) {
					redPieces++;
				} else if (cells[row][col].isTopOwnBy(Piece.BLACK)) {
					blackPieces++;
				}
			}
			if (blackPieces == ROWS) {
				winner = Piece.BLACK;
				return true;
			} else if (redPieces == ROWS) {
				winner = Piece.RED;
				return true;
			}
		}

		// diagonal line
		redPieces = 0;
		blackPieces = 0;
		for (int row = 0, col = 0; row < ROWS && col < COLS; row++, col++) {
			if (cells[row][col].isTopOwnBy(Piece.RED)) {
				redPieces++;
			} else if (cells[row][col].isTopOwnBy(Piece.BLACK)) {
				blackPieces++;
			}
		}
		if (blackPieces == ROWS) {
			winner = Piece.BLACK;
			return true;
		} else if (redPieces == ROWS) {
			winner = Piece.RED;
			return true;
		}

		// diagonal line
		redPieces = 0;
		blackPieces = 0;
		for (int row = ROWS - 1, col = 0; row >= 0 && col < COLS; row--, col++) {
			if (cells[row][col].isTopOwnBy(Piece.RED)) {
				redPieces++;
			} else if (cells[row][col].isTopOwnBy(Piece.BLACK)) {
				blackPieces++;
			}
		}
		if (blackPieces == ROWS) {
			winner = Piece.BLACK;
			return true;
		} else if (redPieces == ROWS) {
			winner = Piece.RED;
			return true;
		}

		return false;
	}

	public void select() {
		if (selectedCell != null) {
			selectedCell.setSelected(true);
		}
	}

//	 cancel selectedCell
	public void unselect() {
		if (selectedCell != null) {
			selectedCell.setSelected(false);
		}
	}

//	get winner
	public int getWinner() {
		return winner;
	}

	public void print() {
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				System.out.print(String.format("%8s", cells[row][col]));
			}
			if (row < redUnusedCells.length) {
				System.out.print(String.format("%8s", redUnusedCells[row]));
			} else {
				System.out.print(String.format("%8s", ""));
			}

			if (row < blackUnusedCells.length) {
				System.out.print(String.format("%8s", blackUnusedCells[row]));
			} else {
				System.out.print(String.format("%8s", ""));
			}
			System.out.println();
		}
	}

}