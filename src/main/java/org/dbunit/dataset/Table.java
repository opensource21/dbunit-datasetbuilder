package org.dbunit.dataset;

import java.util.ArrayList;
import java.util.List;

import org.dbunit.DatabaseUnitRuntimeException;
import org.dbunit.dataset.builder.TableMetaDataBuilder;
import org.dbunit.dataset.builder.valuegenerator.ValueGenerator;

public class Table extends AbstractTable implements ITableManipulator {

    private final List<Row> rows = new ArrayList<Row>();
    
    private final TableMetaDataBuilder tableMetaDataBuilder;
    
    private CustomTableMetaData tableMetaData;
    
	private boolean committed = false;

    public Table( String tableName ) {
    	
    	if( tableName == null || tableName.trim().isEmpty() ) {
    		throw new DatabaseUnitRuntimeException("Table name must not be null or empty.");
    	}
    	
        this.tableMetaDataBuilder = new TableMetaDataBuilder( tableName );
    }
    
    @Override
    public CustomTableMetaData getTableMetaData() {
    	this.assertCommited();
    	return this.tableMetaData;
    }
    
    @Override
    public int getRowCount() {
    	this.assertCommited();
    	return this.rows.size();
    }
    
    @Override
    public Object getValue(int row, String column) throws DataSetException {
    	this.assertCommited();
    	
    	super.assertValidRowIndex( row );
    	super.assertValidColumn( column );
    	
    	
    	return this.rows.get( row ).get( column );
    }
    
	public Table add(Row row) {
		this.assertNotCommited();
		
		if( row == null ) {
			throw new IllegalArgumentException("Row mut not be null.");
		}
		
		row = row.copy();
    	for ( String columnName : row.getColumnNames() ) {
    		this.tableMetaDataBuilder.with( columnName );
		}
    	
        this.rows.add( row );
        return this;
    }
    
    public Table commit() {
    	this.assertNotCommited();
    	this.tableMetaData = this.tableMetaDataBuilder.build();
    	
    	this.tableMetaData.applyValueGenerators( this.rows.toArray(new Row[0]) );
    	
    	this.committed = true;
    	return this;
    }

	public boolean isCommitted() {
		return this.committed;
	}
	
	public String getTableName() {
		return this.tableMetaDataBuilder.getTableName();
	}
	
	public Table addValueGenerator( String columnName, ValueGenerator<? extends Object> valueGenerator ) {
		this.assertNotCommited();
		this.tableMetaDataBuilder.addValueGenerator( columnName, valueGenerator );
        return this;
    }
	
	private void assertCommited() {
		if( !this.isCommitted() ) {
			throw new DatabaseUnitRuntimeException("Table is not committed yet.");
		}
	}
	
	private void assertNotCommited() {
		if( this.isCommitted() ) {
			throw new DatabaseUnitRuntimeException("Table is already committed.");
		}
	}
}
