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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.Table;
import org.dbunit.dataset.builder.util.CaseInsensitiveStringPolicy;
import org.dbunit.dataset.builder.util.CaseSensitiveStringPolicy;
import org.dbunit.dataset.builder.util.IStringPolicy;

public class DataSetBuilder {

	private final List<IDataSet> additionalDataSets = new ArrayList<IDataSet>();

	private final List<Table> tables = new ArrayList<Table>();

	private final Map<String,Table> tablesByName = new HashMap<String, Table>();

	private final boolean caseSensitiveTableNames;

	private final IStringPolicy stringPolicy;

	public DataSetBuilder() throws DataSetException {
		this(false);
	}

	public DataSetBuilder(boolean caseSensitiveTableNames) throws DataSetException {
		this.caseSensitiveTableNames = caseSensitiveTableNames;
		this.stringPolicy = this.caseSensitiveTableNames ? new CaseSensitiveStringPolicy() : new CaseInsensitiveStringPolicy();
	}

	public DataSetBuilder add(Table table) {
		if( this.tablesByName.containsKey( this.stringPolicy.toKey( table.getTableName() ) ) ) {
			throw new IllegalArgumentException("Table already added.");
		}

		this.tables.add( table );
		this.tablesByName.put( this.stringPolicy.toKey( table.getTableName() ), table );
		return this;
	}

	public DataSetBuilder add( AbstractDataSetRowBuilder rowBuilder ) {
		if(rowBuilder == null) {
			throw new IllegalArgumentException("Row must not be null.");
		}
		if( rowBuilder.getTableName() == null || rowBuilder.getTableName().trim().isEmpty() ) {
			throw new IllegalArgumentException("Table name of the row must not be null or empty.");
		}

		Table table = this.tablesByName.get( this.stringPolicy.toKey( rowBuilder.getTableName() ) );

		if( table == null ) {
			this.add( new Table( rowBuilder.getTableName() ).add( rowBuilder.build() ) );
		} else {
			if( !table.isCommitted() ) {
				table.add( rowBuilder.build() );
			}
		}
		return this;
	}
	
	public DataSetBuilder add( IDataSet newDataSet ) {
		this.additionalDataSets.add( newDataSet );
		return this;
	}

	public IDataSet build() throws DataSetException {

		for (Table table : this.tables) {
			if( !table.isCommitted() ) {
				table.commit();
			}
		}

		IDataSet dataSet = new DefaultDataSet( this.tables.toArray(new Table[0]), this.caseSensitiveTableNames );

		if( !this.additionalDataSets.isEmpty() ) {
			/*
			 * Ensures the dataset built here comes before additional ones.
			 * //TODO improvement - allow user to choose in which position the data setbuilt here must come among additional ones.
			 */
			List<IDataSet> datasets = new ArrayList<IDataSet>();
			datasets.add( dataSet );
			datasets.addAll( this.additionalDataSets );
			dataSet = new CompositeDataSet( datasets.toArray(new IDataSet[0]) );
		}

		return dataSet;
	}
}