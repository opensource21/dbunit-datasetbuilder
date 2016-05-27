package org.dbunit.dataset.builder;

import static org.dbunit.dataset.builder.RawDataSetRowBuilder.newRawRow;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.NoSuchTableException;
import org.dbunit.dataset.Table;
import org.junit.Assert;
import org.junit.Test;

public class DataSetBuilderTest {

	@Test
	public void addsEmptyTables() throws Exception {
		IDataSet dataSet = new DataSetBuilder()
				.add(new Table("PERSON"))
				.add(new Table("ADDRESS"))
				.build();

		assertEquals(0, dataSet.getTable("PERSON").getRowCount());
		assertEquals(0, dataSet.getTable("ADDRESS").getRowCount());
	}
	
	@Test
	public void caseInsensitiveTableNamesByDefault() throws Exception {
		IDataSet dataSet = new DataSetBuilder()
				.add( new Table("PeRsoN"))
				.build();

		assertNotNull( dataSet.getTable("Person") );
	}

	@Test
	public void caseSensitiveTableNamesWhenSpecified() throws Exception {
		IDataSet dataSet = new DataSetBuilder( true )
				.add( new Table("PeRsoN"))
				.build();

		assertNotNull( dataSet.getTable("PeRsoN") );
		
		try {
			dataSet.getTable("Person");
			Assert.fail("Should had thrown exception.");
		} catch( NoSuchTableException e ) {
			//success.
		}
	}

	@Test
	public void tablesAreKeptInOrder() throws Exception {
		IDataSet dataSet = new DataSetBuilder()
				.add( new Table("PERSON"))
				.add( new Table("ADDRESS"))
				.add( new Table("_TABLE_"))
				.build();

		assertArrayEquals(new String[]{"PERSON", "ADDRESS", "_TABLE_"}, dataSet.getTableNames());
	}
	
	@Test
	public void addsDataForSingleRow() throws Exception {
		IDataSet dataSet = new DataSetBuilder()
				.add( newRawRow("PERSON").with("NAME", "Bob").with("AGE", 18) )
				.build();

		ITable table = dataSet.getTable("PERSON");
		assertEquals(1, table.getRowCount());
		assertEquals("Bob", table.getValue(0, "NAME"));
		assertEquals(18, table.getValue(0, "AGE"));
	}

	@Test
	public void addsDataForMultipleRowsOfDifferentTables() throws Exception {
		IDataSet dataSet = new DataSetBuilder()
				.add( newRawRow("PERSON").with("NAME", "Bob").with("AGE", 18) )
				.add( newRawRow("ADDRESS").with("STREET", "Main Street").with("NUMBER", 42) )
				.add( newRawRow("PERSON").with("NAME", "Alice").with("AGE", 23) )
				.build();

		ITable table = dataSet.getTable("PERSON");
		assertEquals(2, table.getRowCount());
		assertEquals("Bob", table.getValue(0, "NAME"));
		assertEquals(18, table.getValue(0, "AGE"));
		assertEquals("Alice", table.getValue(1, "NAME"));
		assertEquals(23, table.getValue(1, "AGE"));

		table = dataSet.getTable("ADDRESS");
		assertEquals(1, table.getRowCount());
		assertEquals("Main Street", table.getValue(0, "STREET"));
		assertEquals(42, table.getValue(0, "NUMBER"));
	}

	@Test
	public void addsNewColumnsOnTheFly() throws Exception {
		IDataSet dataSet = new DataSetBuilder()
				.add( newRawRow("PERSON").with("NAME", "Bob") )
				.add( newRawRow("PERSON").with("AGE", 18) )
				.build();

		ITable table = dataSet.getTable("PERSON");
		assertEquals(2, table.getRowCount());
		assertEquals("Bob", table.getValue(0, "NAME"));
		assertNull(table.getValue(0, "AGE"));
		assertNull(table.getValue(1, "NAME"));
		assertEquals(18, table.getValue(1, "AGE"));
	}

	@Test
	public void addDataSet() throws Exception {
		IDataSet dataSet = new DataSetBuilder()
				.add( newRawRow("PERSON").with("NAME", "Bob").with("AGE", 18) )
				.build();

		ITable table = dataSet.getTable("PERSON");

		assertEquals(1, table.getRowCount());

		IDataSet dataSet2 = new DataSetBuilder()
				.add( newRawRow("PERSON").with("NAME", "John").with("AGE", 19) )
				.add( dataSet )
				.build();

		ITable table2 = dataSet2.getTable("PERSON");

		assertEquals(2, table2.getRowCount());
		assertEquals("John", table2.getValue(0, "NAME"));
		assertEquals(19, table2.getValue(0, "AGE"));

		assertEquals("Bob", table2.getValue(1, "NAME"));
		assertEquals(18, table2.getValue(1, "AGE"));
	}
	
	@Test
	public void columnIndexesAreKeptInOrder() throws DataSetException {
		IDataSet dataSet = new DataSetBuilder()
				.add( newRawRow("PERSON").with("NAME", "Bob") )
				.add( newRawRow("PERSON").with("AGE", 18).with("NAME", "Alice") )
				.add( newRawRow("PERSON").with("SEX", "MALE").with("NAME", "Richard") )
				.build();

		ITable table = dataSet.getTable("PERSON");
		ITableMetaData tableMetaData = table.getTableMetaData();
		
		assertEquals(0, tableMetaData.getColumnIndex("NAME"));
		assertEquals(1, tableMetaData.getColumnIndex("AGE"));
		assertEquals(2, tableMetaData.getColumnIndex("SEX"));
	}
}