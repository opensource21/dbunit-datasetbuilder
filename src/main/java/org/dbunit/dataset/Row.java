package org.dbunit.dataset;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dbunit.dataset.builder.ColumnSpec;

public class Row {

    private final ColumnKeyMap<Object> columns = new ColumnKeyMap<Object>();

    public Row add(String columnName, Object value) {
		this.columns.put( columnName, value );
		return this;
	}
    
    public <T> Row add(ColumnSpec<T> columnSpec, T value) {
    	if( columnSpec == null ) {
    		throw new IllegalArgumentException("ColumnSpec must not be null.");
    	}
		return this.add( columnSpec.name(), value );
	}
    
    public Object get(String columnName) {
    	return this.columns.get( columnName );
    }
    
    public Set<String> getColumnNames() {
    	return this.columns.keySet();
    }
    
    public boolean containsColumn(String columnName) {
    	return ( this.columns.containsKey( columnName ) );
	}
    
    public Row copy() {
    	Row copy = new Row();
    	for ( Entry<String, Object> entry : this.columns.entrySet() ) {
			copy.add( entry.getKey(), entry.getValue() );
		}
    	return copy;
    }
    
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Row)) {
            return false;
        }

        final Row other = (Row) obj;

        final ColumnKeyMap<Object> otherCells = other.columns;

        if (columns.size() != otherCells.size()) {
            return false;
        }

        for (Map.Entry<String, Object> cell : columns.entrySet()) {
            final String name = cell.getKey();
            final Object value = cell.getValue();
            if (!value.equals(otherCells.get(name))) {
                return false;
            }
        }

        return true;

    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = prime * result + ((columns == null) ? 0 : cellHashCode());
        return result;
    }

    private int cellHashCode() {
        final int prime = 41;
        int result = 1;
        for (Map.Entry<String, Object> cell : columns.entrySet()) {
            result = prime * result + cell.getKey().hashCode();
            result = prime * result + cell.getValue().hashCode();
        }
        return result;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder("Row [cells=");
    	for (Map.Entry<String, Object> cell : columns.entrySet()) {
            sb.append("{key = ").append(cell.getKey()).append(", value = ").append(cell.getValue()).append("} ");
        }
    	sb.append("]");
    	return sb.toString();
    }
}
