package org.dbunit.dataset;

import org.dbunit.dataset.builder.ColumnSpec;
import org.junit.Assert;
import org.junit.Test;

public class RowTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void addsDataWithNullColumnName() {
		new Row().add((String)null, "Bob");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addsDataWithNullColumnSpec() {
		new Row().add((ColumnSpec<String>)null, "Bob");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addsDataWithNullColumnSpecName() {
		new Row().add(ColumnSpec.newColumn(null), "Bob");
	}
	
	@Test
	public void addsDataWithColumnNameCaseInsensitive() {

		Row row = new Row().add("NAME", "Bob").add("AGE", 18);

		Assert.assertEquals("Bob", row.get("Name"));
		Assert.assertEquals(18, row.get("Age"));
	}
	
	@Test
	public void addsDataWithColumnSpecCaseInsensitive() {

		ColumnSpec<String> name = ColumnSpec.newColumn("NAME");
		ColumnSpec<Integer> age = ColumnSpec.newColumn("AGE");

		Row row = new Row().add(name, "Bob").add(age, 18);

		Assert.assertEquals("Bob", row.get("Name"));
		Assert.assertEquals(18, row.get("Age"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getWithNullColumnName() {
		new Row().get(null);
	}
}

