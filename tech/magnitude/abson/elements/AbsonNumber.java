package tech.magnitude.abson.elements;

import java.math.BigInteger;

import tech.magnitude.abson.AbsonValue;

public interface AbsonNumber<T> extends AbsonValue {
	public int getIntValue();
	public long getLongValue();
	public BigInteger getBigIntegerValue();
	public double getDoubleValue();
}
