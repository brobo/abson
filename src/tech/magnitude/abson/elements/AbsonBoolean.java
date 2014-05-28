package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.OutputStream;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;

public class AbsonBoolean implements Absonifyable {

	boolean value;
	
	public AbsonBoolean(boolean val) {
		this.value = val;
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryBoolean(value));
	}

	@Override
	public byte getBsonPrefix() {
		return 0x08;
	}

	@Override
	public String toJson() {
		return value ? "true" : "false";
	}
	
	public boolean getValue() {
		return value;
	}

}
