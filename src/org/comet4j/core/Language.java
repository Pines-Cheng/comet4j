package org.comet4j.core;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;


public class Language {
	private static String cnLanguageFile = "/language-cn.xml";
	private static String enLanguageFile = "/language-en.xml";
	
	private static Properties prop;
	
	private static void beforeGet(){
		if(prop == null){
			Locale locale = CometContext.getInstance().getLocale();
			if(Locale.ENGLISH == locale){
				InputStream in = Language.class.getResourceAsStream(enLanguageFile);
				prop = new Properties();
				try {
					prop.loadFromXML(in);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(Locale.CHINESE == locale){
				InputStream in = Language.class.getResourceAsStream(cnLanguageFile);
				prop = new Properties();
				try {
					prop.loadFromXML(in);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getConnectSuccess(){
		beforeGet();
		return prop.getProperty("ConnectSuccess");
	}
	
	public static String getConnectFailure(){
		beforeGet();
		return prop.getProperty("ConnectFailure");
	}
	
	public void destroy() {
		prop = null;
	}
}
