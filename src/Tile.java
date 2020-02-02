/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import javax.swing.JButton;

import javax.swing.ImageIcon;

public abstract class Tile extends JButton {
	private String type;
	private Unit unit = null;
	private City city = null;

	public Tile () {

	}

	public void setType (String newType) {
		this.type = newType;

		update ();
	}
	public String getType () {
		return this.type;
	}

	public void setUnit (Unit newUnit) {
		this.unit = newUnit;

		if (this.city == null) {
			this.setIcon(new ImageIcon(unit.getIcon()));
		}

		update ();
	}
	public void deleteUnit () {
		this.unit = null;

		if (this.city == null) {
			this.setIcon(null);
		}
		
		update ();
	}
	public Unit getUnit () {
		if (this.unit != null && this.unit.getUnitType().equals("")) {
			return null;
		} else{
			return this.unit;
		}
	}
	public void setCity (City newCity) {
		this.city = newCity;

		if (this.city != null) {
			this.setIcon(new ImageIcon(city.getIcon()));
		}

		update ();
	}
	public City getCity () {
		return this.city;
	}

	public void update () {
		if (this.city != null) {
			this.city.updateLabels();
		}
	}

	public String toString () {
		return this.type.charAt (0) + (this.city != null ? "C" : "") + (this.unit != null ? this.unit.getUnitType().charAt(0) : "");
	}
}