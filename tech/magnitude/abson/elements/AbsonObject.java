package tech.magnitude.abson.elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.PrintUtil;

public class AbsonObject extends LinkedHashMap<String, Absonifyable> implements Absonifyable {

	public void toBson(OutputStream stream) throws IOException {
		ByteArrayOutputStream temp = new ByteArrayOutputStream();
		for (Map.Entry<String, Absonifyable> entry : entrySet()) {
			temp.write(entry.getValue().getBsonPrefix());
			temp.write(BsonUtil.toBinaryCString(entry.getKey()));
			entry.getValue().toBson(temp);
		}
		stream.write(BsonUtil.toBinaryInt32(temp.size() + 5));
		stream.write(temp.toByteArray());
		stream.write(0x00);
	}

	@Override
	public byte getBsonPrefix() {
		return 0x03;
	}
	
	public void put(String key, String value) {
		put(key, new AbsonString(value));
	}
	
	public void put(String key, int value) {
		put(key, new Abson32Integer(value));
	}
	
	public void put(String key, long value) {
		put(key, new Abson64Integer(value));
	}
	
	public void put(String key, boolean value) {
		put(key, new AbsonBoolean(value));
	}
	
	public void put(String key, double value) {
		put(key, new AbsonFloatingPoint(value));
	}
	
	public void put(String key, Date date) {
		put(key, new AbsonUTCDatetime(date));
	}
	
	public void put(String key, Absonifyable[] arr) {
		put(key, new AbsonArray(arr));
	}
	
	public int getInteger(String key) {
		return ((Abson32Integer) get(key)).getValue();
	}
	
	public long getLong(String key) {
		return ((Abson64Integer) get(key)).getValue();
	}
	
	public double getDouble(String key) {
		return ((AbsonFloatingPoint) get(key)).getValue();
	}
	
	public AbsonArray getArray(String key) {
		return (AbsonArray) get(key);
	}
	
	public boolean getBoolean(String key) {
		return ((AbsonBoolean) get(key)).getValue();	
	}
	
	public String getString(String key) {
		return ((AbsonString) get(key)).getValue();
	}
	
	public Date getDate(String key) {
		return ((AbsonUTCDatetime) get(key)).getValue();
	}
	
	public String toJson() {
		return PrintUtil.getString(this, JsonPrintSettings.DEFAULT);
	}
	
	public String toJson(JsonPrintSettings settings) {
		return PrintUtil.getString(this, settings);
	}
	
	@Override
	public String toString() {
		return toJson(JsonPrintSettings.DEFAULT);
	}

	@Override
	public void toJson(Writer writer, JsonPrintSettings settings) throws IOException {
		if(settings.isMultiline()) {
			toMultilineJson(writer, settings);
			return;
		}
		
		final JsonPrintSettings nextSettings = new JsonPrintSettings(settings, settings.getIndent() + settings.getStartIndent());
		
		writer.write(settings.hasWhitespace() ? "{ " : "{");
		
		boolean first = true;
		for (Map.Entry<String, Absonifyable> entry : entrySet()) {
			if(!first) writer.write(settings.hasWhitespace() ? ", " : ",");
			writer.write("\"");
			writer.write(entry.getKey());
			writer.write(settings.hasWhitespace() ? "\": " : "\":");
			entry.getValue().toJson(writer, nextSettings);
			first = false;
		}
		
		writer.write(settings.hasWhitespace() ? " }" : "}");
	}
	
	protected void toMultilineJson(Writer writer, JsonPrintSettings settings) throws IOException {
		writer.write("{\n");
		
		final JsonPrintSettings nextSettings = new JsonPrintSettings(settings, settings.getIndent() + settings.getStartIndent());
		
		int count = 0;
		for (Map.Entry<String, Absonifyable> entry : entrySet()) {
			PrintUtil.indent(writer, nextSettings.getStartIndent());
			
			writer.write("\"");
			writer.write(entry.getKey());
			writer.write(settings.hasWhitespace() ? "\": " : "\":");
			entry.getValue().toJson(writer, nextSettings);
			
			if(count != this.size() - 1)
				writer.write(",");
			writer.write("\n");
		}
		
		PrintUtil.indent(writer, settings.getStartIndent());
		writer.write("}");
	}
}
