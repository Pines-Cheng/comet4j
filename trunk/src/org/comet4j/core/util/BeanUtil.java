/*
 * BeanUtil.java
 *
 * Created on 2007��5��8��, ����1:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.comet4j.core.util;

// ~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/** @author Fly_m */
@SuppressWarnings("unchecked")
public class BeanUtil {

	/** Creates a new instance of BeanUtil */
	private BeanUtil() {
	}

	public static Map getPropertiesByReflect(Object obj) throws Exception {
		if(obj == null)
			return null;
		Field[] fields = obj.getClass().getDeclaredFields();
		if(fields == null || fields.length == 0)
			return null;
		Map<String, Object> map = new HashMap<String, Object>();
		AccessibleObject.setAccessible(fields, true);
		for(Field field : fields) {
			//remove by xiao if(!field.getName().equals("serialVersionUID") && field.getAnnotation(Fly_m.class) == null)
				map.put(field.getName(), field.get(obj));

		}
		if(map.size() < 1)
			return null;
		return map;
	}

}
