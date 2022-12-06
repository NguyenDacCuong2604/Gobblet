package AI;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class AIPlayer {

	public static final int LEVEL = 3;
	int type;
	public AIPlayer(int type) {
		this.type=type;
	}
	/** 
	 * 
	 * @param board chess board
	 * @param root  Game Tree root node
	 * @return best move
	 */
	public Move getMove(Board board, DefaultMutableTreeNode root) {
		// delete all the nodes that move
		root.removeAllChildren();
		// find the position the all possible move
		int player = board.getCurrentPlayer();
		List<Move> moves = board.getAllMoves(player);

		// best move
		Move bestMove = null;

		if (player == Piece.RED) {
			// if current player is red, find the lowest score to move, the more score for
			// the red, the red is betters
			int bestScore = Integer.MIN_VALUE;
			DefaultMutableTreeNode bestChild = null;
			for (Move move : moves) {
				Board newBoard = board.move(move);
				if (newBoard != null) {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode("red " + move);
					root.add(node);
					// using minmax algorithm to calculate the score
					int score = 0;
					if(type==1) {
						score = minmax(newBoard, LEVEL - 1, Piece.BLACK, node);
					}
					// using alphabeta algorithm to calculate the score
					else if(type==2) {
						score = alphabeta(newBoard, LEVEL - 1, Piece.BLACK, node, Integer.MIN_VALUE, Integer.MAX_VALUE);
					}
					if (score > bestScore) {
						bestScore = score;
						bestMove = move;
						bestChild = node;
					}
				}
			}
			// put a * on the best move
			if (bestChild != null) {
				bestChild.setUserObject("* " + bestChild.getUserObject().toString());
			}
		} else {
			// if current player is black, find the lowest score to move, the more score for
			// the black, the black is betters
			int bestScore = Integer.MAX_VALUE;
			DefaultMutableTreeNode bestChild = null;
			for (Move move : moves) {
				Board newBoard = board.move(move);
				if (newBoard != null) {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode("black " + move);
					root.add(node);
					int score=0;
					if(type==1) {
						// use the min-max algorithm to calculate the score
						score = minmax(newBoard, LEVEL - 1, Piece.RED, node);
					}
					else if(type==2) {
						// use the alpha-beta algorithm to calculate the score
						score = alphabeta(newBoard, LEVEL - 1, Piece.RED, node, Integer.MIN_VALUE, Integer.MAX_VALUE);
					}
					if (score < bestScore) {
						bestScore = score;
						bestMove = move;
						bestChild = node;
					}
				}
			}
			// put a * on the best move
			if (bestChild != null) {
				bestChild.setUserObject("* " + bestChild.getUserObject().toString());
			}
		}
		return bestMove;
	}
	/**
	 * Alphabeta algorithm,en.wikipedia.org/wiki/Alpha-beta_pruning
	chess board
	current move
 	current player
	the node in the tree which you move
	score
	 */
	private int alphabeta(Board board, int level, int player, DefaultMutableTreeNode node, int alpha, int beta) {
		if (board.hasWon() || level == 0)
			return board.heuristic();
		List<Move> movements = board.getAllMoves(player);
		if (player == Piece.RED) {
			int bestScore = Integer.MIN_VALUE;
			DefaultMutableTreeNode bestChild = null;
			for (Move move : movements) {
				Board newBoard = board.move(move);
				if (newBoard != null) {
					DefaultMutableTreeNode child = new DefaultMutableTreeNode("red " + move);
					node.add(child);
					int score = alphabeta(newBoard, level - 1, Piece.BLACK, child, alpha, beta);
					if (score > bestScore) {
						bestChild = child;
					}
					bestScore = Math.max(bestScore, score);
					alpha = Math.max(alpha, bestScore);
					if (beta <= alpha) {
						break;
					}
				}
			}
			if (bestChild != null) {
				bestChild.setUserObject("* " + bestChild.getUserObject().toString());
			}
			return bestScore;
		} else {
			// if the current player is black, so we find the lowest score to move
			int bestScore = Integer.MAX_VALUE;
			DefaultMutableTreeNode bestChild = null;
			for (Move move : movements) {
				Board newBoard = board.move(move);
				if (newBoard != null) {
					DefaultMutableTreeNode child = new DefaultMutableTreeNode("black " + move);
					node.add(child);
					int score = alphabeta(newBoard, level - 1, Piece.RED, child, alpha, beta);
					if (score < bestScore) {
						bestChild = child;
					}
					bestScore = Math.min(bestScore, score);
					beta = Math.min(beta, bestScore);
					if (beta <= alpha) {
						break;
					}
				}
			}
			// we add * on the best move
			if (bestChild != null) {
				bestChild.setUserObject("* " + bestChild.getUserObject().toString());
			}
			return bestScore;
		}

	}

	/**
	 * MinMax algorithm,http://en.wikipedia.org/wiki/Minimax
	chess board
	current move
 	current player
	the node in the tree which you move
	score
	 */
	private int minmax(Board board, int level, int player, DefaultMutableTreeNode node) {
		// if the player win, or the last move, show the total score
		if (board.hasWon() || level == 0)
			return board.heuristic();

		// find the position which we can move
		List<Move> movements = board.getAllMoves(player);

		if (player == Piece.RED) {
			// if the current player is red. so we can find the highest score
			int bestScore = Integer.MIN_VALUE;
			DefaultMutableTreeNode bestChild = null;
			for (Move move : movements) {
				Board newBoard = board.move(move);
				if (newBoard != null) {
					DefaultMutableTreeNode child = new DefaultMutableTreeNode("red " + move);
					node.add(child);
					int score = minmax(newBoard, level - 1, player == Piece.RED ? Piece.BLACK : Piece.RED, child);
					if (score > bestScore) {
						bestScore = score;
						bestChild = child;
					}
				}
			}
			// find the best move to add *
			if (bestChild != null) {
				bestChild.setUserObject("* " + bestChild.getUserObject().toString());
			}
			return bestScore;
		} else {
			// if the current player is black, so we find the lowest score to move
			int bestScore = Integer.MAX_VALUE;
			DefaultMutableTreeNode bestChild = null;
			for (Move move : movements) {
				Board newBoard = board.move(move);
				if (newBoard != null) {
					DefaultMutableTreeNode child = new DefaultMutableTreeNode("black " + move);
					node.add(child);
					int score = minmax(newBoard, level - 1, player == Piece.RED ? Piece.BLACK : Piece.RED, child);
					if (score < bestScore) {
						bestScore = score;
						bestChild = child;
					}
				}
			}
			// we add * on the best move
			if (bestChild != null) {
				bestChild.setUserObject("* " + bestChild.getUserObject().toString());
			}
			return bestScore;
		}
	}

}
