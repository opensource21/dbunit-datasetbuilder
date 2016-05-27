package org.dbunit.dataset.builder;

import org.dbunit.dataset.CustomTableMetaData;
import org.dbunit.dataset.Row;

public abstract class AbstractDataSetRowBuilder {

	protected final Row row = new Row();
	
	public String getTableName() {
		return this.getTableMetaData().getTableName();
	}
	
	public Row build() {
		this.getTableMetaData().applyValueGenerators( this.row );
    	return this.row.copy();
    }
	
	protected abstract CustomTableMetaData getTableMetaData();
	
	@SuppressWarnings("unchecked")
	protected <T extends AbstractDataSetRowBuilder> T _with( String columnName, Object value ) {
    	this.row.add( columnName, value );
    	return (T)this;
    }
}
