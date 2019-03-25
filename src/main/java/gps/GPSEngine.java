package gps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

import gps.api.Heuristic;
import gps.api.Problem;
import gps.api.Rule;
import gps.api.State;

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
	
	public GPSEngine(Problem problem, SearchStrategy strategy, Heuristic heuristic) {
		
		// TODO: open = *Su queue favorito, TENIENDO EN CUENTA EL ORDEN DE LOS NODOS*
		open = new LinkedList<>();
				
		bestCosts = new HashMap<>();
		this.problem = problem;
		this.strategy = strategy;
		this.heuristic = Optional.ofNullable(heuristic);
		explosionCounter = 0;
		finished = false;
		failed = false;
		
		currentMaxDepth = 0;
	}

	public void findSolution() {
		GPSNode rootNode = new GPSNode(problem.getInitState(), 0, null, 0);
		open.add(rootNode);
		
		while (open.size() > 0) {
									
			GPSNode currentNode = open.remove();
						
			if (problem.isGoal(currentNode.getState())) {
				finished = true;
				solutionNode = currentNode;
				return;
			} 
			
			else {
				if (strategy != SearchStrategy.IDDFS || currentNode.getDepth() < currentMaxDepth) {
					explode(currentNode);
				}
			}
			
			if (strategy == SearchStrategy.IDDFS && open.size() == 0) {
				
				// reset open nodes list
				open = new LinkedList<>();
				open.add(rootNode);
				
				currentMaxDepth ++;
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
			for (GPSNode candidate: newCandidates) {
				open.add(candidate);
			}
			
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
//			if (bestCosts.containsKey(node.getState())) {
//				return;
//			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			
			// TODO: ¿Cómo se agregan los nodos a open en IDDFS?
			LinkedList<GPSNode> iddfsOpenList = (LinkedList<GPSNode>) open;
			
			for (GPSNode candidate : newCandidates) {
				iddfsOpenList.addFirst(candidate);
			}
			
			break;
		case GREEDY:
			newCandidates = new PriorityQueue<>(/* TODO: Comparator! */);
			addCandidates(node, newCandidates);
			// TODO: ¿Cómo se agregan los nodos a open en GREEDY?
			break;
		case ASTAR:
			if (!isBest(node.getState(), node.getCost())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
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
				GPSNode newNode = new GPSNode(newState.get(), node.getCost() + rule.getCost(), rule, node.getDepth() + 1);
				newNode.setParent(node);
				
				candidates.add(newNode);
			}
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

}
