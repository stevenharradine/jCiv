/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import java.awt.event.WindowEvent;

public class BuildFarm extends Action {
	public BuildFarm (Unit newParentUnit) {
		this.setName ("Build farm");
		this.setParentUnit (newParentUnit);
		setText ("Build farm");
	}

	public boolean act () {
		// TODO: Build farm
		
		return true;
	}

	public void updateLabels () {
		Tile tile = JCiv.map.getTile(this.getParentUnit().getUnitID());
		City city = tile.getCity();

		this.setEnabled(city == null ? true : false);
	}
}