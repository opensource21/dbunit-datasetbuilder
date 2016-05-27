package org.dbunit.dataset;

public interface ITableManipulator {

	Table add(Row row);
	Table commit();
	boolean isCommitted();
}
