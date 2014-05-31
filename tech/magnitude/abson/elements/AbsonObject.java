package tech.magnitude.abson.elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import tech.magnitude.abson.AbsonDecomposable;
import tech.magnitude.abson.AbsonParseException;
import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonParser;
import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.JsonUtil;

public class AbsonObject extends LinkedHashMap<String, Absonifyable> implements Absonifyable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2925473832134304270L;

	public AbsonObject() {
		super();
	}
	
	public AbsonObject(Map<String, ? extends Absonifyable> map) {
		super(map);
	}
	
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
	
	public byte[] toBson() {
		try {
			return BsonUtil.getArray(this);
		} catch(Exception ex) {
			return null; // Shouldn't happen.
		}
	}

	@Override
	public byte getBsonPrefix() {
		return 0x03;
	}
	
	@Override
	public Object getValue() {
		return this;
	}
	
	public Absonifyable put(String key, Absonifyable abs) {
		if(abs == null)
			return put(key, new AbsonNull());
		else
			return super.put(key, abs);
	}
	
	public Absonifyable put(String key, String value) {
		if(value == null)
			return put(key, new AbsonNull());
		else
			return put(key, new AbsonString(value));
	}
	
	public Absonifyable put(String key, int value) {
		return put(key, new Abson32Integer(value));
	}
	
	public Absonifyable put(String key, long value) {
		return put(key, new Abson64Integer(value));
	}
	
	public Absonifyable put(String key, boolean value) {
		return put(key, new AbsonBoolean(value));
	}
	
	public Absonifyable put(String key, double value) {
		return put(key, new AbsonFloatingPoint(value));
	}
	
	public Absonifyable put(String key, Date date) {
		if(date == null)
			return put(key, new AbsonNull());
		else
			return put(key, new AbsonUTCDatetime(date));
	}
	
	public Absonifyable put(String key, Absonifyable[] arr) {
		if(arr == null)
			return put(key, new AbsonNull());
		else
			return put(key, new AbsonArray(arr));
	}
	public Absonifyable put(String key) {
		return put(key, new AbsonNull());
	}
	
	public Absonifyable put(String key, AbsonDecomposable object) {
		return this.put(key, object.decompose());
	}
	
	public Absonifyable put(String key, byte[] array) {
		return this.put(key, new AbsonBinary(array));
	}

	public Integer getInteger(String key) {
		return ((AbsonNumber<?>) get(key)).getIntValue();
	}
	
	public Long getLong(String key) {
		return ((AbsonNumber<?>) get(key)).getLongValue();
	}
	
	public Double getDouble(String key) {
		return ((AbsonNumber<?>) get(key)).getDoubleValue();
	}
	
	public AbsonArray getArray(String key) {
		return (AbsonArray) get(key).getValue();
	}
	
	public Boolean getBoolean(String key) {
		return (Boolean) get(key).getValue();
	}
	
	public String getString(String key) {
		return (String) get(key).getValue();
	}
	
	public AbsonObject getObject(String key) {
		return (AbsonObject) get(key).getValue();
	}
	
	public Date getDate(String key) {
		return (Date) get(key).getValue();
	}
	
	public byte[] getBinary(String key) {
		return (byte[]) get(key).getValue();
	}
	
	public String toJson() {
		return JsonUtil.getString(this, JsonPrintSettings.DEFAULT);
	}
	
	public String toJson(JsonPrintSettings settings) {
		return JsonUtil.getString(this, settings);
	}
	
	@Override
	public String toString() {
		return toJson(JsonPrintSettings.DEFAULT);
	}
	
	public static AbsonObject fromBson(InputStream stream) throws IOException {
		AbsonObject res = new AbsonObject();
		byte[] lengthArr = new byte[4];
		stream.read(lengthArr);
		int length = BsonUtil.fromBinaryInt32(lengthArr) - 4;
		byte[] subportion = new byte[length];
		stream.read(subportion);
		InputStream toRead = new ByteArrayInputStream(subportion);
		while (toRead.available() > 1) {
			int type = toRead.read();
			String name = BsonUtil.readCString(toRead);
			Absonifyable value;
			switch (type) {
			case 0x01:
				value = AbsonFloatingPoint.fromBson(toRead);
				break;
			case 0x02:
				value = AbsonString.fromBson(toRead);
				break;
			case 0x03:
				value = AbsonObject.fromBson(toRead);
				break;
			case 0x04:
				value = AbsonArray.fromBson(toRead);
				break;
			case 0x05:
				value = AbsonBinary.fromBson(toRead);
				break;
			case 0x08:
				value = AbsonBoolean.fromBson(toRead);
				break;
			case 0x09:
				value = AbsonUTCDatetime.fromBson(toRead);
				break;
			case 0x0a:
				value = AbsonNull.fromBson(toRead);
				break;
			case 0x10:
				value = Abson32Integer.fromBson(toRead);
				break;
			case 0x12:
				value = Abson64Integer.fromBson(toRead);
				break;
			default:
				value = new AbsonBoolean(false);
			}
			res.put(name, value);
		}
		return res;
	}
	
	public static AbsonObject fromBson(byte[] bson) throws IOException {
		return AbsonObject.fromBson(new ByteArrayInputStream(bson));
	}
	
	public static AbsonObject fromJson(String token) throws AbsonParseException {
		try {
			return new JsonParser(token).readObject();
		} catch(AbsonParseException ex) {
			throw ex;
		} catch(Exception ex) {
			return null;
		}
	}

	@Override
	public void toJson(Writer writer, JsonPrintSettings settings) throws IOException {
		if(settings.isMultiline()) {
			toMultilineJson(writer, settings);
			return;
		}
		
		final JsonPrintSettings nextSettings = settings.getNextLevel();
		
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
		
		final JsonPrintSettings nextSettings = settings.getNextLevel();
		
		int count = 0;
		for (Map.Entry<String, Absonifyable> entry : entrySet()) {
			JsonUtil.indent(writer, nextSettings.getStartIndent());
			
			writer.write("\"");
			writer.write(entry.getKey());
			writer.write(settings.hasWhitespace() ? "\": " : "\":");
			entry.getValue().toJson(writer, nextSettings);
			
			if(count != this.size() - 1)
				writer.write(",");
			writer.write("\n");
			
			count++;
		}
		
		JsonUtil.indent(writer, settings.getStartIndent());
		writer.write("}");
	}
}
