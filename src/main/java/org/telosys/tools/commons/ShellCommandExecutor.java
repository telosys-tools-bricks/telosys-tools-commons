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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.exception.TelosysRuntimeException;

/**
 * Shell Command Executor
 * 
 * @author Laurent GUERIN
 *
 */
public class ShellCommandExecutor {

	public ShellCommandExecutor() {
		super();
	}

	public ShellCommandResult execute(String command) {
		return execute(command, null);
	}

	public ShellCommandResult execute(String command, String directory) {
		try {
			return executeCommand(command, directory);
		} catch (IOException e) {
			throw new TelosysRuntimeException("IOException : " + e.getMessage());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return null;
		}
	}
	
	protected ShellCommandResult executeCommand(String command, String directory) throws IOException, InterruptedException {
		Process process = launchProcess(command, directory);
		
		List<String> output = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			output.add(line);
		}
		
		// Wait and get exit code
		// throws InterruptedException if the current thread is interrupted  
		// by another thread while it is waiting
		int exitValue = process.waitFor(); 
		
		return new ShellCommandResult(exitValue, output);
	}
	
	protected Process launchProcess(String command, String directory) throws IOException {
		File dir = getDir(directory);
		return Runtime.getRuntime().exec(
			command, // a specified system command, eg "cmd /c hello.bat"
			null, 	// envp array of strings, each element of which has environment variable settings 
					// in the format name=value, or null if none
			dir	// the working directory of the subprocess, 
				// or null if the subprocess should inheritthe working directory of the current process.
			);
	}

	protected File getDir(String directory) {
		if ( StrUtil.nullOrVoid(directory) ) {
			return null;
		}
		else {
			File dir = new File(directory) ;
			if ( ! dir.exists() ) {
				throw new IllegalArgumentException("invalid directory (does not exist)");
			}
			if ( ! dir.isDirectory() ) {
				throw new IllegalArgumentException("invalid directory (not a directory)");
			}
			return dir ;
		}
	}
}
