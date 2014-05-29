package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import tech.magnitude.abson.AbsonParseException;
import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonParser;
import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.JsonUtil;

public class AbsonArray extends ArrayList<Absonifyable> implements Absonifyable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5680398373817754508L;

	public AbsonArray() {
		super();
	}
	
	public AbsonArray(Collection<? extends Absonifyable> collection) {
		super(collection);
	}
	
	public AbsonArray(int initialCapacity) {
		super(initialCapacity);
	}
	
	public AbsonArray(Absonifyable[] initialArray) {
		super(Arrays.asList(initialArray));
	}
	
	public boolean add(Absonifyable obj) {
		if(obj == null)
			return super.add(new AbsonNull());
		else
			return super.add(obj);
	}
	
	public boolean add(int val) {
		return super.add(new Abson32Integer(val));
	}
	
	public boolean add(long val) {
		return super.add(new Abson64Integer(val));
	}
	
	public boolean add(String val) {
		if(val == null)
			return super.add(new AbsonNull());
		else
			return super.add(new AbsonString(val));
	}
	
	public boolean add(double val) {
		return super.add(new AbsonFloatingPoint(val));
	}
	
	public boolean add(Date date) {
		if(date == null)
			return super.add(new AbsonNull());
		else
			return super.add(new AbsonUTCDatetime(date));
	}
	
	public boolean add(boolean value) {
		return add(new AbsonBoolean(value));
	}
	
	public boolean add(AbsonArray array) {
		if(array == null)
			return super.add(new AbsonNull());
		else
			return super.add(array);
	}
	
	public int getInteger(int index) {
		return ((AbsonNumber<?>) get(index)).getIntValue();
	}
	
	public long getLong(int index) {
		return ((AbsonNumber<?>) get(index)).getLongValue();
	}
	
	public String getString(int index) {
		return (String) get(index).getValue();
	}
	
	public double getDouble(int index) {
		return ((AbsonNumber<?>) get(index)).getDoubleValue();
	}
	
	public Date getDate(int index) {
		return (Date) get(index).getValue();
	}
	
	public boolean getBoolean(int index) {
		return (Boolean) get(index).getValue();
	}
	
	public AbsonArray getArray(int index) {
		return (AbsonArray) get(index).getValue();
	}
	
	public AbsonObject getObject(int index) {
		return (AbsonObject) get(index).getValue();
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		AbsonObject obj = new AbsonObject();
		for (int i=0; i<size(); i++) {
			obj.put(Integer.toString(i), get(i));
		}
		obj.toBson(stream);
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
		return 0x04;
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
	public AbsonArray getValue() {
		return this;
	}
	
	public static AbsonArray fromBson(InputStream stream) throws IOException {
		AbsonObject temp = AbsonObject.fromBson(stream);
		AbsonArray res = new AbsonArray();
		for (Absonifyable value : temp.values()) {
			res.add(value);
		}
		return res;
	}
	
	public static AbsonArray fromJson(String json) throws AbsonParseException {
		try {
			return new JsonParser(json).readArray();
		} catch(AbsonParseException ex) {
			throw ex;
		} catch (Exception ex) {
			return null;
		}
	}
	
	@Override
	public void toJson(Writer output, JsonPrintSettings settings) throws IOException {
		if(settings.isMultiline()) {
			toMultilineJson(output, settings);
			return;
		}
		
		boolean first = true;
		output.write(settings.hasWhitespace() ? "[ " : "[");
		
		final JsonPrintSettings nextSettings = settings.getNextLevel();
		for (Absonifyable value : this) {
			if(!first) output.write(settings.hasWhitespace() ? ", " : ",");
			value.toJson(output, nextSettings);
			first = false;
		}
		
		output.write(settings.hasWhitespace() ? " ]" : "]");
	}
	
	protected void toMultilineJson(Writer output, JsonPrintSettings settings) throws IOException {
		output.write("[\n");
		
		int count = 0;
		
		final JsonPrintSettings nextSettings = settings.getNextLevel();
		for (Absonifyable value : this) {
			JsonUtil.indent(output, nextSettings.getStartIndent());
			
			value.toJson(output, nextSettings);
			
			if(count != this.size() - 1)
				output.write(",");
			output.write("\n");
			
			count++;
		}
		
		JsonUtil.indent(output, settings.getStartIndent());
		output.write("]");
	}
}
