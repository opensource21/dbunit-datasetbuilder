package org.dbunit.dataset;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;
/**
 * 
 * @author Rafael Vanderlei
 *
 * Using this implementation where we need to store Maps using a column name as key ensures the following:
 * <ul>
	 * <li>Keep the same order as the the column names were added.</li>
	 * <li>Case of the column names are ignored.</li>
	 * <li>Exception is thrown where null or empty column names are used.</li>
 * </ul>
 *
 * @param <Value> - the type of mapped values
 */
public class ColumnKeyMap<Value> extends LinkedHashMap<String, Value> {
	private static final long serialVersionUID = 1L;

	@Override
	public Value put(String key, Value value) {
		this.assertColumnNameNotNullOrEmpty( key );
		return super.put(key.toUpperCase(), value);
	}
	
	@Override
	public Value get(Object key) {
		this.assertColumnNameNotNullOrEmpty( key );
		return super.get(key.toString().toUpperCase());
	}
	
	@Override
	public boolean containsKey(Object key) {
		this.assertColumnNameNotNullOrEmpty( key );
		return super.containsKey(key.toString().toUpperCase());
	}
	
	@Override
	public Set<String> keySet() {
		return Collections.unmodifiableSet( super.keySet() );
	}
	
	private void assertColumnNameNotNullOrEmpty( Object key ) {
		if( key == null || key.toString().trim().isEmpty() ) {
			throw new IllegalArgumentException("Column Name must not be null or empty.");
		}
	}
}
