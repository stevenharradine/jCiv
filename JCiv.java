/*
 * (c) 2020 Steven J Harradine
 */
import java.io.BufferedReader;
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
/*
    private static void menu_WhatToDo () {
	    boolean inputInvalid;

		System.out.println (map.toString());

    	do {
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    		inputInvalid = false;	// assume input is good and prove otherwise

	    	System.out.println ("What to do? (Turn: " + map.getTurnNumber() + ")");
	    	System.out.println ("1) Select a city ");
	    	System.out.println ("2) Select a unit ");
	    	System.out.println ("3) End turn");
	    	System.out.println ("0) Quit");
	        
	        try {
	            int input = Integer.parseInt(br.readLine());

	            switch (input) {
	            	case 1:
	            		menu_SelectCity();
	            		break;
	            	case 2:
	            		menu_SelectUnit();
	            		break;
	            	case 3:
	            		map.endTurn();
	            		menu_WhatToDo ();
	            		break;
	            	case 0:
	            		// exit
	            		break;
	            	default:
	            		inputInvalid = true; 
	            }
	        } catch (Exception e) {
				System.out.println (e.toString());
				inputInvalid = true; 	// repeat the menu option instead of erroring out
	        }
	    } while (inputInvalid);

    }

    private static void menu_SelectUnit() {
	    boolean inputInvalid;
    	do {
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        int x = -1;
	        int y = -1;
    		inputInvalid = false;	// assume input is good and prove otherwise

	    	do {
		    	System.out.println ("Select a unit (enter coordinates): ");
		    	System.out.print ("x: ");
		    	try {
		    		x = Integer.parseInt(br.readLine());
		    	} catch (Exception e) {
		    		System.out.println (e.getMessage());
		    	}
	    	} while (x < 0 || x >= 8); 
	    	
	    	do {
		    	System.out.print ("y: ");
		    	try {
		    		y = Integer.parseInt(br.readLine());
		    	} catch (Exception e) {
		    		System.out.println (e.getMessage());
		    	}
	    	} while (y < 0 || y >= 8);

	    	if (map.getTile(x, y).getUnit() != null) {
	    		menu_UnitOptions(x,y);
	    	} else {
	    		System.out.println ("no unit there");
	    		menu_WhatToDo();
	    	}
	    } while (inputInvalid);    	
    }

    private static void menu_SelectCity() {
	    boolean inputInvalid;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int x = -1;
        int y = -1;
    	do {
    		inputInvalid = false;	// assume input is good and prove otherwise

	    	do {
		    	System.out.println ("Select a city (enter coordinates): ");
		    	System.out.print ("x: ");
		    	try {
		    		x = Integer.parseInt(br.readLine());
		    	} catch (Exception e) {
		    		System.out.println (e.getMessage());
		    	}
	    	} while (x < 0 || x >= 8); 
	    	
	    	do {
		    	System.out.print ("y: ");
		    	try {
		    		y = Integer.parseInt(br.readLine());
		    	} catch (Exception e) {
		    		System.out.println (e.getMessage());
		    	}
	    	} while (y < 0 || y >= 8);
	    } while (inputInvalid); 

    	if (map.getTile(x, y).getCity() != null) {
    		menu_CityOptions(x,y);
    	} else {
    		System.out.println ("no city there");
    		menu_WhatToDo();
    	}
    }

    public static void menu_UnitOptions (int x, int y) {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  
    	System.out.println ("Unit: " + map.getTile(x, y).getUnit().getUnitType());
    	System.out.println ("Moves available: " + map.getTile(x, y).getUnit().getMovesLeft() + " / " + map.getTile(x, y).getUnit().getMoves());

    	int input = -1;
    	do {
	    	System.out.println ("Make this unit");
	    	System.out.println ("1) Move");
	    	System.out.println ("2) Perform action");
	    	try {
	    		input = Integer.parseInt(br.readLine());
	    	} catch (Exception e) {
	    		System.out.println (e.getMessage());
	    	}
	    } while (!(input == 1 || input == 2));

	    if (input == 1) {
	    	menu_MoveUnit(x, y);
	    } else if (input == 2) {
	    	menu_SelectAction(x, y);
	    }
    }

    public static void menu_CityOptions (int x, int y) {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	int input = -1;
    	String cityBuildQueue = map.getTile(x, y).getCity().getCityBuildQueue().getUnit() != null ? map.getTile(x, y).getCity().getCityBuildQueue().toString() : "Empty";
    	String stationedUnit = map.getTile(x, y).getUnit() != null ? map.getTile(x, y).getUnit().toString() : "None";
  
    	System.out.println ();
    	System.out.println ("City: " + map.getTile(x, y).getCity().getName());
    	System.out.println ("Build queue: " + cityBuildQueue);
    	System.out.println ("Stationed Unit: " + stationedUnit);
    	System.out.println ("Citizens: " + map.getTile(x, y).getCity().getCitizens().length + " (" + Citizen.sumFood(map.getTile(x, y).getCity().getCitizens()) + " / " + (5 * map.getTile(x, y).getCity().getCitizens().length) + ")");

    	do {
	    	System.out.println ();
	    	System.out.println ("City options:");
	    	System.out.println ("1) Change build queue");
	    	System.out.println ("2) View citizens");
	    	System.out.println ("0) Exit city");

	    	try {
	    		input = Integer.parseInt(br.readLine());
	    	} catch (Exception e) {
	    		System.out.println (e.getMessage());
	    	}
	    } while (input < 0 || input > 2);

	    if (input == 1) {
	    	menu_ChangeBuildQueue (x, y);
	    } else if (input == 2) {
	    	menu_ViewCitizens (x, y);
	    } else if (input == 0) {
			menu_WhatToDo();
	    }
    }

    public static void menu_ViewCitizens (int x, int y) {
    	City thisCity = map.getTile(x, y).getCity();
    	Citizen[] citizens = thisCity.getCitizens();
    	int numberOfCitizens = citizens.length;

    	System.out.println ();
    	System.out.println ("City: " + thisCity.getName());
    	System.out.println ("Citizens: " + numberOfCitizens + " (" + Citizen.sumFood(citizens) + " / " + (5 * numberOfCitizens) + ")");
    	System.out.println ();
    	for (int i = 0; i < numberOfCitizens; i++) {
    		System.out.println ((i+1) + ") " + citizens[i].getFood());
    	}

    	menu_CityOptions (x, y);
    }

    public static void menu_ChangeBuildQueue (int x, int y) {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	int input = -1;
    	String cityBuildQueue = map.getTile(x, y).getCity().getCityBuildQueue().getUnit() != null ? map.getTile(x, y).getCity().getCityBuildQueue().toString() : "Empty";
  
    	System.out.println ();
    	System.out.println ("City: " + map.getTile(x, y).getCity().getName());
    	System.out.println ("Build queue: " + cityBuildQueue);

		int numberOfUnits = Unit.UNITS_AVAILABLE.length;
    	do {
	    	System.out.println ("Select a unit to build");
	    	for (int i = 0; i < numberOfUnits; i++) {
	    		System.out.println ((i + 1) + ") " + Unit.UNITS_AVAILABLE[i].getUnitType());
	    	}
	    	System.out.println ("0) cancel");

	    	try {
	    		input = Integer.parseInt(br.readLine());
	    	} catch (Exception e) {
	    		System.out.println (e.getMessage());
	    	}
	    } while (input < 0 || input > numberOfUnits);

	    if (input == 0) {
			menu_CityOptions (x, y);
	    } else {
	    	map.getTile(x, y).getCity().getCityBuildQueue().setUnit (Unit.UNITS_AVAILABLE[input - 1]);

	    	menu_CityOptions (x, y);
	    }
    }

    public static void menu_SelectAction (int x, int y) {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	int input = -1;
		Action[] actions = map.getTile(x, y).getUnit().getActions();
		int numberOfActions = actions.length;
    	do {
	    	System.out.println ("Select action");
	    	for (int i = 0; i < numberOfActions; i++) {
	    		System.out.println ((i + 1) + ") " + actions[i].getName());
	    	}
	    	System.out.println ("0) cancel");

	    	try {
	    		input = Integer.parseInt(br.readLine());
	    	} catch (Exception e) {
	    		System.out.println (e.getMessage());
	    	}
	    } while (input < 0 || input > numberOfActions);

	    if (input != 0) {
	    	actions[input - 1].act();
	    }
	    menu_WhatToDo();
    }

    public static void menu_MoveUnit (int x, int y) {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	int input = -1;
    	do {
	    	System.out.println ("Move " + map.getTile(x, y).getUnit());
	    	System.out.println ("7 8 9");
	    	System.out.println ("4 + 6");
	    	System.out.println ("1 2 3");
	    	try {
	    		input = Integer.parseInt(br.readLine());
	    	} catch (Exception e) {
	    		System.out.println (e.getMessage());
	    	}
	    } while (input >= 10 || input <= 0 || input == 5);

//TODO: move to map class
	    if (input == 9) {			// move up/right
	    	map.moveUnit(x, y, x - 1, y + 1);
	    } else if (input == 8) {	// move up
	    	map.moveUnit(x, y, x - 1, y);
	    } else if (input == 7) {	// move up/left
	    	map.moveUnit(x, y, x - 1, y - 1);
	    } else if (input == 4) {	// move left
	    	map.moveUnit(x, y, x, y - 1);
	    } else if (input == 6) {	// move right
	    	map.moveUnit(x, y, x, y + 1);
	    } else if (input == 3) {	// move down/right
	    	map.moveUnit(x, y, x + 1, y + 1);
	    } else if (input == 2) {	// move down
	    	map.moveUnit(x, y, x + 1, y);
	    } else if (input == 1) {	// move down/left
	    	map.moveUnit(x, y, x + 1, y - 1);
	    }

	    menu_WhatToDo ();
    }*/
}
