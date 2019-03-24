package ohh1.model;

import java.util.Arrays;

import gps.api.State;

public class Ohh1State implements State {

	private int[][] board;
	private int emptyCells;

	public Ohh1State(final int[][] board, final int emptyCells) {
		this.board = board;
		this.emptyCells = emptyCells;
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(final int[][] board) {
		this.board = board;
	}

	public int getEmptyCells() {
		return emptyCells;
	}

	public void setEmptyCells(int emptyCells) {
		this.emptyCells = emptyCells;
	}

	@Override
	public String getRepresentation() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				sb.append(board[i][j] + " ");
			}
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	public int[][] cloneBoard() {
		int[][] result = new int[board.length][];
		for (int r = 0; r < board.length; r++) {
			result[r] = board[r].clone();
		}
		return result;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ohh1State ohh1State = (Ohh1State) o;

        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j] != ohh1State.board[i][j]){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
    	int aux = 31;
        int[] hashcodes = new int[board.length];
        for (int i = 0; i < board.length; i++){
        	
            hashcodes[i] = aux * Arrays.hashCode(board[i]);
            aux += hashcodes[i];
        }
        return Arrays.hashCode(hashcodes);
    }
	
}