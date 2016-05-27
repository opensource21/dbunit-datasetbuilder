package org.dbunit.dataset;

import static org.dbunit.dataset.builder.valuegenerator.ValueGeneratorMaker.newFixedValueGenerator;

import java.sql.Date;

import org.dbunit.DatabaseUnitRuntimeException;
import org.junit.Assert;
import org.junit.Test;

public class TableTest {

	@Test(expected=DatabaseUnitRuntimeException.class)
	public void testNewTableNullName() {
		new Table(null);
	}
	
	@Test(expected=DatabaseUnitRuntimeException.class)
	public void testNewTableEmptyName() {
		new Table("");
	}
	
	@Test(expected=DatabaseUnitRuntimeException.class)
	public void testNewTableEmptyTrimName() {
		new Table(" ");
	}
	
	@Test(expected=DatabaseUnitRuntimeException.class)
	public void testGetRowCountWithoutCommit() {
		new Table("PERSON")
			.getRowCount();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNullRow() {
		new Table("PERSON")
			.add( null );
	}
	
	@Test
	public void testAddRow() {
		Table table = new Table("PERSON")
			.add( new Row().add("ID", "1") )
			.add( new Row().add("FIRST_NAME", "FIRST") )
			.commit();
		Assert.assertEquals( 2, table.getRowCount() );
	}
	
	@Test
	public void testAddRowColumnsAddedToRowAfterRowAddedToTableAreIgnored() {
		Row row = new Row().add("ID", "1");
		Table table = new Table("PERSON")
				.add( row )
				.commit();
		
		row.add("FIRST_NAME", "NAME");
		
		Column[] columns = table.getTableMetaData().getColumns();
		Assert.assertEquals(1, columns.length);
		Assert.assertEquals("ID", columns[0].getColumnName());
	}
	
	@Test
	public void testGetRowCountWithNoRows() {
		Table table = new Table("PERSON")
			.commit();
		Assert.assertEquals( 0, table.getRowCount() );
	}
	
	@Test(expected=DatabaseUnitRuntimeException.class)
	public void testGetValueWithoutCommit() throws DataSetException {
		new Table("PERSON").
			getValue(0, "ID");
	}
	
	@Test(expected=RowOutOfBoundsException.class)
	public void testGetValueWithInvalidNegativeRow() throws DataSetException {
		new Table("PERSON")
			.commit()
			.getValue(-1, "ID");
	}
	
	@Test(expected=RowOutOfBoundsException.class)
	public void testGetValueWithInvalidOutOfBoundsRow() throws DataSetException {
		new Table("PERSON")
			.commit()
			.getValue(1, "ID");
	}
	
	@Test(expected=NoSuchColumnException.class)
	public void testGetValueInvalidColumn() throws DataSetException {
		new Table("PERSON")
			.add(new Row().add("ID", "1"))
			.commit()
			.getValue(0, "NAME");
	}
	
	@Test
	public void testGetValue() throws DataSetException {
		Table table = new Table("PERSON")
			.add( new Row().add("ID", "1") )
			.add( new Row().add("FIRST_NAME", "FIRST") )
			.commit();
		Assert.assertEquals( "1", table.getValue( 0, "ID" ) );
		Assert.assertEquals( null, table.getValue( 0, "FIRST_NAME" ) );
		Assert.assertEquals( null, table.getValue( 1, "ID" ) );
		Assert.assertEquals( "FIRST", table.getValue( 1, "FIRST_NAME" ) );
	}
	
	@Test(expected=DatabaseUnitRuntimeException.class)
	public void testGetTableMetadataWithoutCommit() {
		new Table("PERSON")
			.getTableMetaData();
	}
	
	@Test
	public void testGetTableMetaDataTableNameCaseSensitive() {
		Table table = new Table("PeRsOn")
			.commit();
		Assert.assertEquals( "PeRsOn", table.getTableMetaData().getTableName() );
	}
	
	@Test
	public void testGetTableMetaDataColumnsEmpty() {
		Table table = new Table("PERSON")
			.commit();
		Assert.assertEquals( 0, table.getTableMetaData().getColumns().length );
	}
	
	@Test
	public void testGetTableMetaDataColumnsOrderedByInsertTime() throws DataSetException {
		Table table = new Table("PERSON")
			.add( new Row().add("ID", "1").add("FIRST_NAME", "FIRST").add("LAST_NAME", "LAST") )
			.commit();
		Column[] columns = table.getTableMetaData().getColumns();
		Assert.assertEquals( "ID", columns[0].getColumnName() );
		Assert.assertEquals( "FIRST_NAME", columns[1].getColumnName() );
		Assert.assertEquals( "LAST_NAME", columns[2].getColumnName() );
		
		//Asserts that index by column name also work.
		Assert.assertEquals( 0, table.getTableMetaData().getColumnIndex("ID") );
		Assert.assertEquals( 1, table.getTableMetaData().getColumnIndex("FIRST_NAME") );
		Assert.assertEquals( 2, table.getTableMetaData().getColumnIndex("LAST_NAME") );
	}
	
	@Test
	public void testGetTableMetaDataColumnsNewColumnsAutoDiscovery() {
		Table table = new Table("PERSON")
			.add( new Row().add("ID", "1").add("FIRST_NAME", "FIRST") )
			.add( new Row().add("LAST_NAME", "LAST") )
			.commit();
		Column[] columns = table.getTableMetaData().getColumns();
		Assert.assertEquals( "ID", columns[0].getColumnName() );
		Assert.assertEquals( "FIRST_NAME", columns[1].getColumnName() );
		Assert.assertEquals( "LAST_NAME", columns[2].getColumnName() );
	}
	
	@Test
	public void testGetTableMetaDataColumnNameUpperCase() {
		Table table = new Table("PERSON")
			.add( new Row().add("id", "1").add("FiRsT_nAmE", "First") )
			.add( new Row().add("Id", "2").add("FIRST_name", "First") )
			.commit();
		Column[] columns = table.getTableMetaData().getColumns();
		Assert.assertEquals(2, columns.length);
		Assert.assertEquals( "ID", columns[0].getColumnName() );
		Assert.assertEquals( "FIRST_NAME", columns[1].getColumnName() );
	}
	
	@Test
	public void testAddValueGeneratorAlsoInsertsColumnIntoTableMetaData() {
		Table table = new Table("PERSON")
			.addValueGenerator("SEX", newFixedValueGenerator("MALE"))
			.commit();
		Assert.assertEquals("SEX", table.getTableMetaData().getColumns()[0].getColumnName());
	}
	
	@Test
	public void testAddValueGenerator() throws DataSetException {
		Table table = new Table("PERSON")
			.addValueGenerator("DATE_OF_BIRTH", newFixedValueGenerator(new Date(0)))
		    .addValueGenerator("VERSION", newFixedValueGenerator(1))
		    .addValueGenerator("FIRSTNAME", newFixedValueGenerator(""))
		    .addValueGenerator("SEX", newFixedValueGenerator(null))
			.add(new Row().add("NAME", "Bob").add("BIRTHPLACE", "NEW YORK"))
			.commit();
		
		Assert.assertEquals( new Date(0),  table.getValue( 0, "DATE_OF_BIRTH" ) );
		Assert.assertEquals( 1,  table.getValue( 0, "VERSION" ) );
		Assert.assertEquals( "",  table.getValue( 0, "FIRSTNAME" ) );
		Assert.assertEquals( null,  table.getValue( 0, "SEX" ) );
	}
}
