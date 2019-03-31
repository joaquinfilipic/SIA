package gps;

import gps.api.Heuristic;
import gps.api.Problem;
import gps.api.Rule;
import gps.api.State;
import ohh1.exception.RequestException;
import org.springframework.http.HttpStatus;

import java.util.*;

public class GPSEngine {

    Queue<GPSNode> open;

    Map<State, Integer> bestCosts;
    Problem problem;
    long explosionCounter;
    boolean finished;
    boolean failed;
    GPSNode solutionNode;
    Optional<Heuristic> heuristic;

    // Use this variable in open set order.
    protected SearchStrategy strategy;

    // For IDDFS algorithm.
    // TODO: check a better way to handle this.
    private int currentMaxDepth;
    private int iterativeDepth;
    private List<GPSNode> pendingNodesWithMaxDepthList;

    public GPSEngine(Problem problem, SearchStrategy strategy, Heuristic heuristic) {

        // TODO: open = *Su queue favorito, TENIENDO EN CUENTA EL ORDEN DE LOS NODOS*
        if (strategy == SearchStrategy.ASTAR || strategy == SearchStrategy.GREEDY) {
            open = new PriorityQueue(new HeuristicComparator());
        } else {
            open = new LinkedList<>();
        }

        bestCosts = new HashMap<>();
        this.problem = problem;
        this.strategy = strategy;
        this.heuristic = Optional.ofNullable(heuristic);
        explosionCounter = 0;
        finished = false;
        failed = false;

        currentMaxDepth = 0;
        iterativeDepth = 0;
        pendingNodesWithMaxDepthList = new LinkedList<>();
    }

    public void findSolution() {
        GPSNode rootNode = new GPSNode(problem.getInitState(), 0, null, 0);

        if (strategy == SearchStrategy.ASTAR || strategy == SearchStrategy.GREEDY) {
            if (!heuristic.isPresent()) {
                throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "HeuristicEnum not found");
            }

            State initialState = problem.getInitState();
            rootNode = new GPSNode(initialState, heuristic.get().getValue(initialState), null, 0);
        }

        open.add(rootNode);

        while (open.size() > 0) {

            GPSNode currentNode = open.remove();

            if (problem.isGoal(currentNode.getState())) {
                finished = true;
                solutionNode = currentNode;
                return;
            } else {
                if (strategy != SearchStrategy.IDDFS || currentNode.getDepth() < currentMaxDepth) {
                    explode(currentNode);
                } else if (currentNode.getDepth() >= currentMaxDepth) {
                    pendingNodesWithMaxDepthList.add(currentNode);
                }
            }

            if (open.size() == 0) {
                open = new LinkedList<>(pendingNodesWithMaxDepthList);
                pendingNodesWithMaxDepthList.clear();
                currentMaxDepth += iterativeDepth;
            }

            if (strategy == SearchStrategy.IDDFS && open.size() == 0) {

                // reset open nodes list
                open = new LinkedList<>();
                open.add(rootNode);

                currentMaxDepth++;
            } else if ((strategy == SearchStrategy.ASTAR || strategy == SearchStrategy.GREEDY)&& open.size() == 0) {

                open = new PriorityQueue<>(new HeuristicComparator());
                open.add(rootNode);

                currentMaxDepth++;
            }
        }
        failed = true;
        finished = true;
    }

    private void explode(GPSNode node) {

        Collection<GPSNode> newCandidates;
        switch (strategy) {
            case BFS:
                if (bestCosts.containsKey(node.getState())) {
                    return;
                }
                newCandidates = new ArrayList<>();
                addCandidates(node, newCandidates);

                // TODO: ¿Cómo se agregan los nodos a open en BFS?
                open.addAll(newCandidates);

                break;
            case DFS:
                if (bestCosts.containsKey(node.getState())) {
                    return;
                }
                newCandidates = new ArrayList<>();
                addCandidates(node, newCandidates);

                // TODO: ¿Cómo se agregan los nodos a open en DFS?
                LinkedList<GPSNode> dfsOpenList = (LinkedList<GPSNode>) open;

                for (GPSNode candidate : newCandidates) {
                    dfsOpenList.addFirst(candidate);
                }

                break;
            case IDDFS:
                // TODO: check if it is posible to implement this in iddfs algorithm.
                if (bestCosts.containsKey(node.getState())) {
                    return;
                }

                newCandidates = new ArrayList<>();
                addCandidates(node, newCandidates);

                // TODO: ¿Cómo se agregan los nodos a open en IDDFS?
                LinkedList<GPSNode> iddfsOpenList = (LinkedList<GPSNode>) open;

                for (GPSNode candidate : newCandidates) {
                    iddfsOpenList.addFirst(candidate);
                }

                break;
            case GREEDY:
                if (!isBest(node.getState(), node.getCost())) {
                    return;
                }
                bestCosts.clear();

                newCandidates = new PriorityQueue<>(new HeuristicComparator());
                addCandidates(node, newCandidates);

                PriorityQueue greedyPriorityQueue = (PriorityQueue) open;

                greedyPriorityQueue.clear();

                for (GPSNode candidate : newCandidates){
                    greedyPriorityQueue.add(candidate);
                }

                printOpen(greedyPriorityQueue);

                // TODO: ¿Cómo se agregan los nodos a open en GREEDY?
                break;
            case ASTAR:
                if (!isBest(node.getState(), node.getCost())) {
                    return;
                }

                newCandidates = new PriorityQueue<>(new HeuristicComparator());
                addCandidates(node, newCandidates);

                PriorityQueue aStarPriorityQueue = (PriorityQueue) open;

                for (GPSNode candidate : newCandidates) {
                    aStarPriorityQueue.add(candidate);
                }

                // TODO: ¿Cómo se agregan los nodos a open en A*?
                break;
        }
    }

    private void addCandidates(GPSNode node, Collection<GPSNode> candidates) {
        explosionCounter++;
        updateBest(node);
        for (Rule rule : problem.getRules()) {
            Optional<State> newState = rule.apply(node.getState());
            if (newState.isPresent()) {
                GPSNode newNode;

                if (strategy == SearchStrategy.ASTAR || strategy == SearchStrategy.GREEDY) {
                    newNode = new GPSNode(newState.get(),
                            node.getCost() + rule.getCost() + heuristic.get().getValue(newState.get()), rule,
                            node.getDepth() + 1);
                } else {
                    newNode = new GPSNode(newState.get(), node.getCost() + rule.getCost(), rule,
                            node.getDepth() + 1);
                }

                newNode.setParent(node);

                candidates.add(newNode);
            }
        }
    }

    public int getIterativeDepth() {
        return iterativeDepth;
    }

    public void setIterativeDepth(final int iterativeDepth) {
        this.iterativeDepth = iterativeDepth;
    }

    private static class HeuristicComparator implements Comparator<GPSNode> {

        @Override
        public int compare(final GPSNode node1, final GPSNode node2) {
            return node1.getCost() - node2.getCost();
        }
    }

    // TODO: Delete this code
    private void printOpen(Queue<GPSNode> list) {

        System.out.println("+--------------------------------+");
        System.out.println("| Printing list of nodes to open |");
        System.out.println("+--------------------------------+");
        for (GPSNode node : list) {

            System.out.println(node.getState().getRepresentation());

        }
    }

    private boolean isBest(State state, Integer cost) {
        return !bestCosts.containsKey(state) || cost < bestCosts.get(state);
    }

    private void updateBest(GPSNode node) {
        bestCosts.put(node.getState(), node.getCost());
    }

    // GETTERS FOR THE PEOPLE!

    public Queue<GPSNode> getOpen() {
        return open;
    }

    public Map<State, Integer> getBestCosts() {
        return bestCosts;
    }

    public Problem getProblem() {
        return problem;
    }

    public long getExplosionCounter() {
        return explosionCounter;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isFailed() {
        return failed;
    }

    public GPSNode getSolutionNode() {
        return solutionNode;
    }

    public SearchStrategy getStrategy() {
        return strategy;
    }

    public int getCurrentMaxDepth() {
        return currentMaxDepth;
    }

    public void setCurrentMaxDepth(final int currentMaxDepth) {
        this.currentMaxDepth = currentMaxDepth;
    }
}
