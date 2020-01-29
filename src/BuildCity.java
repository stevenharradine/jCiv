/*
 * (c) 2020 Steven J Harradine
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.awt.event.WindowEvent;

public class BuildCity extends Action {
	public BuildCity (Unit newParentUnit) {
		this.setName ("Build City");
		this.setParentUnit (newParentUnit);
		setText ("Build city");
	}

	public boolean act () {
		BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
		Tile tile = JCiv.map.getTile(this.getParentUnit().getUnitID());

		TextInput cityNameInput = new TextInput ("Enter city name", "kanata", this.getParentUnit()); 
 		String cityName = cityNameInput.getInput();

 		// build the city
		tile.setCity (new City (cityName));

		// close unit window
		tile.getUnit().dispatchEvent(new WindowEvent(tile.getUnit(), WindowEvent.WINDOW_CLOSING));

		// destroy the settler
		tile.deleteUnit();

		return true;
	}
}