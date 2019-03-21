package ohh1.logic;

import java.util.ArrayList;
import java.util.List;

import gps.api.Problem;
import gps.api.Rule;
import gps.api.State;
import ohh1.model.CellColor;
import ohh1.model.Ohh1State;
import ohh1.model.Point;

public class Ohh1Problem implements Problem {

	private State initialState;
	List<Rule> rules;

	public Ohh1Problem(int[][] board) {

		this.rules = new ArrayList<>();
		int emptyCells = 0;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				
				if (board[i][j] == CellColor.BLANK.getValue()) {
				
					rules.add(new Ohh1Rule(new Point(i, j), CellColor.RED));
					rules.add(new Ohh1Rule(new Point(i, j), CellColor.BLUE));
				
					emptyCells++;
				}

			}
		}
		
		this.initialState = new Ohh1State(board, emptyCells);
	}

	@Override
	public State getInitState() {

		return initialState;
	}

	@Override
	public boolean isGoal(State state) {

		Ohh1State ohh1State = (Ohh1State) state;
		return ohh1State.getEmptyCells() == 0;
	}

	@Override
	public List<Rule> getRules() {

		return rules;
	}

}
