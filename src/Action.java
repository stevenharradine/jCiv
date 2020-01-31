/*
 * (c) 2020 Steven J Harradine
 */
import javax.swing.JButton;
public abstract class Action extends JButton {
	private Unit parentUnit;
	private String name;

	public abstract boolean act ();
	public abstract void updateLabels ();

	public Unit getParentUnit () {
		return parentUnit;
	}
	public void setParentUnit (Unit newParentUnit) {
		this.parentUnit = newParentUnit;
	}

	public String getName () {
		return this.name;
	}
	public void setName (String newName) {
		this.name = newName;
	}
}