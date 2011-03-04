package org.comet4j.core.demo.talker;

import java.util.HashMap;
import java.util.Map;

/**
 * (用一句话描述类的主要功能)
 * @author xiaojinghai
 * @date 2011-2-25
 */

public class AppStore {

	private static Map<String, String> map;
	private static AppStore instance;

	public static AppStore getInstance() {

		if (instance == null) {
			instance = new AppStore();
			map = new HashMap<String, String>();
		}
		return instance;
	}

	public void put(String key, String value) {
		map.put(key, value);
	}

	public String get(String key) {
		return map.get(key);
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void destroy() {
		map.clear();
		map = null;
	}

}
