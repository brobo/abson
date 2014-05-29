package tech.magnitude.abson.elements;

import java.math.BigInteger;

import tech.magnitude.abson.Absonifyable;

public interface AbsonNumber<T> extends Absonifyable {
	public int getIntValue();
	public long getLongValue();
	public BigInteger getBigIntegerValue();
	public double getDoubleValue();
}
