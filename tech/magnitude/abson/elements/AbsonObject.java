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

import tech.magnitude.abson.AbsonComposer;
import tech.magnitude.abson.AbsonConstants;
import tech.magnitude.abson.AbsonDecomposable;
import tech.magnitude.abson.AbsonParseException;
import tech.magnitude.abson.AbsonValue;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonParser;
import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.JsonUtil;

public class AbsonObject extends LinkedHashMap<String, AbsonValue> implements AbsonValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2925473832134304270L;

	public static AbsonObject fromIntMap(Map<Integer, ? extends AbsonDecomposable> map) {
		AbsonObject res = new AbsonObject();
		
		for(Map.Entry<Integer, ? extends AbsonDecomposable> obj : map.entrySet())
			res.put("" + obj.getKey(), obj.getValue());
		
		return res;
	}
	
	public static AbsonObject fromMap(Map<String, ? extends AbsonDecomposable> map) {
		AbsonObject res = new AbsonObject();
		
		for(Map.Entry<String, ? extends AbsonDecomposable> obj : map.entrySet())
			res.put(obj.getKey(), obj.getValue());
		
		return res;
	}
	
	
	public AbsonObject() {
		super();
	}
	
	public AbsonObject(AbsonObject old) {
		super(old);
	}
	
	public void toBson(OutputStream stream) throws IOException {
		ByteArrayOutputStream temp = new ByteArrayOutputStream();
		
		for (Map.Entry<String, AbsonValue> entry : entrySet()) {
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
	
	public AbsonValue put(String key, AbsonValue abs) {
		if(abs == null || (!(abs instanceof AbsonNull) && abs.getValue() == null))
			return put(key, new AbsonNull());
		else
			return super.put(key, abs);
	}
	
	public AbsonValue put(String key, String value) {
		return put(key, new AbsonString(value));
	}
	
	public AbsonValue put(String key, int value) {
		return put(key, new Abson32Integer(value));
	}
	
	public AbsonValue put(String key, long value) {
		return put(key, new Abson64Integer(value));
	}
	
	public AbsonValue put(String key, boolean value) {
		return put(key, new AbsonBoolean(value));
	}
	
	public AbsonValue put(String key, double value) {
		return put(key, new AbsonFloatingPoint(value));
	}
	
	public AbsonValue put(String key, Date date) {
		return put(key, new AbsonUTCDatetime(date));
	}
	
	public AbsonValue put(String key) {
		return put(key, new AbsonNull());
	}
	
	public AbsonValue put(String key, AbsonDecomposable object) {
		if(object == null)
			return put(key, new AbsonNull());
		else
			return put(key, object.decompose());
	}
	
	public AbsonValue put(String key, byte[] array) {
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
	
	/**
	 * Creates a map based on the information contained in this AbsonObject;
	 * all values are casted to the specified class, if possible.
	 * 
	 * This function can internally convert numerical values (eg Long -> Integer).
	 * @param input The map to fill.
	 */
	@SuppressWarnings("unchecked")
	public <T> void fillMap(Map<String, T> input, Class<T> targetType) {
		for(Map.Entry<String, AbsonValue> values : this.entrySet()) {
			AbsonValue object = values.getValue();
			Object value = object.getValue();
			T res = object instanceof AbsonNumber ? JsonUtil.castNumber((Number) value, targetType) : targetType.cast(value);
			input.put(values.getKey(), res);
		}
	}
	
	/**
	 * Filles the provided map based on the information contained in this AbsonObject;
	 * all values are casted to the specified class, if possible; the keys are
	 * parsed as integers.
	 * @param input The map to fill.
	 */
	@SuppressWarnings("unchecked")
	public <T> void fillIntMap(Map<Integer, T> input, Class<T> targetType) {
		for(Map.Entry<String, AbsonValue> values : this.entrySet()) {
			AbsonValue object = values.getValue();
			Object value = object.getValue();
			T res = object instanceof AbsonNumber ? JsonUtil.castNumber((Number) value, targetType) : targetType.cast(value);
			input.put(Integer.parseInt(values.getKey()), res);
		}
	}
	
	/**
	 * Fills the provided map with key value pairs created from applying
	 * the composer on the AbsonObjects contained in this object.
	 * Any objects which are not AbsonObjects are ignored. 
	 * @param input The map to fill.
	 * @param composer The composer to be applied to the AbsonObjects.
	 */
	public <T extends AbsonDecomposable> void fillMap(Map<String, T> input, AbsonComposer<T> composer) {
		for(Map.Entry<String, AbsonValue> values : this.entrySet())
			if(values.getValue() instanceof AbsonObject) input.put(values.getKey(), composer.compose((AbsonObject) values.getValue()));
	}
	
	/**
	 * Fills the provided map with key value pairs created from applying
	 * the composer on the AbsonObjects contained in this object, parsing the keys as integers.
	 * Any objects which are not AbsonObjects are ignored. 
	 * @param input The map to fill.
	 * @param composer The composer to be applied to the AbsonObjects.
	 */
	public <T extends AbsonDecomposable> void fillIntMap(Map<Integer, T> input, AbsonComposer<T> composer) {
		for(Map.Entry<String, AbsonValue> values : this.entrySet())
			if(values.getValue() instanceof AbsonObject) input.put(Integer.parseInt(values.getKey()), composer.compose((AbsonObject) values.getValue()));
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
			AbsonValue value;
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
		if(settings.isMultiline() && this.size() > 1) {
			toMultilineJson(writer, settings);
			return;
		}
		
		final JsonPrintSettings nextSettings = settings.getNextLevel();
		
		writer.write(AbsonConstants.OPENING_BRACKET);
		writer.write(settings.hasWhitespace() ? " " : "");
		
		boolean first = true;
		for (Map.Entry<String, AbsonValue> entry : entrySet()) {
			if(!first) {
				writer.write(AbsonConstants.ENTRY_SEPERATOR);
				if(settings.hasWhitespace()) writer.write(" ");
			}
			
			writer.write(AbsonConstants.STRING_DELIMITER);
			writer.write(entry.getKey());
			
			writer.write(AbsonConstants.STRING_DELIMITER + "" + AbsonConstants.KEY_VALUE_SEPERATOR);
			if(settings.hasWhitespace()) writer.write(" ");
			
			entry.getValue().toJson(writer, nextSettings);
			first = false;
		}
		
		if(settings.hasWhitespace()) writer.write(" ");
		writer.write(AbsonConstants.CLOSING_BRACKET);
	}
	
	protected void toMultilineJson(Writer writer, JsonPrintSettings settings) throws IOException {
		writer.write(AbsonConstants.OPENING_BRACKET + "\n");
		
		final JsonPrintSettings nextSettings = settings.getNextLevel();
		
		int count = 0;
		for (Map.Entry<String, AbsonValue> entry : entrySet()) {
			JsonUtil.indent(writer, nextSettings.getStartIndent());
			
			writer.write(AbsonConstants.STRING_DELIMITER);
			writer.write(entry.getKey());
			writer.write(AbsonConstants.STRING_DELIMITER + "" + AbsonConstants.KEY_VALUE_SEPERATOR);
			if(settings.hasWhitespace()) writer.write(" ");
			
			entry.getValue().toJson(writer, nextSettings);
			
			if(count != this.size() - 1)
				writer.write(AbsonConstants.ENTRY_SEPERATOR);
			writer.write("\n");
			
			count++;
		}
		
		JsonUtil.indent(writer, settings.getStartIndent());
		writer.write(AbsonConstants.CLOSING_BRACKET);
	}
}
