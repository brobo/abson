package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import tech.magnitude.abson.AbsonConstants;
import tech.magnitude.abson.AbsonValue;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.JsonUtil;

public class AbsonBinary implements AbsonValue {

	byte[] data;
	
	public AbsonBinary(byte[] data) {
		this.data = data;
	}
	
	@Override
	public void toJson(Writer writer, JsonPrintSettings settings) throws IOException {
		writer.write(AbsonConstants.BINARY_OPENER + "" + AbsonConstants.STRING_DELIMITER);
		JsonUtil.writeBase64(writer, data);
		writer.write(AbsonConstants.STRING_DELIMITER);
	}
	
	public String toJson() {
		return toJson(JsonPrintSettings.DEFAULT);
	}
	
	public String toJson(JsonPrintSettings settings) {
		return JsonUtil.getString(this, settings);
	}
	
	@Override
	public String toString() {
		return toJson();
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
	public byte[] getValue() {
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
