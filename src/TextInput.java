/*
 * (c) 2020 Steven J Harradine
 */
package stevenharradine.jciv;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TextInput {
	private String text;
	private String defaultText;
	private JFrame frame;

	public TextInput (String newText, String newDefaultText, JFrame newFrame) {
		this.text = newText;
		this.defaultText = newDefaultText;
		this.frame = newFrame;
	}

	public String getInput () {
		String inputText = (String)JOptionPane.showInputDialog(
			this.frame,
			this.text,
			"Customized Dialog",
			JOptionPane.PLAIN_MESSAGE,
			null,
			null,
			this.defaultText
		);

		// if the text is valid return it
		if (inputText.length() > 0) {
			return inputText;
		}
		
		// if the text is not valid try again
		return getInput();
	}
}