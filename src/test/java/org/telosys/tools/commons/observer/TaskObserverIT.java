package org.telosys.tools.commons.observer;

import org.junit.Test;

public class TaskObserverIT {
	
	@Test
	public void testIsExcludedTable_patterns_null() {
		FakeTask task = new FakeTask(new MyTaskObserver());
		// task.doYourJob();
	}

}
