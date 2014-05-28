package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.OutputStream;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;

public class AbsonFloatingPoint implements Absonifyable {

	double value;
	
	public AbsonFloatingPoint(double value) {
		this.value = value;
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryFloatingPoint(value));
	}

	@Override
	public byte getBsonPrefix() {
		return 0x01;
	}

	@Override
	public String toJson() {
		return "" + value;
	}

	public double getValue() {
		return value;
	}
}
