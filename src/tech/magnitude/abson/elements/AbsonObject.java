package tech.magnitude.abson.elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;

public class AbsonObject extends LinkedHashMap<String, Absonifyable> implements Absonifyable {

	public void toBson(OutputStream stream) throws IOException {
		ByteArrayOutputStream temp = new ByteArrayOutputStream();
		for (Map.Entry<String, Absonifyable> entry : entrySet()) {
			temp.write(entry.getValue().getBsonPrefix());
			temp.write(BsonUtil.toBinaryCString(entry.getKey()));
			entry.getValue().toBson(temp);
		}
		stream.write(BsonUtil.toBinaryInt32(temp.size()));
		stream.write(temp.toByteArray());
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

	@Override
	public String toJson() {
		String res = "{";
		for (Map.Entry<String, Absonifyable> entry : entrySet()) {
			res += entry.getKey() + ":" + entry.getValue().toJson() + ",";
		}
		if (res.length() > 1) {
			res = res.substring(0, res.length()-1);
		}
		res += "}";
		return res;
	}
}
