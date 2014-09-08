/**
 *  Copyright (C) 2008-2014  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.commons.io;

import javax.swing.JOptionPane;

public class DialogBoxOverwriteChooser implements OverwriteChooser {

	private Integer globalChoice = null ;
	private final static Object[] options = {"Yes",  "Yes To All",  "No",  "No To All",  "Cancel" };
	
	public DialogBoxOverwriteChooser() {
		super();
	}

	public int choose(String fileName) {
		String message = "Overwrite file \n"
			+ "'" + fileName + "' ?" ;
		return openDialogBox( message) ;
	}

	public int choose(String fileName, String folderName) {
		String message = "Overwrite file '" + fileName + "' \n"
		+ "in folder '" + folderName + "'  ? ";
		return openDialogBox(message);
	}
	
	private int openDialogBox(String message) {
		if ( globalChoice != null ) {
			return globalChoice ;
		}
		int r = JOptionPane.showOptionDialog(
				null, 
				message, 
				"Question ", 
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				0);
		
		switch(r) {
		case 0 : // YES 
			return OverwriteChooser.YES ;
		case 1 : // YES FOR ALL
			globalChoice = OverwriteChooser.YES; 
			return globalChoice ;
		case 2 : // NO
			return OverwriteChooser.NO ;
		case 3 : // NO FOR ALL
			globalChoice = OverwriteChooser.NO; 
			return globalChoice ;
		case 4 : // CANCEL
			return OverwriteChooser.CANCEL ;
		default :
			throw new RuntimeException("Unexpected choice " + r);
		}
	}
}
