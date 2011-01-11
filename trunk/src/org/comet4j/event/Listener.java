package org.comet4j.event;

/**
 * 
 */

/**
 * 侦听抽象类 
 * 职责：规范事件侦听的执行方法
 * 
 * @author xiaojinghai@kedacom.com
 */
@SuppressWarnings("unchecked")
public abstract class Listener<E extends Event> implements ListenerInterface<E> {
	/** 
	 * 事件处理
	 * @param anEvent
	 * @return 如果返回值为false则终被终止被终止执行此动作，避免动作的发生（如before类事件，一般为动作发生之前的事件才可以被终止执行，避免动作的发生），
	 * 否则继续执行其它侦听函数。
	 */
	public abstract boolean handleEvent(E anEvent);
}
