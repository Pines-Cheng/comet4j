package org.kedacom.event.demo.mac;


import org.kedacom.event.Event;
import org.kedacom.event.Listener;
import org.kedacom.event.Observable;



public class Test {
	//用法展示:宏观事件模式
	public class PersonEvent extends Event<Person>{
		private SubEventType subType;
		public String sayWords ="";
		public String goWhere ="";
		public PersonEvent(Person target){
			super(target);
		}
		public PersonEvent(Person target,SubEventType subtype){
			super(target);
			this.subType = subtype;
		}
		public SubEventType getSubType(){
			return subType;
		}
	}
	
	public abstract class PersonListener extends Listener<PersonEvent>{
		public boolean handleEvent(PersonEvent pe){
			if(PersonEventSubType.BEFORESAY == pe.getSubType()){
				return onBeforeSay(pe);
			}else if( PersonEventSubType.BEFOREGO == pe.getSubType() ){
				return onBeforeGo(pe);
			}
			return true;
		}
		public abstract boolean onBeforeSay(PersonEvent pe);
		public abstract boolean onBeforeGo(PersonEvent pe);
	}
	
	public class PersonListener1 extends PersonListener{
		public boolean onBeforeSay(PersonEvent pe){
			System.out.println("1:One person want to say:"+pe.sayWords);
			return true;
		}
		public boolean onBeforeGo(PersonEvent pe){
			System.out.println("1:One person want to go:"+pe.goWhere);
			return true;
		}
	}
	
	
	public class Person extends Observable<PersonEvent,PersonListener>{
		public Person(){
			this.addEvent(PersonEvent.class);
		}
		public void say(String aWords){
			PersonEvent e = new PersonEvent(this,PersonEventSubType.BEFORESAY);
			e.sayWords = aWords;
			if(!this.fireEvent(e)){
				return;
			}
			System.out.println("say:"+aWords);
		}
		public void go(String where){
			PersonEvent e = new PersonEvent(this,PersonEventSubType.BEFOREGO);
			e.goWhere = where;
			if(!this.fireEvent(e)){
				return;
			}
			System.out.println("go:"+where);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Test().run();
		
	}
	//用法
	public void run(){
		Person person = new Person();

		person.addListener(PersonEvent.class, new PersonListener1());
		
		//行间加入侦听
		person.addListener(PersonEvent.class, new PersonListener(){
			public boolean onBeforeSay(PersonEvent pe){
				//pe.stopEvent();
				//pe.preventDefault();
				System.out.println("2:One person want to say:"+pe.sayWords);
				return true;
			}
			public boolean onBeforeGo(PersonEvent pe){
				pe.stopEvent();
				//pe.preventDefault();
				System.out.println("2:One person want to go:"+pe.goWhere);
				return true;
			}
		});
		
		person.say("Hello world!");
		person.go("Home");

	}

}
