package org.telosys.tools.tests.commons.dialogbox;

import javax.swing.JOptionPane;

public class MessageDialogBox {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		JOptionPane.showMessageDialog(null, 
				"My message",
				"My title", 
				
				// the type of message to be displayed: 
				//ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE, or PLAIN_MESSAGE

				JOptionPane.INFORMATION_MESSAGE ); // "Info" icon, "OK" button
				//JOptionPane.ERROR_MESSAGE ); // "Error" icon, "OK" button
				//JOptionPane.WARNING_MESSAGE ); // "Warning" icon, "OK" button
				//JOptionPane.PLAIN_MESSAGE ); // No icon, "OK" button
				//JOptionPane.QUESTION_MESSAGE ); // "?" icon, "OK" button
	}

}
