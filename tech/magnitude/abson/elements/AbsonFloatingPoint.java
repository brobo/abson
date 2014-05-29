package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;

public class AbsonFloatingPoint implements Absonifyable {

	protected double value;
	
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
	public void toJson(Writer writer, JsonPrintSettings settings) throws IOException {
		writer.write(toJson(settings));
	}
	
	public String toJson() {
		return toJson(JsonPrintSettings.DEFAULT);
	}
	
	public String toJson(JsonPrintSettings settings) {
		return settings.shouldRoundFractions() ? String.format("%." + settings.getRoundAmount() + "f", value) : Double.toString(value);
	}
	
	public static AbsonFloatingPoint fromBson(InputStream stream) throws IOException {
		byte[] bytes = new byte[8];
		stream.read(bytes);
		return new AbsonFloatingPoint(BsonUtil.fromBinaryFloatingPoint(bytes));
	}
	
	@Override
	public String toString() {
		return toJson();
	}

	public double getValue() {
		return value;
	}
}
