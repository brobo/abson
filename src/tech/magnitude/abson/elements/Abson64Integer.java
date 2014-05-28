package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.OutputStream;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;

public class Abson64Integer implements Absonifyable {
	long value;

	public Abson64Integer(long value) {
		this.value = value;
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryInt64(value));
	}

	@Override
	public byte getBsonPrefix() {
		return 0x12;
	}

	@Override
	public String toJson() {
		return "" + value;
	}
	
	public long getValue() {
		return value;
	}
}
