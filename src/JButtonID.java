/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import javax.swing.JButton;

public class JButtonID extends JButton {
    private String id;

    public JButtonID (String text, String id) {
        super (text);

        this.id = id;
    }

    public String getID () {
        return this.id;
    }
}