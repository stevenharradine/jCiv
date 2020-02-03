/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import javax.swing.JButton;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.awt.Image;
import javax.swing.ImageIcon;

public abstract class Tile extends JButton {
	private String type;
	private Unit unit = null;
	private City city = null;
	private String enhancement = null;

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

		update ();
	}
	public void deleteUnit () {
		this.unit = null;
		
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

		update ();
	}
	public City getCity () {
		return this.city;
	}

	public String getEnhancement () {
		return this.enhancement;
	}
	public void setEnhancement (String newEnhancement) {
		this.enhancement = newEnhancement;

		update ();
	}

	public void update () {
		if (this.city != null) {
			this.setIcon(new ImageIcon(city.getIcon()));
		} else if (this.unit != null) {
			this.setIcon(new ImageIcon(unit.getIcon()));
		} else if (this.enhancement != null) {
			try {
				Image img = ImageIO.read(new FileInputStream("graphics/FARM.png"));
				
				this.setIcon(new ImageIcon(img));
			} catch (Exception e) {
				System.out.println(e);
			}			
		} else {
			this.setIcon (null);
		}
	}

	public String toString () {
		return this.type.charAt (0) + (this.city != null ? "C" : "") + (this.unit != null ? this.unit.getUnitType().charAt(0) : "");
	}
}