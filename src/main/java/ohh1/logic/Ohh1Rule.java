package ohh1.logic;

import java.util.Optional;

import gps.api.Rule;
import gps.api.State;
import ohh1.model.CellColor;
import ohh1.model.Ohh1State;
import ohh1.model.Point;

public class Ohh1Rule implements Rule {
	
	private Point point;
	private CellColor color;
	
	public Ohh1Rule(final Point point, final CellColor color) {
		this.point = point;
		this.color = color;
	}

	@Override
	public Integer getCost() {
		return 1;
	}

	@Override
	public String getName() {
		return ("Rule: paint cell " + point.toString() + " with color " + color.getName());
	}

	@Override
	public Optional<State> apply(State state) {
		
		if (Ohh1RuleValidator.isValid((Ohh1State)state, this)) {
			
			// TODO: paint cell
			
		}
		return null;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public CellColor getColor() {
		return color;
	}

	public void setColor(CellColor color) {
		this.color = color;
	}

}
