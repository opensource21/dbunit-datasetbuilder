package org.dbunit.dataset.builder.valuegenerator;

public class FixedValueGenerator<T> implements ValueGenerator<T> {

	private T value;

	public FixedValueGenerator( T value ) {
		this.value = value;
	}
	
	@Override
	public T nextValue() {
		return value;
	}
}
