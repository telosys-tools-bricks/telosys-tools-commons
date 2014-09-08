package org.telosys.tools.tests.commons.dialogbox;

import javax.swing.JOptionPane;

public class ConfirmDialogBox {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		dialogBox1() ;
		dialogBox2() ;
		dialogBox3() ;
	}
	
	private static void dialogBox1() {
		int r = JOptionPane.showConfirmDialog(null, 
				"Type : OK_CANCEL_OPTION ", 
				"JOptionPane.showConfirmDialog", 
				JOptionPane.OK_CANCEL_OPTION);
		info (r, "");
	}

	private static void dialogBox2() {
		int r = JOptionPane.showConfirmDialog(null, 
				"Type : YES_NO_CANCEL_OPTION ", 
				"JOptionPane.showConfirmDialog", 
				JOptionPane.YES_NO_CANCEL_OPTION);
		info (r, "");
	}

	private static void dialogBox3() {
		//Custom button text
		Object[] options = {"Yes", "Yes To All", "No", "No To All", "Cancel" };
		int r = JOptionPane.showOptionDialog(null, 
				"Type : YES_NO_CANCEL_OPTION ", 
				"JOptionPane.showConfirmDialog", 
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				0);
		info (r, "");
	}

	private static void info(int value, String msg) {
		JOptionPane.showMessageDialog(null, 
				"Return value = " + value + "\n" + msg,
				"Dialog box choice ", 
				JOptionPane.INFORMATION_MESSAGE ); 
	}
}
