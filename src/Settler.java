/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

public class Settler extends Unit {
	public Settler () {
		this.setType ("SETTLER");
		this.setMoves(2);
		this.setProductionCost (10);

		Action[] actions = new Action[1];
		actions[0] = new BuildCity(this);
		this.setActions (actions);
	}
}