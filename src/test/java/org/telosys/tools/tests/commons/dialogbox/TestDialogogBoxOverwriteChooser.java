package org.telosys.tools.tests.commons.dialogbox;

import org.telosys.tools.commons.io.DialogBoxOverwriteChooser;
import org.telosys.tools.commons.io.OverwriteChooser;

public class TestDialogogBoxOverwriteChooser {
	
	public static void main(String[] args) {

		OverwriteChooser chooser = new DialogBoxOverwriteChooser();
		int r = chooser.choose("/foo/bar/file.txt");
		System.out.println("Chooser result : " + r);
		
		
		r = chooser.choose("/foo/bar/file2.txt");
		System.out.println("Chooser result : " + r);
		
	}

}
