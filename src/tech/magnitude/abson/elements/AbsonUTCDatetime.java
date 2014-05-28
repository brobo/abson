package tech.magnitude.abson.elements;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;

public class AbsonUTCDatetime implements Absonifyable {
	long value;

	public AbsonUTCDatetime(long value) {
		this.value = value;
	}
	
	public AbsonUTCDatetime(Date date) {
		value = date.getTime();
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryInt64(value));
	}

	@Override
	public byte getBsonPrefix() {
		return 0x09;
	}

	@Override
	public String toJson() {
		return "" + value;
	}
	
	public Date getValue() {
		return new Date(value);
	}
}
