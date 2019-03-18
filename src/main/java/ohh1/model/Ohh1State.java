package ohh1.model;

import gps.api.State;

import java.awt.Point;
import java.util.List;

public class Ohh1State implements State {

	private int[][] board;
	private int emptyCells;

	private List<Point> mandatoryMoves;

	public Ohh1State(final int[][] board, final int emptyCells, final List<Point> mandatoryMoves) {
		this.board = board;
		this.emptyCells = emptyCells;
		this.mandatoryMoves = mandatoryMoves;
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

	public List<Point> getMandatoryMoves() {
		return mandatoryMoves;
	}

	public void setMandatoryMoves(final List<Point> mandatoryMoves) {
		this.mandatoryMoves = mandatoryMoves;
	}

	@Override
	public String getRepresentation() {
		return null;
	}
}