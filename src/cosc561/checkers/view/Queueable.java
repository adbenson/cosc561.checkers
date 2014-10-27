package cosc561.checkers.view;

import java.lang.reflect.Method;

public interface Queueable {
	
	public boolean isQueueTrigger(Method m);

	public boolean shouldQueue(Method m);
	
}
