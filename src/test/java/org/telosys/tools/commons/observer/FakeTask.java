package org.telosys.tools.commons.observer;

public class FakeTask {

	private final TaskObserver<String> observer ; 
	
	public FakeTask(TaskObserver<String> observer) {
		this.observer = observer ;
	}

    private void notify(String message) {
    	if ( observer != null ) {
        	observer.notify(message);
    	}
    }

    
    public void doYourJob() {
    	for ( int i = 0 ; i < 20 ; i++ ) {
    		System.out.println("In FakeTask ");
    		notify("Step = " + i);
    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
	    		notify("Interrupted" );
			}
    	}
    }
}
