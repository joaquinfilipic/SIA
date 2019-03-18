package ohh1.model;

import gps.api.State;

import java.awt.Point;
import java.util.List;


public class Ohh1State implements State {

    private int[][] board;
    private int size;
    private List<Point> mandatoryMoves;

    public Ohh1State(final int[][] board, final int size, final List<Point> mandatoryMoves) {
        this.board = board;
        this.size = size;
        this.mandatoryMoves = mandatoryMoves;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(final int[][] board) {
        this.board = board;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
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