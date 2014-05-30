package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;

public class AbsonBinary implements Absonifyable {

	byte[] data;
	
	public AbsonBinary(byte[] data) {
		this.data = data;
	}
	
	@Override
	public void toJson(Writer writer, JsonPrintSettings settings) throws IOException {
		
	}

	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryInt32(data.length));
		stream.write(0x00);
		stream.write(data);
	}

	@Override
	public byte getBsonPrefix() {
		return 0x05;
	}

	@Override
	public Object getValue() {
		return data;
	}
	
	public static AbsonBinary fromBson(InputStream stream) throws IOException {
		byte[] lengthArr = new byte[4];
		stream.read(lengthArr);
		int length = BsonUtil.fromBinaryInt32(lengthArr);
		byte[] data = new byte[length];
		stream.read(data);
		return new AbsonBinary(data);
	}

}
