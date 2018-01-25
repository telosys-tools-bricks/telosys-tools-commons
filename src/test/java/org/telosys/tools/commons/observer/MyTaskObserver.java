package org.telosys.tools.commons.observer;

public class MyTaskObserver implements TaskObserver<String> {

	@Override
    public void notify(String message) {
		System.out.println("notified with message : " + message );
    }

}
