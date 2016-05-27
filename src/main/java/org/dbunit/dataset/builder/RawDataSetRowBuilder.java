package org.dbunit.dataset.builder;

import org.dbunit.dataset.CustomTableMetaData;

public class RawDataSetRowBuilder extends AbstractDataSetRowBuilder {

	private TableMetaDataBuilder tableMetaDataBuilder;
	
	private RawDataSetRowBuilder(String tableName) {
		this.tableMetaDataBuilder = new TableMetaDataBuilder( tableName );
	}
	
	public static RawDataSetRowBuilder newRawRow( String tableName ) {
		return new RawDataSetRowBuilder( tableName );
	}
	
	protected CustomTableMetaData getTableMetaData() {
		return this.tableMetaDataBuilder.build();
	}
	
	public <T extends RawDataSetRowBuilder> T with( String columnName, Object value ) {
		this.tableMetaDataBuilder.with( columnName );
    	return super._with( columnName, value );
    }
	
	public <T extends RawDataSetRowBuilder, E> T with( ColumnSpec<E> columnSpec, E value ) {
		this.tableMetaDataBuilder.with( columnSpec.name() );
    	this.row.add( columnSpec, value );
    	return (T)this;
    }
}
