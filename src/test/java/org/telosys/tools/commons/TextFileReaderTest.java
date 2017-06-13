package org.telosys.tools.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class TextFileReaderTest {

	@Test ( expected=RuntimeException.class)
	public void testInex() {
		System.out.println("----------");
		File file = FileUtil.getFileByClassPath("/textfiles/file-inex.txt");
        System.out.println( "File = " + file.getAbsolutePath() ) ;
		new TextFileReader1(file.getAbsolutePath());
	}

	//----------------------------------------------------------------------------------
	class TextFileReader1 extends TextFileReader {
		public TextFileReader1(String filePath) {
			super(filePath);
		}
		@Override
		protected void parseLine(String line, List<String> lines) {
		}
	}
	@Test
	public void testNoLine() {
		System.out.println("----------");
		File file = FileUtil.getFileByClassPath("/textfiles/file1.txt");
        System.out.println( "File = " + file.getAbsolutePath() ) ;
		TextFileReader1 reader = new TextFileReader1(file.getAbsolutePath());
        assertTrue ( reader.exists() ) ;
        assertEquals ( 0, reader.loadLines().size() );
	}
	
	//----------------------------------------------------------------------------------
	class TextFileReader2 extends TextFileReader {
		public TextFileReader2(String filePath) {
			super(filePath);
		}
		@Override
		protected void parseLine(String line, List<String> lines) {
			lines.add(line);
		}
	}
	@Test
	public void testAllLines() {
		System.out.println("----------");
		File file = FileUtil.getFileByClassPath("/textfiles/file1.txt");
        System.out.println( "File = " + file.getAbsolutePath() ) ;
		TextFileReader2 reader = new TextFileReader2(file.getAbsolutePath());
        assertTrue ( reader.exists() ) ;
        assertEquals ( 8, reader.loadLines().size() );
	}

	//----------------------------------------------------------------------------------
	class TextFileReader3 extends TextFileReader {
		public TextFileReader3(String filePath) {
			super(filePath);
		}
		@Override
		protected void parseLine(String line, List<String> lines) {
			if ( line.trim().length() > 0 ) {
				lines.add(line);
			}
		}
	}
	@Test
	public void testLinesNotBlankOrVoid() {
		System.out.println("----------");
		File file = FileUtil.getFileByClassPath("/textfiles/file1.txt");
        System.out.println( "File = " + file.getAbsolutePath() ) ;
		TextFileReader3 reader = new TextFileReader3(file.getAbsolutePath());
        assertTrue ( reader.exists() ) ;
        assertEquals ( 6, reader.loadLines().size() );
	}
}
