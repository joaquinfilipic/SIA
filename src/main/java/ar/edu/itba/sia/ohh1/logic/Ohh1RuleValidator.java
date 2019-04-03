package ar.edu.itba.sia.ohh1.logic;

import ar.edu.itba.sia.ohh1.model.Ohh1State;

public class Ohh1RuleValidator {

	public static boolean isValid(final Ohh1State state, final Ohh1Rule rule) {

		int[][] board = state.getBoard();
		int row = rule.getRow();
				
		if (areRowsEquals(board[row], rule.getRowArrange())) {
			return false;
		}
		
//		return Ohh1RestrictionManager.isBoardValid(board);
		return true;
	}
	
	private static boolean areRowsEquals(int[] r1, int[] r2) {
		
		for (int i = 0; i < r1.length; i++) {
			if (r1[i] != r2[i]) {
				return false;
			}
		}
		
		return true;
	}

//	private static boolean validateConsecutives(final int[][] board, final Point point,
//			final int colorVal) {
//
//		int i = point.getX();
//		int j = point.getY();
//		
//		int size = board.length;
//		
//		// Validate horizontal
//		if (i >= 2 && board[i - 2][j] == colorVal && board[i - 1][j] == colorVal) {
//			return false;
//		}
//
//		if (i < (size - 2) && board[i + 2][j] == colorVal && board[i + 1][j] == colorVal) {
//			return false;
//		}
//
//		if (i >= 1 && i < (size - 1) && board[i - 1][j] == colorVal && board[i + 1][j] == colorVal) {
//			return false;
//		}
//
//		// Validate vertical
//		if (j >= 2 && board[i][j - 2] == colorVal && board[i][j - 1] == colorVal) {
//			return false;
//		}
//
//		if (j < (size - 2) && board[i][j + 2] == colorVal && board[i][j + 1] == colorVal) {
//			return false;
//		}
//
//		if (j >= 1 && j < (size - 1) && board[i][j - 1] == colorVal && board[i][j + 1] == colorVal) {
//			return false;
//		}
//
//		return true;
//	}
//
//	private static boolean validateColorCount(final int[][] board, final Point point,
//			final int colorVal) {
//
//		int row = point.getX();
//		int col = point.getY();
//				
//		int size = board.length;
//
//		// Validate row
//		int colorCount = 0;
//		for (int j = 0; j < size; j++) {
//			if (board[row][j] == colorVal) {
//				colorCount++;
//			}
//		}
//
//		if (colorCount > (size / 2) - 1) {
//			return false;
//		}
//
//		// Validate column
//		colorCount = 0;
//		for (int i = 0; i < size; i++) {
//			if (board[i][col] == colorVal) {
//				colorCount++;
//			}
//		}
//
//		if (colorCount > (size / 2) - 1) {
//			return false;
//		}
//		
//		return true;
//	}
//
//	private static boolean validateDistinctLines(final int[][] board, final Point point,
//			final int colorVal) {
//
//		int x = point.getX();
//		int y = point.getY();
//		
//		int size = board.length;
//
//		// Validate rows
//		for (int row = 0; row < size; row++) {
//
//			// Do not check current row against itself
//			if (row == x) {
//				continue;
//			}
//
//			boolean equals = true;
//			for (int j = 0; j < size; j++) {
//
//				if ((j == y && board[row][j] != colorVal) || (j != y && board[row][j] != board[x][j]) || (board[row][j] == CellColor.BLANK.getValue())) {
//					equals = false;
//				}
//
//			}
//
//			if (equals) {
//				return false;
//			}
//		}
//
//		// Validate columns
//		for (int col = 0; col < size; col++) {
//
//			// Do not check current column against itself
//			if (col == y) {
//				continue;
//			}
//
//			boolean equals = true;
//			for (int i = 0; i < size; i++) {
//
//				if ((i == x && board[i][col] != colorVal) || (i != x && board[i][col] != board[i][y]) || (board[i][col] == CellColor.BLANK.getValue())) {
//					equals = false;
//				}
//			}
//
//			if (equals) {
//				return false;
//			}
//		}
//
//		// No equal rows or columns
//		return true;
//	}

}