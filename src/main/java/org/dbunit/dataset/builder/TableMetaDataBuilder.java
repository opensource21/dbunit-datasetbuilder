/*
 *
 * The DbUnit Database Testing Framework
 * Copyright (C)2002-2008, DbUnit.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package org.dbunit.dataset.builder;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.ColumnKeyMap;
import org.dbunit.dataset.CustomTableMetaData;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.builder.valuegenerator.ValueGenerator;
import org.dbunit.dataset.datatype.DataType;

public class TableMetaDataBuilder {

    private final String tableName;
    private final ColumnKeyMap<Column> keysToColumns = new ColumnKeyMap<Column>();
    private final ColumnKeyMap<ValueGenerator<? extends Object>> valueGenerators = new ColumnKeyMap<ValueGenerator<? extends Object>>();

    public TableMetaDataBuilder( String tableName ) {
    	if( tableName == null || tableName.trim().toString().isEmpty() ) {
    		throw new IllegalArgumentException("Table name must not be null.");
    	}
        this.tableName = tableName;
    }

    public TableMetaDataBuilder with(ITableMetaData metaData) throws DataSetException {
        return with(metaData.getColumns());
    }

    public TableMetaDataBuilder with(Column... columns) {
        for (Column column : columns) {
            with(column);
        }
        return this;
    }

    public TableMetaDataBuilder with(Column column) {
        if (isUnknown(column)) {
        	this.keysToColumns.put( column.getColumnName(), column );
        }
        return this;
    }
    
    public TableMetaDataBuilder with(String column) {
    	with( new Column( column, DataType.UNKNOWN ) );
    	return this;
    }
	
	public TableMetaDataBuilder addValueGenerator( String columnName, ValueGenerator<? extends Object> valueGenerator ) {
		this.with( columnName );
		this.valueGenerators.put( columnName, valueGenerator );
        return this;
    }

    public CustomTableMetaData build() {
        return new CustomTableMetaData( tableName, columns(), new String[0], this.valueGenerators );
    }
    
    public String getTableName() {
		return this.tableName;
	}

    private boolean isUnknown(Column column) {
        return !( this.keysToColumns.containsKey( column.getColumnName() ) );
    }

    private Column[] columns() {
        return this.keysToColumns.values().toArray(new Column[this.keysToColumns.size()]);
    }
}
