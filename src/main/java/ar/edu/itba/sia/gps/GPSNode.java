package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

public class GPSNode {
	
	private State state;

	private GPSNode parent;

	private Integer cost;

	private Rule generationRule;
	
	private int depth;
	
	private int hValue;

	public GPSNode(State state, Integer cost, Rule generationRule, int depth, int hValue) {
		this.state = state;
		this.cost = cost;
		this.generationRule = generationRule;
		this.depth = depth;
		this.hValue = hValue;
	}

	public GPSNode getParent() {
		return parent;
	}

	public void setParent(GPSNode parent) {
		this.parent = parent;
	}

	public State getState() {
		return state;
	}

	public Integer getCost() {
		return cost;
	}
	
	public Rule getGenerationRule() {
		return generationRule;
	}

	public void setGenerationRule(Rule generationRule) {
		this.generationRule = generationRule;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int gethValue() {
		return hValue;
	}

	public void sethValue(int hValue) {
		this.hValue = hValue;
	}

	//TODO: check this method.
	@Override
	public String toString() {
		return state.toString();
	}

	public String getSolution() {
		if (this.parent == null) {
			return this.state.getRepresentation();
		}
		return this.parent.getSolution() + this.state.getRepresentation();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GPSNode other = (GPSNode) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

}
