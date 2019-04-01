package ohh1.logic;

import java.util.List;

import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;
import ohh1.model.CellColor;
import ohh1.model.Ohh1State;

public class Ohh1Problem implements Problem {

	private State initialState;
	List<Rule> rules;

	public Ohh1Problem(Ohh1State initialState) {

		this.rules = Ohh1RuleGenerator.generateRules(initialState.getBoard(), initialState.getFixedPointsByRows());
		
		int[][] board = initialState.getBoard();
		
		for (int row = 0; row < board.length; row ++) {	
			for (int rule = 0; rule < rules.size(); rule ++) {
				
				Ohh1Rule ohh1Rule = (Ohh1Rule) rules.get(rule);
				if (ohh1Rule.getRow() == row) {
					board[row] = ohh1Rule.getRowArrange();
				}
				
			}
		}
		
		initialState.setBoard(board);
		this.initialState = initialState;
	}

	@Override
	public State getInitState() {

		return initialState;
	}

	@Override
	public boolean isGoal(State state) {

		Ohh1State ohh1State = (Ohh1State) state;
		int[][] board = ohh1State.getBoard();
		
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				
				if (board[row][col] == CellColor.BLANK.getValue()) {
					return false;
				}
				
			}
		}
		
		return Ohh1RestrictionManager.isBoardValid(ohh1State.getBoard());
	}

	@Override
	public List<Rule> getRules() {

		return rules;
	}

}
