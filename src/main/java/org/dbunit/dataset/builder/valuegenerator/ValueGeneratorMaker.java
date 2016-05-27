package org.dbunit.dataset.builder.valuegenerator;

/**
 * Facade for constructors calls of concrete implementations for {@link ValueGenerator}.   
 */
public class ValueGeneratorMaker {

	public static <T> FixedValueGenerator<T> newFixedValueGenerator(T value) {
		return new FixedValueGenerator<T>( value );
	}
}
