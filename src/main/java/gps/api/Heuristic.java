package gps.api;

public interface Heuristic {


    /**
     * Computes the value of the HeuristicEnum for the given state.
     *
     * @param state
     *            The state where the HeuristicEnum should be computed.
     * @return The value of the HeuristicEnum.
     */
    Integer getValue(State state);
}
