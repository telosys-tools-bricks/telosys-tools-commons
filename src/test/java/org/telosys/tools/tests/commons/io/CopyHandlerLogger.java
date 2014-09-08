package org.telosys.tools.tests.commons.io;

import java.io.File;

import org.telosys.tools.commons.io.CopyHandler;

public class CopyHandlerLogger implements CopyHandler {

	@Override
	public void beforeCopy(File origin, File destination) {
		System.out.println("BEFORE copy from " + origin + " to " + destination );		
	}

	@Override
	public void afterCopy(File origin, File destination) {
		System.out.println("AFTER copy from " + origin + " to " + destination );		
	}

}
