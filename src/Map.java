/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.BorderLayout;

public class Map extends JFrame {
	private Tile[][] tiles = new Tile[8][8];
	private int turnNumber;
	private JLabel turnNumberLabel;

	public Map (String mapName) {
		super ("jCiv");
		this.turnNumber = 0;

		load (mapName);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout ());//using no layout managers

		JPanel turnPanel = new JPanel ();
		turnPanel.setLayout(new BorderLayout());

		turnNumberLabel = new JLabel ();
		setTurnLabel();

		JButton endTurnButton = new JButton ("End Turn");

		endTurnButton.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCiv.map.endTurn();
				JCiv.map.repaint();
			}
		});

		turnPanel.add (turnNumberLabel, BorderLayout.CENTER);
		turnPanel.add (endTurnButton, BorderLayout.EAST);

		JPanel mapPanel = new JPanel ();
		mapPanel.setLayout(new GridLayout(8,8));//using no layout managers  
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JButton button = tiles[i][j];

				mapPanel.add(button);//adding button in JFrame 

				button.addActionListener (new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Tile thisTile = ((Tile)e.getSource());

						if (thisTile.getCity() != null) {
							thisTile.getCity().displayCity();
						} else if (thisTile.getUnit() != null) {
							thisTile.getUnit().displayUnit();
						}
					}
				});
			}
		}

		add (turnPanel, BorderLayout.NORTH);
		add (mapPanel, BorderLayout.CENTER);
 
		setSize(600,600);//400 width and 500 height  
		setVisible(true);//making the frame visible  
	}

	public void load (String mapName) {
		String mapPath = "maps/" + mapName;
		System.out.println ("Reading map: " + mapPath);

		try(BufferedReader in = new BufferedReader(new FileReader(mapPath))) {
			String str;
			int y = 0;
			while ((str = in.readLine()) != null) {
				String[] tokens = str.split(",");

				for (int x = 0; x < tokens.length; x++) {
					Tile currentTile = null;

					switch (tokens[x]) {
						case "LAND":
							currentTile = new Land ();
							break;
						case "WATER":
							currentTile = new Water ();
							break;
					}
					this.tiles[x][y] = currentTile;
				}
				y++;
			}
		} catch (IOException e) {
			System.out.println("File Read Error");
		}
	}

	public String toString () {
		String stringMap = "";
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				char currentTile = ' ';
				if (tiles[x][y].getCity() != null) {
					currentTile = 'C';
				} else if (tiles[x][y].getUnit() != null) {
					switch (tiles[x][y].getUnit().getUnitType()) {
						case "SETTLER":
							currentTile = 'S';
							break;
					}
				} else {
					switch (tiles[x][y].getType()) {
						case "WATER":
							currentTile = 'W';
							break;
						case "LAND":
							currentTile = 'L';
							break;
					}
				}
				stringMap += currentTile + ",";
			}
			stringMap += "\n";
		}

		return stringMap;
	}

	public Tile getTile (String id) {
		// find the unit on the map
		for (int i = 0; i < JCiv.map.getTiles().length; i++) {
			for (int j = 0; j < JCiv.map.getTiles()[i].length; j++) {
				if (JCiv.map.getTile(i,j).getUnit() != null && JCiv.map.getTile(i,j).getUnit().getUnitID().equals(id) ||
					JCiv.map.getTile(i,j).getCity() != null && JCiv.map.getTile(i,j).getCity().getCityID().equals(id)) {
					return JCiv.map.getTile(i,j);
				}
			}
		}

		return null;
	}
	public Tile getTile (int x, int y) {
		return this.tiles[x][y];
	}
	public Tile[][] getTiles () {
		return tiles;
	}

	public boolean moveUnit (int x, int y, int newX, int newY) {
		if (
			tiles[x][y].getUnit().getMovesLeft() >= 1 &&		// if this unit a move left
			this.tiles[newX][newY].getType().equals("LAND") &&	// if this unit can move to the new tile
			this.tiles[newX][newY].getUnit() == null			// if the new tile does not have a unit already in it
		) {
				// remove a move from this unit
			tiles[x][y].getUnit().removeMove();

			// move the unit
			tiles[newX][newY].setUnit(tiles[x][y].getUnit());
			tiles[x][y].deleteUnit();

			return true;
		}

		return false;
	}

	public void addUnit (Unit unit, int x, int y) {
		tiles[x][y].setUnit(unit);
	}
	public void addCity (City city, int x, int y) {
		tiles[x][y].setCity(city);
		city.updateLabels();

		// covers start (turn 0) state where a unit might be in the city
		// TODO move to map init function why am i doing this every time i add a city if its a turn 0 only condition (validate this theory?)
		if (JCiv.map.getTile(x, y).getUnit() != null) {
			JCiv.map.getTile(x, y).getUnit().updateLabels();
		}
	}

	public void endTurn () {
		// init tiles
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if (tiles[x][y].getCity() != null) {
					tiles[x][y].getCity().endTurn (this, x, y);
				}
				if (tiles[x][y].getUnit() != null) {
					tiles[x][y].getUnit().resetMoves();
				}
			}
		}

		this.turnNumber++;
		setTurnLabel();
	}

	private void setTurnLabel () {
		this.turnNumberLabel.setText ("Turn: " + this.turnNumber);
	}

	public int getTurnNumber () {
		return this.turnNumber;
	}
}