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

import static org.dbunit.dataset.builder.DataSetBuilderIntegrationTest.PERSONRowBuilder.newPERSON;
import static org.dbunit.dataset.builder.RawDataSetRowBuilder.newRawRow;
import static org.dbunit.dataset.builder.valuegenerator.ValueGeneratorMaker.newFixedValueGenerator;

import java.sql.Date;

import org.dbunit.Assertion;
import org.dbunit.dataset.CustomTableMetaData;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.Row;
import org.dbunit.dataset.Table;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the class {@link DataSetBuilder}.
 * @author niels (linux-java AT users.sourceforge.net)
 * @author Last changed by: niels
 * @version 02.01.2014
 * @since 2.4.10
 *
 */
public class DataSetBuilderIntegrationTest {

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for {@link org.dbunit.dataset.builder.DataSetBuilder#build()}.
     */
    @Test
    public void testBuild() throws Exception {
    	//raw builder with default values.
		IDataSet actual = new DataSetBuilder()
			.add(new Table("PERSON")
					.addValueGenerator("DATE_OF_BIRTH", newFixedValueGenerator(new Date(0)))
			        .addValueGenerator("NAME", newFixedValueGenerator(""))
			        .addValueGenerator("VERSION", newFixedValueGenerator(0L))
			        .addValueGenerator("FIRSTNAME", newFixedValueGenerator(""))
			        .addValueGenerator("ID", newFixedValueGenerator(0L))
			        .addValueGenerator("SEX", newFixedValueGenerator(null))
			        .addValueGenerator("BIRTHPLACE", newFixedValueGenerator(""))
        			.add(new Row().add("NAME", "Bob").add("BIRTHPLACE", "NEW YORK"))
					.add(new Row().add("NAME", "Alice").add("BIRTHPLACE", "London")))
			.add(new Table("ADDRESS")
	    			.add(new Row().add("STREET", "Main Street").add("NUMBER", 42)))
			.build();
		
		//table-specific builder approach.
		IDataSet actual2 = new DataSetBuilder()
			.add( newPERSON().NAME("Bob").BIRTHPLACE("NEW YORK") )
			.add( newPERSON().NAME("Alice").BIRTHPLACE("London") )
			.add( newRawRow("ADDRESS").with("STREET", "Main Street").with("NUMBER", 42) )
			.build();

        ReplacementDataSet expected = new ReplacementDataSet(new FlatXmlDataSetBuilder().build(
                this.getClass().getResourceAsStream("/reference.xml")));
        expected.addReplacementObject("[EPOCH_TIME]", new Date(0));
        expected.addReplacementObject("[NULL]", null);

        Assertion.assertEquals(expected, actual);
        
        Assertion.assertEquals(expected, actual2);
    }
    
    @Test
    public void testBuildCaseSensitive() throws Exception {
    	final IDataSet actual = new DataSetBuilder( true )
        		.add( newRawRow("ADDRESS").with("STREET", "Main Street").with("NUMBER", 42) )
        		.add( newRawRow("ADDREss").with("STREET", "Main Street 2").with("NUMBER", 43) )
        		.build();

        FlatXmlDataSet expected = new FlatXmlDataSetBuilder()
        		.setCaseSensitiveTableNames(true)
        		.build(this.getClass().getResourceAsStream("/reference_case_sensitive.xml"));

        Assertion.assertEquals(expected, actual);
    }

    static class PERSONRowBuilder extends AbstractDataSetRowBuilder {

        public static final String TABLE_NAME = "PERSON";

        private static final String C_ID = "ID";
        private static final String C_NAME = "NAME";
        private static final String C_FIRSTNAME = "FIRSTNAME";
        private static final String C_SEX = "SEX";
        private static final String C_DATE_OF_BIRTH = "DATE_OF_BIRTH";
        private static final String C_BIRTHPLACE = "BIRTHPLACE";
        private static final String C_VERSION = "VERSION";
        
        public static final CustomTableMetaData PERSON_TABLE_META_DATA;
        
        static {
        	PERSON_TABLE_META_DATA = new TableMetaDataBuilder( TABLE_NAME )
        			.addValueGenerator( C_ID, newFixedValueGenerator( 0L ) )
        			.addValueGenerator( C_NAME, newFixedValueGenerator("") )
        			.addValueGenerator( C_FIRSTNAME, newFixedValueGenerator("") )
		        	.addValueGenerator( C_SEX, newFixedValueGenerator( null ) )
		        	.addValueGenerator( C_DATE_OF_BIRTH, newFixedValueGenerator( new Date(0) ) )
		        	.addValueGenerator( C_BIRTHPLACE, newFixedValueGenerator("") )
		        	.addValueGenerator( C_VERSION, newFixedValueGenerator( 0L ) )
        			.build();
        }
        
        public static PERSONRowBuilder newPERSON() {
            return new PERSONRowBuilder();
        }
        
        public final PERSONRowBuilder ID (Long value) {
            return super._with(C_ID, value);
        }

        public final PERSONRowBuilder NAME (String value) {
            return super._with(C_NAME, value);
        }
        
        public final PERSONRowBuilder FIRSTNAME (String value) {
            return super._with(C_FIRSTNAME, value);
        }
        
        public final PERSONRowBuilder SEX (Integer value) {
            return super._with(C_SEX, value);
        }
        
        public final PERSONRowBuilder DATE_OF_BIRTH (Date value) {
            return super._with(C_DATE_OF_BIRTH, value);
        }

        public final PERSONRowBuilder BIRTHPLACE (String value) {
            return super._with(C_BIRTHPLACE, value);
        }

        public final PERSONRowBuilder VERSION (Long value) {
            return super._with(C_VERSION, value);
        }
        
        @Override
        protected CustomTableMetaData getTableMetaData() {
        	return PERSON_TABLE_META_DATA;
        }
    }
}