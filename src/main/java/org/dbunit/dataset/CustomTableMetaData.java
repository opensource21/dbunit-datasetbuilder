package org.dbunit.dataset;

import org.dbunit.dataset.builder.valuegenerator.ValueGenerator;

public class CustomTableMetaData extends DefaultTableMetaData {

	private final ColumnKeyMap<ValueGenerator<? extends Object>> valueGenerators;
	
	public CustomTableMetaData(String tableName, Column[] columns, String[] primaryKeys, ColumnKeyMap<ValueGenerator<? extends Object>> valueGenerators) {
		super(tableName, columns, primaryKeys);
		//TODO enhance to ensure no invalid values are passed (assert tableName not null or empty and column not empty).
		this.valueGenerators = valueGenerators;
	}
	
	public ValueGenerator<? extends Object> getValueGenerator( String columnName ) {
		if( columnName == null ) {
			throw new IllegalArgumentException("Column name must not be null.");
		}
		return this.valueGenerators.get( columnName );
	}
	
	public void applyValueGenerators( Row...rows ) {
		if( rows != null ) {
			for( Row row : rows ) {
				for ( Column column : this.getColumns() ) {
					if ( !row.containsColumn( column.getColumnName() ) ) {
						ValueGenerator<? extends Object> valueGenerator = this.getValueGenerator( column.getColumnName() );
						if( valueGenerator != null ) {
							row.add( column.getColumnName(), valueGenerator.nextValue() );
						} else {
							//TODO must force existence of generator for all non-specified & non-nullable columns (+1) or must ignore?
						}
					}
				}
			}
		}
	}

}
