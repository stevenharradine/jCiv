/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

public class BuildFarm extends Action {
	public BuildFarm (Unit newParentUnit) {
		this.setName ("Build farm");
		this.setParentUnit (newParentUnit);
		setText ("Build farm");
	}

	public boolean act () {
		Tile tile = JCiv.map.getTile(this.getParentUnit().getUnitID());

		tile.setEnhancement ("farm");

		this.updateLabels();

		return true;
	}

	public void updateLabels () {
		Tile tile = JCiv.map.getTile(this.getParentUnit().getUnitID());
		String enhancement = tile.getEnhancement();

		this.setEnabled(enhancement == null ? true : ("farm".equals(enhancement) ? false : true) );
	}
}