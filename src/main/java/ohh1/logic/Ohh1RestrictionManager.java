package ohh1.logic;

import ohh1.model.CellColor;

public class Ohh1RestrictionManager {

	public static boolean isBoardValid(int[][] board) {
		
		return areColsValid(board) && !areRowsEquals(board) && !areColsEquals(board);
	}
	
	private static boolean areColsValid(int[][] board) {
		
		int consecutiveReds;
		int consecutiveBlues;
		
		int totalReds;
		int totalBlues;
		
		for (int col = 0; col < board.length; col++) {
			
			consecutiveReds = 0;
			consecutiveBlues = 0;
			
			totalReds = 0;
			totalBlues = 0;
			
			for (int row = 0; row < board.length; row++) {
				
				if (board[row][col] == CellColor.BLANK.getValue()) {
					consecutiveReds = 0;
					consecutiveBlues = 0;
				} else if (board[row][col] == CellColor.RED.getValue()) {
					consecutiveReds ++;
					consecutiveBlues = 0;
					totalReds ++;
				} else {
					consecutiveReds = 0;
					consecutiveBlues ++;
					totalBlues ++;
				}
				
				if (consecutiveReds >= 3 || consecutiveBlues >= 3 || totalReds > board.length / 2 || totalBlues > board.length / 2) {
					return false;
				}
				
			}
			
		}
		
		return true;
	}
	
	private static boolean areRowsEquals(int[][] board) {
		
		boolean equals;
		
		for (int r1 = 0; r1 < board.length; r1 ++) {
			
			for (int r2 = r1 + 1; r2 < board.length; r2++) {
				
				equals = true;
				for (int col = 0; col < board.length && equals; col++) {
					
					if (board[r1][col] != board[r2][col] || board[r1][col] == CellColor.BLANK.getValue()) {
						equals = false;
					}
					
				}
				
				if (equals) {
					return true;
				}
				
			}
			
		}
		
		return false;
	}
	
	private static boolean areColsEquals(int[][] board) {
		
		boolean equals;
		
		for (int c1 = 0; c1 < board.length; c1 ++) {
			
			for (int c2 = c1 + 1; c2 < board.length; c2++) {
				
				equals = true;
				for (int row = 0; row < board.length && equals; row++) {
					
					if (board[row][c1] != board[row][c2] || board[row][c1] == CellColor.BLANK.getValue()) {
						equals = false;
					}
					
				}
				
				if (equals) {
					return true;
				}
				
			}
			
		}
		
		return false;
	}

//	private static boolean areColsValid(int[][] board) {
//
//		int consecutiveRedCount = 0;
//		int consecutiveBlueCount = 0;
//
//		int totalRedCount = 0;
//		int totalBlueCount = 0;
//
//		for (int col = 0; col < board.length; col++) {
//
//			consecutiveRedCount = 0;
//			consecutiveBlueCount = 0;
//
//			totalRedCount = 0;
//			totalBlueCount = 0;
//
//			for (int i = col + 1; i < board.length; i++) {
//
//				boolean equals = true;
//				for (int row = 0; row < board.length && equals; row++) {
//
//					if (i == col + 1) {
//
//						if (board[row][col] == CellColor.BLANK.getValue()) {
//							consecutiveRedCount = 0;
//							consecutiveBlueCount = 0;
//						} else if (board[row][col] == CellColor.RED.getValue()) {
//							consecutiveRedCount++;
//							consecutiveBlueCount = 0;
//							totalRedCount++;
//						} else {
//							consecutiveBlueCount++;
//							consecutiveRedCount = 0;
//							totalBlueCount++;
//						}
//
//						if (consecutiveRedCount == 3 || consecutiveBlueCount == 3 || totalRedCount > board.length
//								|| totalBlueCount > board.length) {
//							return false;
//						}
//
//					}
//
//					if (board[row][col] != board[row][i]) {
//						equals = false;
//					}
//				}
//
//				if (equals) {
//					return false;
//				}
//			}
//
//			if (col == board.length - 1) {
//
//				for (int row = 0; row < board.length; row++) {
//
//					if (board[row][col] == CellColor.BLANK.getValue()) {
//						consecutiveRedCount = 0;
//						consecutiveBlueCount = 0;
//					} else if (board[row][col] == CellColor.RED.getValue()) {
//						consecutiveRedCount++;
//						consecutiveBlueCount = 0;
//					} else {
//						consecutiveBlueCount++;
//						consecutiveRedCount = 0;
//					}
//
//					if (consecutiveRedCount == 3 || consecutiveBlueCount == 3) {
//						return false;
//					}
//
//				}
//
//			}
//		}
//
//		return true;
//	}
//
//	private static boolean areRowsValid(int[][] board) {
//
//		int consecutiveRedCount = 0;
//		int consecutiveBlueCount = 0;
//
//		for (int row = 0; row < board.length; row++) {
//
//			consecutiveRedCount = 0;
//			consecutiveBlueCount = 0;
//
//			for (int i = row + 1; i < board.length; i++) {
//
//				boolean equals = true;
//				for (int col = 0; col < board.length && equals; col++) {
//
//					if (i == row + 1) {
//
//						if (board[row][col] == CellColor.BLANK.getValue()) {
//							consecutiveRedCount = 0;
//							consecutiveBlueCount = 0;
//						} else if (board[row][col] == CellColor.RED.getValue()) {
//							consecutiveRedCount++;
//							consecutiveBlueCount = 0;
//						} else {
//							consecutiveBlueCount++;
//							consecutiveRedCount = 0;
//						}
//
//						if (consecutiveRedCount == 3 || consecutiveBlueCount == 3) {
//							return false;
//						}
//
//					}
//
//					if (board[row][col] != board[i][col]) {
//						equals = false;
//					}
//				}
//
//				if (equals) {
//					return false;
//				}
//			}
//
//			if (row == board.length - 1) {
//
//				for (int col = 0; col < board.length; col++) {
//
//					if (board[row][col] == CellColor.BLANK.getValue()) {
//						consecutiveRedCount = 0;
//						consecutiveBlueCount = 0;
//					} else if (board[row][col] == CellColor.RED.getValue()) {
//						consecutiveRedCount++;
//						consecutiveBlueCount = 0;
//					} else {
//						consecutiveBlueCount++;
//						consecutiveRedCount = 0;
//					}
//
//					if (consecutiveRedCount == 3 || consecutiveBlueCount == 3) {
//						return false;
//					}
//
//				}
//
//			}
//		}
//
//		return true;
//	}

}
