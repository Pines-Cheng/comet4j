package org.kedacom.comet.event;

import org.kedacom.comet.CometEngine;
import org.kedacom.event.Event;


public class ErrorEvent extends Event<CometEngine> {
	private Exception err ;
	public ErrorEvent(CometEngine target,Exception anErr) {
		super(target);
		err = anErr;
	}
	public Exception getErr() {
		return err;
	}
	public void setErr(Exception err) {
		this.err = err;
	}
	
}
