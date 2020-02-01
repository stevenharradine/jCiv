/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

public class CityBuildQueue {
	private Unit unit;
	private int production;

	public CityBuildQueue () {
		this.unit = null;
		this.production = 0;
	}

	public void setUnit (Unit newUnit) {
		this.unit = newUnit;
		this.production = 0;
	}
	public void deleteUnit () {
		this.unit = null;
		this.production = 0;
	}
	public Unit getUnit () {
		return this.unit;
	}
	public int getProduction () {
		return this.production;
	}
	public void addProduction (int newProduction) {
		this.production += newProduction;
	}

	public String toString () {
		if (this.unit != null) {
			return this.unit.getUnitType() + " (Production " + this.production + " / " + this.unit.getProductionCost() + ")";
		} else {
			return "Empty";
		}
	}
}