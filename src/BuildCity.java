/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import java.awt.event.WindowEvent;

public class BuildCity extends Action {
	public BuildCity (Unit newParentUnit) {
		this.setName ("Build City");
		this.setParentUnit (newParentUnit);
		setText ("Build city");
	}

	public boolean act () {
		Tile tile = JCiv.map.getTile(this.getParentUnit().getUnitID());
		String cityName = null;
		boolean isCityNameExist = false;

		do {
			String title = isCityNameExist ? "Error: City " + cityName + " already exists" : "New city";
			TextInput cityNameInput = new TextInput (title, "Enter city name" , "kanata", this.getParentUnit()); 
	 		cityName = cityNameInput.getInput();

	 		isCityNameExist = JCiv.map.isCityNameExist(cityName);
	 	} while (isCityNameExist);

 		// build the city
		tile.setCity (new City (cityName));

		// close unit window
		tile.getUnit().dispatchEvent(new WindowEvent(tile.getUnit(), WindowEvent.WINDOW_CLOSING));

		// destroy the settler
		tile.deleteUnit();

		return true;
	}

	public void updateLabels () {
		Tile tile = JCiv.map.getTile(this.getParentUnit().getUnitID());
		City city = tile.getCity();

		this.setEnabled(city == null ? true : false);
	}
}