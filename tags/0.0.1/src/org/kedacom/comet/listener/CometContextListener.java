package org.kedacom.comet.listener;

import org.kedacom.comet.event.CometContextEvent;
import org.kedacom.event.Listener;

public abstract class CometContextListener extends Listener<CometContextEvent> {
	public boolean handleEvent(CometContextEvent event){
		if(CometContextEvent.INITIALIZED == event.getSubType()){
			return onInitialized(event);
		}else if( CometContextEvent.DESTROYED == event.getSubType() ){
			return onDestroyed(event);
		}
		return true;
	}
	public abstract boolean onInitialized(CometContextEvent event);
	public abstract boolean onDestroyed(CometContextEvent event);
}
