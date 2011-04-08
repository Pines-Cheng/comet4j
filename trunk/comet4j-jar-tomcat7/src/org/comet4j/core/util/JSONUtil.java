/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */

package org.comet4j.core.util;

// ~--- JDK imports ------------------------------------------------------------

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

// ~--- non-JDK imports --------------------------------------------------------

public class JSONUtil {

	public JSONUtil() {
	}

	private static boolean isSimpleType(@SuppressWarnings("rawtypes") Class clazz) {
		return clazz.isPrimitive() || String.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz)
				|| java.util.Date.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)
				|| Character.class.isAssignableFrom(clazz);
	}

	public static String convertToJson(Object obj) {
		JsonHandle jsonHandle = new JsonHandle();

		return jsonHandle.convertToJson(0, obj).toString();
	}

	private static class JsonHandle {

		public static final int DEFAULT_DEPTH = 10;

		@SuppressWarnings("rawtypes")
		public StringBuffer convertToJson(int depth, Object obj) {
			StringBuffer sb = new StringBuffer();

			if (obj == null) {
				sb.append("null");
			} else if (isSimpleType(obj.getClass())) {
				// fixed by xiao
				if ((obj instanceof String)) {
					String str = obj.toString();
					str = str.replace("\\", "\\\\"); // 将特殊字符处理成转义字符
					str = str.replace("\r", "\\r"); // 将回车处理成转义字符
					str = str.replace("\n", "\\n"); // 将换行处理成转义字符
					str = str.toString().replace("\"", "\\\"");// 将双引号处理成转义字符
					sb.append("\"").append(str).append("\"");
				} else if (obj instanceof java.util.Date) {
					sb.append("\"").append(obj).append("\"");
				} else if (obj instanceof Character) {
					sb.append("'").append(obj).append("'");
				} else {
					sb.append(obj);
				}
			} else if (obj instanceof Collection) {
				boolean hibernateFlag;
				try {
					((Collection) obj).size();
					hibernateFlag = true;
				} catch (Exception ex) {
					hibernateFlag = false;
				}

				if (hibernateFlag) {
					sb.append("[");

					for (Iterator iterator = ((Collection) obj).iterator(); iterator.hasNext();) {
						sb.append(convertToJson(depth, iterator.next()));

						if (iterator.hasNext()) {
							sb.append(",");
						}
					}

					sb.append("]");
				} else {
					sb.append("null");
				}

			} else if (obj.getClass().isArray()) {
				sb.append("[");

				int max = java.lang.reflect.Array.getLength(obj);

				for (int i = 0; i < max; i++) {
					if (i > 0) {
						sb.append(",");
					}

					sb.append(convertToJson(depth, java.lang.reflect.Array.get(obj, i)));
				}

				sb.append("]");
			} else if (java.util.Map.class.isAssignableFrom(obj.getClass())) {
				// sb.append("{\n");
				sb.append("{");// fix by xiao
				for (Map.Entry e : ((Map<?, ?>) obj).entrySet()) {
					if (!(e.getKey() instanceof String)) continue;
					sb.append(e.getKey()).append(":");

					if (depth <= DEFAULT_DEPTH) {
						sb.append(convertToJson(depth + 1, e.getValue()));
					} else {
						sb.append("undefined");
					}

					sb.append(",");
				}

				if (sb.length() > 3) {
					sb.deleteCharAt(sb.length() - 1);
				}

				// sb.append("\n}");
				sb.append("}");// fix by xiao
			} else {
				Map map = null;

				try {
					map = BeanUtil.getPropertiesByReflect(obj);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				sb.append(convertToJson(depth, map));
			}

			return sb;
		}
	}
}
