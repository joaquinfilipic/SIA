package ar.edu.itba.sia.ohh1.logic;

import java.util.Optional;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;
import ar.edu.itba.sia.ohh1.model.Ohh1State;

public class Ohh1Rule implements Rule {
	
	private int[] rowArrange;
	private int row;
	
	public Ohh1Rule(final int[] rowArrange, final int row) {
		this.rowArrange = rowArrange;
		this.row = row;
	}

	public int[] getRowArrange() {
		return rowArrange;
	}

	public void setRowArrange(int[] rowArrange) {
		this.rowArrange = rowArrange;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	@Override
	public Integer getCost() {
		return 1;
	}

	@Override
	public String getName() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Rule: paint row ").append(row).append(" with the following arrange: ");
		for (int i = 0; i < rowArrange.length; i++) {
			if (i == rowArrange.length - 1) {
				sb.append(rowArrange[i]);
			}
			else {
				sb.append(rowArrange[i]).append(" ");
			}
		}
		return sb.toString();
	}

	@Override
	public Optional<State> apply(State state) {
		
		Ohh1State ohh1State = (Ohh1State)state;
		
		return Optional.ofNullable(ohh1State)
				.filter(s -> Ohh1RuleValidator.isValid(ohh1State, this))
				.map(s -> {
					int[][] newBoard = ohh1State.cloneBoard();
					
					// Paint row
					newBoard[row] = rowArrange;
					
					return new Ohh1State(newBoard, ohh1State.getFixedPointsByRows());
				});
	}

}
