/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.awt.Image;

public abstract class Unit extends JFrame {
	public static Unit[] UNITS_AVAILABLE = { new Settler (), new Worker () };

	private String type;
	private int moves = 0;
	private int moves_left;
	private String unitID;
	private Action[] actions;
	private int productionCost;
	private JLabel movesLabel;
	private JPanel actionsPanel;
	public Map map;

	public Unit () {
		super ();
		this.unitID = JCiv.generateRandomID();

		this.setLayout (new BorderLayout());
		this.movesLabel = new JLabel ();

		JPanel movesPanel = new JPanel ();
		movesPanel.setLayout (new GridLayout(3,3));

		movesPanel.add (createMoveButton ("7"));
		movesPanel.add (createMoveButton ("8"));
		movesPanel.add (createMoveButton ("9"));
		movesPanel.add (createMoveButton ("4"));
		movesPanel.add (createMoveButton (""));
		movesPanel.add (createMoveButton ("6"));
		movesPanel.add (createMoveButton ("1"));
		movesPanel.add (createMoveButton ("2"));
		movesPanel.add (createMoveButton ("3"));

		actionsPanel = new JPanel ();
		actionsPanel.setLayout(new FlowLayout());

		add (movesLabel, BorderLayout.NORTH);
		add (movesPanel, BorderLayout.CENTER);
		add (actionsPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300,500);//400 width and 500 height  
	}

	public JButton createMoveButton (String text) {
		JButton button = new JButton (text);

		button.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = ((JFrame)((JButton)e.getSource()).getParent().getParent().getParent().getParent().getParent()).getTitle();
				String action = ((JButton)e.getSource()).getText();
				String[] titleParts = title.split ("\\|");
				String unitID = titleParts[1];
				Unit unit = null;

				// find the unit on the map
				int x = -1;
				int y = -1;
				for (int i = 0; i < JCiv.map.getTiles().length; i++) {
					for (int j = 0; j < JCiv.map.getTiles()[i].length; j++) {
						if (JCiv.map.getTile(i,j).getUnit() != null && JCiv.map.getTile(i,j).getUnit().getUnitID().equals(unitID)) {
							unit = JCiv.map.getTile(i,j).getUnit();
							x = i;
							y = j;
						}
					}
				}
				
				boolean isMoveSuccess = false;
				int newX = x;
				int newY = y;
				if ("9".equals(action)) {			// move up/right
					newX--;
					newY++;
				} else if ("8".equals(action)) {	// move up
					newX--;
				} else if ("7".equals(action)) {	// move up/left
					newX--;
					newY--;
				} else if ("4".equals(action)) {	// move left
					newY--;
				} else if ("6".equals(action)) {	// move right
					newY++;
				} else if ("3".equals(action)) {	// move down/right
					newX++;
					newY++;
				} else if ("2".equals(action)) {	// move down
					newX++;
				} else if ("1".equals(action)) {	// move down/left
					newX++;
					newY--;
				}
				isMoveSuccess = JCiv.map.moveUnit(x, y, newX, newY);

				if (isMoveSuccess) {
					if (JCiv.map.getTile(x,y).getCity() != null) {
						JCiv.map.getTile(x,y).getCity().updateLabels();
					}
					if (JCiv.map.getTile(newX,newY).getCity() != null) {
						JCiv.map.getTile(newX,newY).getCity().updateLabels();
					}

					unit.updateLabels();
					unit.repaint();
				}
			}
		});

		return button;
	}

	public Unit (String newType) {
		if (!"".equals(newType)) {
			this.setType (newType);
		} else {
			this.type = null;
		}
	}

	public String getUnitID () {
		return this.unitID;
	}

	public Image getIcon () {
		try {
			Image img = ImageIO.read(new FileInputStream("graphics/" + this.type + ".png"));
			
			return img;
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	} 

	public void setType (String newType) {
		this.type = newType;
		this.setTitle("Unit: " + this.type + " |"+ this.unitID);
	}
	public String getUnitType () {
		return type;
	}
	public void setMoves (int newMoves) {
		this.moves = newMoves;
		this.moves_left = newMoves;

		updateLabels();
	}

	public int getMoves () {
		return this.moves;
	}
	public int getMovesLeft () {
		return this.moves_left;
	}
	public boolean removeMove () {
		if (this.moves_left >= 1) {
			this.moves_left--;

			updateLabels();
			
			return true;
		}
		return false;
	}

	public String toString() {
		return this.type + "(Moves " + this.moves_left + ")";
	}
	public static String toString(Unit unit) {
		return unit.type + "(" + unit.productionCost + " production)";
	}

	public void resetMoves() {
		this.moves_left = this.moves;
		updateLabels();
	}

	public Action[] getActions () {
		return this.actions;
	}
	public void setActions (Action[] newActions) {
		this.actions = newActions;

		for (int i = 0; i < this.actions.length; i++) {
			this.actionsPanel.add (actions[i]);

			actions[i].addActionListener (new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Action action = (Action)e.getSource();
					action.act();
				}
			});
		}
	}
	public void setProductionCost (int newProductionCost) {
		this.productionCost = newProductionCost;
	}
	public int getProductionCost () {
		return this.productionCost;
	}


	private void updateMovesLabel () {
		movesLabel.setText ("Moves: " + this.moves_left + " / " + this.moves);
	}
	public void updateLabels () {
		updateMovesLabel ();

		Component[] components = this.actionsPanel.getComponents();
		if (components.length > 0) {
			for (int i = 0; i < components.length; i++) {
				((Action)components[i]).updateLabels();
			}
		}
	}

	public void displayUnit () {
		this.repaint();
		this.setVisible(true);//making the frame visible 
	}
}