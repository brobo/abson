package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import tech.magnitude.abson.AbsonComposer;
import tech.magnitude.abson.AbsonConstants;
import tech.magnitude.abson.AbsonDecomposable;
import tech.magnitude.abson.AbsonParseException;
import tech.magnitude.abson.AbsonValue;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonParser;
import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.JsonUtil;

public class AbsonArray extends ArrayList<AbsonValue> implements AbsonValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5680398373817754508L;

	public static AbsonArray fromCollection(Collection<? extends AbsonDecomposable> coll) {
		return new AbsonArray(coll);
	}
	
	public AbsonArray() {
		super();
	}
	
	public AbsonArray(Collection<? extends AbsonDecomposable> collection) {
		super();
		for(AbsonDecomposable decomp : collection)
			add(decomp);
	}
	
	public AbsonArray(AbsonArray old) {
		super(old);
	}
	
	public AbsonArray(int initialCapacity) {
		super(initialCapacity);
	}
	
	public Object getValue(int index) {
		return get(index).getValue();
	}
	
	public boolean isNull(int index) {
		return index < 0 || index >= size() || get(index).getValue() == null;
	}

	public boolean add(AbsonValue obj) {
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
	
	public boolean add() {
		return super.add(new AbsonNull());
	}
	
	public boolean add(AbsonDecomposable object) {
		return this.add(object.decompose());
	}
	
	public boolean add(byte[] binary) {
		return this.add(new AbsonBinary(binary));
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
		if(isNull(index)) return null;
		return (Date) get(index).getValue();
	}
	
	public boolean getBoolean(int index) {
		return (Boolean) get(index).getValue();
	}
	
	public AbsonArray getArray(int index) {
		if(isNull(index)) return null;
		return (AbsonArray) get(index).getValue();
	}
	
	public AbsonObject getObject(int index) {
		if(isNull(index)) return null;
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
	
	/**
	 * Returns a list with entries of specified type. Note that this function is
	 * only practical for homogenous lists with primitive objects that can be stored by the Abson library;
	 * for complex objects, use asList(AbsonComposer<T>).
	 * 
	 * This function is capable of converting between wrapper classes internally.
	 * @param targetType The type to attempt to cast all objects to.
	 * @return A list containing the casted objects.
	 */
	public <T> List<T> asList(Class<T> targetType) {
		List<T> res = new ArrayList<T>();
		
		for(AbsonValue object : this) {
			Object value = object.getValue();
			res.add(object instanceof AbsonNumber ? JsonUtil.castNumber((Number) value, targetType) : targetType.cast(value));
		}
		
		return res;
	}
	
	/**
	 * Fills a list with the objects in this AbsonArray by attempting to
	 * cast all of their values to the lists' type.
	 * 
	 * This function is capable of converting between wrapper classes internally.
	 * @param list The list to fill.
	 */
	public <T> void fillCollection(Collection<T> list, Class<T> targetType) {
		for(AbsonValue object : this) {
			Object value = object.getValue();
			list.add(object instanceof AbsonNumber ? JsonUtil.castNumber((Number) value, targetType) : targetType.cast(value));
		}
	}
	
	/**
	 * Returns a list with entries created by applying the composer on all
	 * AbsonObjects in this array. Elements which are not AbsonObjects are
	 * ignored (rather than throwing an error).
	 * @param composer The composer to apply to all AbsonObjects.
	 * @return A list containing the composed objects.
	 */
	public <T extends AbsonDecomposable> List<T> asList(AbsonComposer<T> composer) {
		List<T> res = new ArrayList<T>();
		for(AbsonValue object : this)
			if(object instanceof AbsonObject) res.add(composer.compose((AbsonObject) object));
		
		return res;
	}
	
	/**
	 * Fills a list with the objects in this AbsonArray by applying the
	 * provided composer to all of the AbsonObjects in this array; other values
	 * are ignored.
	 * @param list The list to fill.
	 * @param composer The composer to apply.
	 */
	public <T extends AbsonDecomposable> void fillCollection(Collection<T> list, AbsonComposer<T> composer) {
		for(AbsonValue object : this)
			if(object instanceof AbsonObject) list.add(composer.compose((AbsonObject) object));
	}
	
	public static AbsonArray fromBson(InputStream stream) throws IOException {
		AbsonObject temp = AbsonObject.fromBson(stream);
		AbsonArray res = new AbsonArray();
		for (AbsonValue value : temp.values()) {
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
		if(settings.isMultiline() && this.size() > 0) {
			toMultilineJson(output, settings);
			return;
		}
		
		boolean first = true;
		output.write(AbsonConstants.OPENING_ARRAY);
		if(settings.hasWhitespace()) output.write(" ");
		
		final JsonPrintSettings nextSettings = settings.getNextLevel();
		for (AbsonValue value : this) {
			if(!first) {
				output.write(AbsonConstants.ENTRY_SEPERATOR);
				if(settings.hasWhitespace()) output.write(" ");
			}
			value.toJson(output, nextSettings);
			first = false;
		}
		
		if(settings.hasWhitespace()) output.write(" ");
		output.write(AbsonConstants.CLOSING_ARRAY);
	}
	
	protected void toMultilineJson(Writer output, JsonPrintSettings settings) throws IOException {
		output.write(AbsonConstants.OPENING_ARRAY + "\n");
		
		int count = 0;
		
		final JsonPrintSettings nextSettings = settings.getNextLevel();
		for (AbsonValue value : this) {
			JsonUtil.indent(output, nextSettings.getStartIndent());
			
			value.toJson(output, nextSettings);
			
			if(count != this.size() - 1)
				output.write(AbsonConstants.ENTRY_SEPERATOR);
			output.write("\n");
			
			count++;
		}
		
		JsonUtil.indent(output, settings.getStartIndent());
		output.write(AbsonConstants.CLOSING_ARRAY);
	}
}
