package org.dbunit.dataset.builder.example;

import static org.dbunit.dataset.builder.ColumnSpec.newColumn;
import static org.dbunit.dataset.builder.RawDataSetRowBuilder.newRawRow;

import java.io.PrintWriter;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.builder.ColumnSpec;
import org.dbunit.dataset.builder.DataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSetWriter;

public class DataSetBuilderExample {

	private static final ColumnSpec<String> NAME = newColumn("NAME");
	private static final ColumnSpec<Integer> AGE = ColumnSpec.newColumn("AGE");

    public static void main(String[] args) throws Exception {

    	IDataSet dataSet = new DataSetBuilder()

        // Using strings as column names, not type-safe
        .add( newRawRow("PERSON").with("NAME", "Bob").with("AGE", 18) )

         // Using ColumnSpecs to identify columns, type-safe!
        .add( newRawRow("PERSON").with(NAME, "Alice").with(AGE, 23) )

        // New columns are added on the fly
        .add( newRawRow("PERSON").with("NAME", "Charlie").with("LAST_NAME", "Brown") )
        .build();

        new XmlDataSetWriter(new PrintWriter(System.out)).write(dataSet);
    }

}
