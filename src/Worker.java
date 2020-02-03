/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

public class Worker extends Unit {
	public Worker () {
		this.setType ("WORKER");
		this.setMoves(2);
		this.setProductionCost (5);

		Action[] actions = new Action[1];
		actions[0] = new BuildFarm (this);
		this.setActions (actions);
	}
}