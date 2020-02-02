/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import java.awt.Color;

public class Land extends Tile {
	public Land () {
		this.setType ("LAND");
		this.setBackground (Color.GREEN);
	}
}