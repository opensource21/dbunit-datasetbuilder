package org.dbunit.dataset;

import org.dbunit.dataset.builder.CaseInsensitiveStringPolicyTest;
import org.dbunit.dataset.builder.CaseSensitiveStringPolicyTest;
import org.dbunit.dataset.builder.DataSetBuilderIntegrationTest;
import org.dbunit.dataset.builder.DataSetBuilderTest;
import org.dbunit.dataset.builder.DataSetRowChangerTest;
import org.dbunit.dataset.builder.TableMetaDataBuilderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	CaseSensitiveStringPolicyTest.class,
	CaseInsensitiveStringPolicyTest.class,
	DataSetBuilderIntegrationTest.class,
	DataSetBuilderTest.class,
	DataSetRowChangerTest.class,
	TableMetaDataBuilderTest.class,
	TableTest.class,
	RowTest.class
})
public class AllTests {}