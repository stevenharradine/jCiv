/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.GridLayout;
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.awt.Image;

public class City extends JFrame {
	private String name;
	private CityBuildQueue cityBuildQueue;
	private Citizen[] population;
	private String cityID;

	private JLabel stationedUnitLabel;
	private JButton stationedUnitOpenButton;
	private JLabel populationLabel;
	private JLabel buildQueueLabel;

	public City (String newName) {
		super ();
		setLayout (new GridLayout (0, 1));

		this.cityID = JCiv.generateRandomID();
		this.name = newName;
		this.cityBuildQueue = new CityBuildQueue ();
		this.population = new Citizen[0];
		this.population = Citizen.addCitizen(population);

		stationedUnitLabel = new JLabel ();
		populationLabel = new JLabel ();
		buildQueueLabel = new JLabel ();

		setTitle("City: " + this.name + " |"+ this.cityID);
		add (new JLabel ("City name: " + this.name + "(" + this.cityID + ")"));

		JPanel stationedUnitPanel = new JPanel ();
		stationedUnitPanel.setLayout (new BorderLayout());

		stationedUnitOpenButton = new JButton ("Open");
		stationedUnitOpenButton.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = ((JFrame)((JButton)e.getSource()).getParent().getParent().getParent().getParent().getParent()).getTitle();
				String[] titleParts = title.split ("\\|");
				String cityID = titleParts[1];

				JCiv.map.getTile(cityID).getUnit().setVisible(true);
			}
		});

		stationedUnitPanel.add (stationedUnitLabel, BorderLayout.CENTER);
		stationedUnitPanel.add (stationedUnitOpenButton, BorderLayout.EAST);

		add (stationedUnitPanel);
		add (this.populationLabel);

		JButton changeBuildQueueButton = new JButton ("Change");
		changeBuildQueueButton.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = ((JFrame)((JButton)e.getSource()).getParent().getParent().getParent().getParent().getParent()).getTitle();
				String[] titleParts = title.split ("\\|");
				String cityID = titleParts[1];

				// build options of avilable units
		        Object[] options = new Object[Unit.UNITS_AVAILABLE.length];
		        for (int i = 0; i < options.length; i++) {
		        	options[i] = Unit.toString(Unit.UNITS_AVAILABLE[i]);
		        }

		        // show the dialog box
				String inputText = (String)JOptionPane.showInputDialog(
					JCiv.map.getTile (cityID).getCity(),
					"Change build queue too",
					"Customized Dialog",
					JOptionPane.PLAIN_MESSAGE,
					null,
					options,
					null
				);

				// find the selected unit and add it to the build queue (null does not equal anything and does not change the queue)
		        for (int i = 0; i < options.length; i++) {
		        	if (Unit.toString(Unit.UNITS_AVAILABLE[i]).equals(inputText)) {
						JCiv.map.getTile (cityID).getCity().getCityBuildQueue().setUnit(Unit.UNITS_AVAILABLE[i]);
		        	}
		        }
		        updateBuildQueueLabel();
			}
		});

		JPanel buildPanel = new JPanel ();
		buildPanel.setLayout (new BorderLayout ());
		buildPanel.add (buildQueueLabel, BorderLayout.CENTER);
		buildPanel.add (changeBuildQueueButton, BorderLayout.EAST);

		add (buildPanel);

		updateLabels();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300,500);//400 width and 500 height  
	}

	public Image getIcon () {
		try {
			Image img = ImageIO.read(new FileInputStream("graphics/village.png"));

			return img;
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}

	public void displayCity () {
		this.repaint();
		this.setVisible(true);//making the frame visible 
	}

	public String getName () {
		return this.name;
	}
	public CityBuildQueue getCityBuildQueue () {
		return this.cityBuildQueue;
	}
	public String getCityID () {
		return this.cityID;
	}
	public Citizen[] getCitizens () {
		return this.population;
	}
	public String toString () {
		return "City: " + this.name + " (" + this.population.length + ")";
	}

	public void endTurn (Map map, int x, int y) {
		this.cityBuildQueue.addProduction(1);

		// feed the people (least food first, ensure equitable distribution)
		int foodPerTurn = 3;
		do {
			int leastFood = Integer.MAX_VALUE;
			int leastFoodIndex = 0;

			for (int i = 0; i < population.length && foodPerTurn > 0; i++) {
				if (population[i].getFood() < leastFood) {
					leastFood = population[i].getFood();
					leastFoodIndex = i;
				}
			}

			population[leastFoodIndex].addFood(1);
			foodPerTurn--;
		} while (foodPerTurn > 0);

		// if all the people are full add a citizen
		boolean isEveryoneFull = true;
		for (int i = 0; i < population.length; i++) {
			if (population[i].getFood() < 5) {
				isEveryoneFull = false;
			}
		}

		if (isEveryoneFull) {
			population = Citizen.addCitizen(population);
		}

		// if the build queue is complete
		if (this.cityBuildQueue.getUnit() != null && this.cityBuildQueue.getProduction() >= this.cityBuildQueue.getUnit().getProductionCost()) {
			Tile curTile = map.getTile(x, y);
			if (curTile.hasUnit()) {
				map.getTile(x, y).setUnit (cityBuildQueue.getUnit());
				cityBuildQueue.deleteUnit();
			}
		}

		// update labels
		updateLabels();
	}

	public void updateLabels () {
		updateStationedUnitLabel();
		updatePopulationLabel();
		updateBuildQueueLabel();
	}

	public void updatePopulationLabel () {
		String buffer = "<html>";

		buffer += "Population: " + population.length + "<br>";

		for (int i = 0; i < this.population.length; i++) {
			buffer += "&nbsp;&nbsp;&nbsp;&nbsp;";
			buffer += "Person " + (i + 1) + " has " + population[i].getFood() + " food<br>";
		}

		buffer += "</html>";

		this.populationLabel.setText (buffer);
	}

	public void updateBuildQueueLabel () {
		String buffer = "<html>";

		buffer += "Build queue: " + this.cityBuildQueue.toString();

		buffer += "</html>";

		this.buildQueueLabel.setText (buffer);
	}

	public void updateStationedUnitLabel () {
		String buffer = "<html>";

		buffer += "Stationed unit: ";

		Tile tile = JCiv.map.getTile(this.cityID);
		if (tile != null) {
			Unit unit = JCiv.map.getTile(this.cityID).getUnit();

			if (unit != null) {
				buffer += unit;
				stationedUnitOpenButton.setEnabled(true);
			} else {
				buffer += "Empty";
				stationedUnitOpenButton.setEnabled(false);
			}
		}


		buffer += "</html>";

		this.stationedUnitLabel.setText (buffer);
	}
}