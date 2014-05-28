package tech.magnitude.abson.elements;
import java.io.IOException;
import java.io.OutputStream;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;

public class Abson32Integer implements Absonifyable {

	int value;
	
	public Abson32Integer(int value) {
		this.value = value;
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryInt32(value));
	}

	@Override
	public byte getBsonPrefix() {
		return 0x10;
	}

	@Override
	public String toJson() {
		return "" + value;
	}
	
	public int getValue() {
		return value;
	}
}
