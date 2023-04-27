package antigravity.utils;

import org.springframework.jdbc.support.JdbcUtils;

import java.util.HashMap;

@SuppressWarnings({ "serial", "rawtypes" })
public class CamelMap extends HashMap {

	@SuppressWarnings({ "unchecked" })
	@Override
	public Object put(Object key, Object value) {

		String camelCaseKey = JdbcUtils.convertUnderscoreNameToPropertyName((String) key);

		return super.put(camelCaseKey, value);
	}
}