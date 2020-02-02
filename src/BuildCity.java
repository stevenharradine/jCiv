/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

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
			String recommendedCityName = getRecommendedCityName();
			TextInput cityNameInput = new TextInput (title, "Enter city name" , recommendedCityName, this.getParentUnit());

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

	public String getRecommendedCityName () {
		String str;
		List<String> list = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader("data/citylist.txt"));
			while((str = in.readLine()) != null){
				list.add(str);
			}
		} catch (Exception e) {
			System.out.println (e);
		}

		String[] stringArr = list.toArray(new String[0]);

		for (int i = 0; i < stringArr.length; i++) {
			boolean isCityNameUnique = !JCiv.map.isCityNameExist(stringArr[i]);

			if (isCityNameUnique) {
				return stringArr[i];
			}

		}

		return "";
	}

	public void updateLabels () {
		Tile tile = JCiv.map.getTile(this.getParentUnit().getUnitID());
		City city = tile.getCity();

		this.setEnabled(city == null ? true : false);
	}
}