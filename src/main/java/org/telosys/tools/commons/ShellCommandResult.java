/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.commons;

import java.util.LinkedList;
import java.util.List;

/**
 * Shell Command Result with exit code and output lines
 * 
 * @author Laurent GUERIN
 *
 */
public class ShellCommandResult {

	private final int exitValue ;
	private final List<String> outputLines ;
	private String outputString ;
	
	protected ShellCommandResult(int exitValue, List<String> output) {
		super();
		this.exitValue = exitValue;
		this.outputLines = output != null ? output : new LinkedList<String>();
		this.outputString = null;
	}

	public int getExitValue() {
		return exitValue;
	}

	public List<String> getOutputLines() {
		return outputLines;
	}

	private String buildOutputString() {
		StringBuilder sb = new StringBuilder();
		for ( String line : outputLines ) {
			sb.append(line + "\n");
		}
		return sb.toString();
	}
	
	public String getOutputString() {
		if ( this.outputString == null ) {
			this.outputString = buildOutputString();
		}
		return this.outputString;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("EXIT VALUE = " + exitValue + "\n");
		sb.append("OUTPUT LINES : \n");
		for ( String line : outputLines ) {
			sb.append(" " + line + "\n");
		}
		return sb.toString();
	}

}
