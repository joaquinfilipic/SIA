package ohh1.logic;

import gps.api.Heuristic;
import gps.api.State;
import ohh1.model.CellColor;
import ohh1.model.Ohh1State;

public class Ohh1Heuristic implements Heuristic {

    @Override
    public Integer getValue(final State state) {

        Ohh1State ohh1State = (Ohh1State) state;
        int restrictionsCount = 0;

        restrictionsCount += getEqualRowsCount(ohh1State.getBoard());
        restrictionsCount += getColsRestrictionsCount(ohh1State.getBoard());

        return restrictionsCount;
    }

    private int getEqualRowsCount(int[][] board) {

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

                        if (consecutiveColorsCount % 3 == 0) {
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

    // TODO: Remove, only for testing
    private void printBoard(int[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }
}
