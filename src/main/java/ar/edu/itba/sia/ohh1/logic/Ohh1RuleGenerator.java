package ar.edu.itba.sia.ohh1.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.ohh1.model.CellColor;
import ar.edu.itba.sia.ohh1.model.Point;

public class Ohh1RuleGenerator {

	public static List<Rule> generateRules(int[][] board, List<List<Point>> fixedPointsByRow) {

		List<Rule> rules = new ArrayList<>();
		List<int[]> permutations = new ArrayList<>();

		computeAllPermutations(permutations, new int[board.length], 0, 0, 0);
		filterInvalidPermutations(permutations);

		for (int row = 0; row < board.length; row++) {

			for (int[] rowArrange : permutations) {
				
				if (areFixedPointsValid(board, fixedPointsByRow.get(row), rowArrange)) {
					rules.add(new Ohh1Rule(rowArrange, row));
				}
				
			}

		}

		return rules;
	}

	private static void computeAllPermutations(List<int[]> permutations, int[] rowArrange, int position, int reds,
			int blues) {

		if (position == rowArrange.length) {
			permutations.add(cloneRow(rowArrange));
			return;
		}
		if (reds == rowArrange.length / 2) {
			rowArrange[position] = CellColor.BLUE.getValue();
			computeAllPermutations(permutations, rowArrange, position + 1, reds, blues + 1);
			return;
		}
		if (blues == rowArrange.length / 2) {
			rowArrange[position] = CellColor.RED.getValue();
			computeAllPermutations(permutations, rowArrange, position + 1, reds + 1, blues);
			return;
		}
		rowArrange[position] = CellColor.RED.getValue();
		computeAllPermutations(permutations, rowArrange, position + 1, reds + 1, blues);
		rowArrange[position] = CellColor.BLUE.getValue();
		computeAllPermutations(permutations, rowArrange, position + 1, reds, blues + 1);
		return;
	}

	private static int[] cloneRow(int[] row) {

		int[] cloned = new int[row.length];

		for (int i = 0; i < row.length; i++) {
			cloned[i] = row[i];
		}

		return cloned;
	}

	private static void filterInvalidPermutations(List<int[]> permutations) {

		int consecutiveReds;
		int consecutiveBlues;

		for (Iterator<int[]> it = permutations.iterator(); it.hasNext();) {

			int[] rowArrange = it.next();

			consecutiveReds = 0;
			consecutiveBlues = 0;

			boolean invalid = false;

			for (int i = 0; i < rowArrange.length && !invalid; i++) {

				if (rowArrange[i] == CellColor.RED.getValue()) {
					consecutiveReds += 1;
					consecutiveBlues = 0;
				} else if (rowArrange[i] == CellColor.BLUE.getValue()) {
					consecutiveBlues += 1;
					consecutiveReds = 0;
				} else {
					throw new RuntimeException("Invalid permutation found: " + Arrays.toString(rowArrange));
				}

				// invalid permutation of row
				if (consecutiveReds == 3 || consecutiveBlues == 3) {
					it.remove();
					invalid = true;
				}

			}

		}

	}

	private static boolean areFixedPointsValid(int[][] board, List<Point> fixedPoints, int[] rowArrange) {

		for (Point point : fixedPoints) {
			if (board[point.getX()][point.getY()] != rowArrange[point.getY()]) {
				return false;
			}
		}

		return true;
	}

}
