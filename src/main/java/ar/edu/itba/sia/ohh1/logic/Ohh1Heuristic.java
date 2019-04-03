package ar.edu.itba.sia.ohh1.logic;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;
import ar.edu.itba.sia.ohh1.model.CellColor;
import ar.edu.itba.sia.ohh1.model.Ohh1State;

import java.util.HashMap;
import java.util.Map;

public class Ohh1Heuristic implements Heuristic {

    private final static int FIRST_HEURISTIC = 1;
    private final static int SECOND_HEURISTIC = 2;

    private int heuristic;

    public Ohh1Heuristic(final int heuristic) {
        this.heuristic = heuristic;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(final int heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Integer getValue(final State state) {

        Ohh1State ohh1State = (Ohh1State) state;

        switch (heuristic) {
            case FIRST_HEURISTIC:
                return getValueFirstHeuristic(ohh1State);

            case SECOND_HEURISTIC:
                return getValueSecondHeuristic(ohh1State);

            default:
                return 0;
        }
    }

    private int getValueSecondHeuristic(final Ohh1State ohh1State) {
        int maxColsConsecutiveColors;
        int equalColorCountPerCol;
        int equalColsCount;
        int equalRowsCount;

        maxColsConsecutiveColors = getMaxColsConsecutiveColorsRestriction(ohh1State.getBoard());
        equalColorCountPerCol = checkEqualColorCountPerColRestriction(ohh1State.getBoard());
        equalColsCount = getEqualColsCount(ohh1State.getBoard());
        equalRowsCount = getEqualRowsCount(ohh1State.getBoard());

        return Math.max(maxColsConsecutiveColors, Math.max(equalColorCountPerCol, Math.max(equalColsCount,
                equalRowsCount)));
    }

    private int getValueFirstHeuristic(Ohh1State state) {
        return getCummulativeEqualRowsCount(state.getBoard()) + getColsRestrictionsCount(state.getBoard());
    }

    private int checkEqualColorCountPerColRestriction(final int[][] board) {

        int redCount;
        int blueCount;

        for (int col = 0; col < board.length; col++) {
            redCount = 0;
            blueCount = 0;

            for (int row = 0; row < board.length; row++) {
                if (board[row][col] == CellColor.RED.getValue()) {
                    redCount++;
                } else {
                    blueCount++;
                }
            }

            if (redCount != blueCount) {
                return 1;
            }
        }

        return 0;
    }

    private int getEqualColsCount(final int[][] board) {

        Map<Integer, Integer> colsMap = new HashMap<>();
        int equalColsCount = 0;
        StringBuilder colString;

        for (int col = 0; col < board.length; col++) {
            colString = new StringBuilder();

            for (int row = 0; row < board.length; row++) {
                colString.append(board[row][col]);
            }

            if (colsMap.containsKey(colString.toString().hashCode())) {
                colsMap.put(colString.toString().hashCode(), colsMap.get(colString.toString().hashCode()) + 1);
            } else {
                colsMap.put(colString.toString().hashCode(), 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : colsMap.entrySet()) {
            equalColsCount += entry.getValue() - 1;
        }

        return equalColsCount;
    }

    private int getEqualRowsCount(final int[][] board) {

        Map<Integer, Integer> rowsMap = new HashMap<>();
        int equalRowsCount = 0;
        StringBuilder rowString;

        for (int row = 0; row < board.length; row++) {
            rowString = new StringBuilder();

            for (int col = 0; col < board.length; col++) {
                rowString.append(board[row][col]);
            }

            if (rowsMap.containsKey(rowString.toString().hashCode())) {
                rowsMap.put(rowString.toString().hashCode(), rowsMap.get(rowString.toString().hashCode()) + 1);
            } else {
                rowsMap.put(rowString.toString().hashCode(), 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : rowsMap.entrySet()) {
            equalRowsCount += entry.getValue() - 1;
        }

        return equalRowsCount;
    }

    private int getMaxColsConsecutiveColorsRestriction(final int[][] board) {

        int consecutiveColorsCount;
        int auxConsecutiveColorsRestrictionCount;
        int maxConsecutiveColorsRestrictionCount = 0;

        for (int col = 0; col < board.length; col++) {
            consecutiveColorsCount = 0;
            auxConsecutiveColorsRestrictionCount = 0;

            for (int row = 0; row < board.length; row++) {
                if (board[row][col] == CellColor.RED.getValue()) {
                    if (consecutiveColorsCount < 0) {
                        consecutiveColorsCount = 0;
                    }
                    consecutiveColorsCount++;
                } else {
                    if (consecutiveColorsCount > 0) {
                        consecutiveColorsCount = 0;
                    }
                    consecutiveColorsCount--;
                }

                if (consecutiveColorsCount >= 3 || consecutiveColorsCount <= -3) {
                    consecutiveColorsCount = 0;
                    auxConsecutiveColorsRestrictionCount++;
                }
            }

            maxConsecutiveColorsRestrictionCount = Math.max(auxConsecutiveColorsRestrictionCount,
                    maxConsecutiveColorsRestrictionCount);
        }

        return maxConsecutiveColorsRestrictionCount;
    }

    private int getCummulativeEqualRowsCount(int[][] board) {

        int equalRowsCount = 0;
        boolean equals;

        for (int row = 0; row < board.length; row++) {
            for (int auxRow = row + 1; auxRow < board.length; auxRow++) {
                equals = true;
                for (int col = 0; col < board.length && equals; col++) {
                    if (board[row][col] != board[auxRow][col]) {
                        equals = false;
                    }
                }

                if (equals) {
                    equalRowsCount++;
                }
            }
        }

        return equalRowsCount;
    }

    private int getColsRestrictionsCount(int[][] board) {

        boolean equals;
        int equalColsCount = 0;
        int consecutiveColorsCount;
        int consecutiveColorsRestrictionCount = 0;
        int redCount;
        int blueCount;
        int equalColorCountSumRestricions = 0;

        for (int col = 0; col < board.length; col++) {
            for (int auxCol = col + 1; auxCol < board.length + 1; auxCol++) {
                equals = true;
                consecutiveColorsCount = 0;
                redCount = 0;
                blueCount = 0;
                for (int row = 0; row < board.length; row++) {
                    if (auxCol == board.length) {
                        equals = false;
                    } else {
                        if (equals && board[row][col] != board[row][auxCol]) {
                            equals = false;
                        }
                    }

                    if (col + 1 == auxCol) {
                        if (board[row][col] == CellColor.RED.getValue()) {
                            if (consecutiveColorsCount < 0) {
                                consecutiveColorsCount = 0;
                            }
                            consecutiveColorsCount++;
                            redCount++;
                        } else {
                            if (consecutiveColorsCount > 0) {
                                consecutiveColorsCount = 0;
                            }
                            consecutiveColorsCount--;
                            blueCount++;
                        }

                        if (consecutiveColorsCount >= 3 || consecutiveColorsCount <= -3) {
                            consecutiveColorsCount = 0;
                            consecutiveColorsRestrictionCount++;
                        }
                    }
                }

                if (redCount != blueCount) {
                    equalColorCountSumRestricions++;
                }

                if (equals) {
                    equalColsCount++;
                }
            }
        }

        return equalColsCount + consecutiveColorsRestrictionCount + equalColorCountSumRestricions;
    }
}
