package ar.edu.itba.sia.gps;

import ohh1.exception.RequestException;
import org.springframework.http.HttpStatus;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

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
	private Integer analyzedStates = 0;

	// Use this variable in open set order.
	protected SearchStrategy strategy;

	// For IDDFS algorithm.
	private int currentMaxDepth;
	private boolean areUnexploitedNodes;

	public GPSEngine(Problem problem, SearchStrategy strategy, Heuristic heuristic) {

		validateParams(strategy, heuristic);

		open = initializeOpenQueue(open, strategy);

		bestCosts = new HashMap<>();
		this.problem = problem;
		this.strategy = strategy;
		this.heuristic = Optional.ofNullable(heuristic);
		explosionCounter = 0;
		finished = false;
		failed = false;

		currentMaxDepth = 2;
		areUnexploitedNodes = false;
	}

	public void findSolution() {

		GPSNode rootNode = getRootNode(problem, strategy, heuristic);
		open.add(rootNode);

		while (open.size() > 0) {

			GPSNode currentNode = open.remove();
			analyzedStates++;

			if (problem.isGoal(currentNode.getState())) {

				finished = true;
				solutionNode = currentNode;
				return;

			} else if (strategy != SearchStrategy.IDDFS || currentNode.getDepth() < currentMaxDepth) {

				explode(currentNode);

			} else {

				if (!bestCosts.containsKey(currentNode.getState())) {
					areUnexploitedNodes = true;
				}

			}

			if (strategy == SearchStrategy.IDDFS && open.size() == 0) {

				if (!areUnexploitedNodes) {
					failed = true;
					finished = true;
					return;
				}

				open = new LinkedList<GPSNode>();
				open.add(rootNode);
				bestCosts.clear();
				areUnexploitedNodes = false;

				currentMaxDepth += currentMaxDepth;
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

			open.addAll(newCandidates);

			break;
		case DFS:
			if (bestCosts.containsKey(node.getState())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);

			LinkedList<GPSNode> dfsOpenList = (LinkedList<GPSNode>) open;

			for (GPSNode candidate : newCandidates) {
				dfsOpenList.addFirst(candidate);
			}

			break;
		case IDDFS:
			if (bestCosts.containsKey(node.getState())) {
				return;
			}

			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);

			LinkedList<GPSNode> iddfsOpenList = (LinkedList<GPSNode>) open;

			for (GPSNode candidate : newCandidates) {
				iddfsOpenList.addFirst(candidate);
			}

			break;
		case GREEDY:
			if (bestCosts.containsKey(node.getState())) {
				return;
			}

			newCandidates = new PriorityQueue<>(new HeuristicComparator());
			addCandidates(node, newCandidates);

			LinkedList<GPSNode> greedyOpenList = (LinkedList<GPSNode>) open;

			for (GPSNode candidate : newCandidates) {
				greedyOpenList.addFirst(candidate);
			}

			break;
		case ASTAR:
			if (!isBest(node.getState(), node.getCost())) {
				return;
			}

			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);

			PriorityQueue<GPSNode> aStarPriorityQueue = (PriorityQueue<GPSNode>) open;

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
				GPSNode newNode = getInnerNode(newState.get(), node, rule, strategy, heuristic);
				candidates.add(newNode);
			}
		}
	}

	private Queue<GPSNode> initializeOpenQueue(Queue<GPSNode> open, SearchStrategy strategy) {
		if (strategy == SearchStrategy.ASTAR) {
			open = new PriorityQueue<>(new HeuristicComparator());
		} else {
			open = new LinkedList<>();
		}
		return open;
	}

	private void validateParams(SearchStrategy strategy, Heuristic heuristic) {
		if (strategy == SearchStrategy.ASTAR || strategy == SearchStrategy.GREEDY) {
			if (heuristic == null) {
				throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "HeuristicEnum not found");
			}
		}
	}

	private GPSNode getRootNode(Problem problem, SearchStrategy strategy, Optional<Heuristic> heuristic) {
		if (strategy == SearchStrategy.ASTAR || strategy == SearchStrategy.GREEDY) {
			return new GPSNode(problem.getInitState(), 0, null, 0, heuristic.get().getValue(problem.getInitState()));
		}
		return new GPSNode(problem.getInitState(), 0, null, 0, 0);
	}

	private GPSNode getInnerNode(State state, GPSNode parent, Rule rule, SearchStrategy strategy, Optional<Heuristic> heuristic) {
		GPSNode newNode;
		if (strategy == SearchStrategy.ASTAR || strategy == SearchStrategy.GREEDY) {
			newNode = new GPSNode(state, parent.getCost() + rule.getCost(), rule, parent.getDepth() + 1,
					heuristic.get().getValue(state));
		} else {
			newNode = new GPSNode(state, parent.getCost() + rule.getCost(), rule, parent.getDepth() + 1, 0);
		}
		newNode.setParent(parent);
		return newNode;
	}

	private static class HeuristicComparator implements Comparator<GPSNode> {
		@Override
		public int compare(final GPSNode node1, final GPSNode node2) {
			return (node1.getCost() + node1.gethValue()) - (node2.getCost() + node2.gethValue());
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

	public Integer getAnalyzedStates() {
		return this.analyzedStates;
	}
}
