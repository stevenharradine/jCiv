/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import java.io.InputStreamReader;

class JCiv {
	public static Map map;

    public static void main(String[] args) {
    	map = new Map ("circle.map");

		map.addUnit(new Settler(), 1, 2);
		map.addUnit(new Settler(), 2, 3);

		City city = new City ("Test City");

    	map.addCity(city, 1, 2);
    }

	public static String generateRandomID () {
		final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int count = 8; // 2.8T options (2.8211099e+12)
		StringBuilder builder = new StringBuilder();
		
		while (count-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}

		// TODO: Make sure the id is uniqe on the map before actually returning
		return builder.toString();
	}
}
