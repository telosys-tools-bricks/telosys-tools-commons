package org.telosys.tools.commons;

import org.junit.Test;
import org.telosys.tools.commons.exception.TelosysRuntimeException;

import junit.framework.TestCase;

public class ShellCommandExecutorIT extends TestCase {

	
	private ShellCommandResult exec(String command) {
		return exec(command, null);
	}
	private ShellCommandResult exec(String command, String dir) {
		System.out.println("Command : '" + command + "', dir : '" + dir + "'");
		ShellCommandExecutor executor = new ShellCommandExecutor();
		ShellCommandResult r = executor.execute(command, dir);
		System.out.println("Result : ");
		System.out.println(r);
		return r;
	}

	@Test
	public void testWindowsCmd1() {		
		ShellCommandResult r = exec("cmd /c dir C:\\Temp"); // only 1 arg
		assertTrue( r.getExitValue() == 0 ); 
	}

	@Test
	public void testWindowsCmd2() {		
		ShellCommandResult r = exec("cmd /c dir", "C:\\Temp"); // 2 args
		assertTrue( r.getExitValue() == 0 ); 
	}

	@Test
	public void testWindowsPowerShell1() {		
		ShellCommandResult r = exec("powershell.exe  $PSVersionTable.PSVersion");
		assertTrue( r.getExitValue() == 0 ); 
	}

	@Test
	public void testWindowsPowerShell2() {		
		ShellCommandResult r = exec("powershell.exe ls");
		assertTrue( r.getExitValue() == 0 ); 
	}

//	@Test
//	public void testWindows99() {
//		// NB : do not launch interactive shell
//		ShellCommandResult r = exec("cmd", "C:\\Temp");
//		assertTrue( r.getExitValue() == 0 ); 
//	}

	@Test
	public void testGit() {		
		ShellCommandResult r = exec("git --version");
		assertTrue( r.getExitValue() == 0 ); 
	}

	@Test
	public void testGo() {		
		ShellCommandResult r = exec("go help mod");
		assertTrue( r.getExitValue() == 0 ); 
	}

	@Test
	public void testInvalidCommand() {		
		ShellCommandResult r;
		try {
			r = exec("badcmd x y");
			System.out.println(r);
		} catch (TelosysRuntimeException e) {
			System.out.println(e.getMessage());
		}
	}
}
