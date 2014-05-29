package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.PrintUtil;

public class AbsonArray extends ArrayList<Absonifyable> implements Absonifyable {

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
	
	public void add(int val) {
		add(new Abson32Integer(val));
	}
	
	public void add(long val) {
		add(new Abson64Integer(val));
	}
	
	public void add(String val) {
		add(new AbsonString(val));
	}
	
	public void add(double val) {
		add(new AbsonFloatingPoint(val));
	}
	
	public void add(Date date) {
		add(new AbsonUTCDatetime(date));
	}
	
	public void add(boolean value) {
		add(new AbsonBoolean(value));
	}
	
	public void add(AbsonArray array) {
		add(array);
	}
	
	public int getInteger(int index) {
		return ((Abson32Integer) get(index)).getValue();
	}
	
	public long getLong(int index) {
		return ((Abson64Integer) get(index)).getValue();
	}
	
	public String getString(int index) {
		return ((AbsonString) get(index)).getValue();
	}
	
	public double getDouble(int index) {
		return ((AbsonFloatingPoint) get(index)).getValue();
	}
	
	public Date getDate(int index) {
		return ((AbsonUTCDatetime) get(index)).getValue();
	}
	
	public boolean getBoolean(int index) {
		return ((AbsonBoolean) get(index)).getValue();
	}
	
	public AbsonArray getArray(int index) {
		return ((AbsonArray) get(index));
	}
	
	public AbsonObject getObject(int index) {
		return (AbsonObject) get(index);
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		AbsonObject obj = new AbsonObject();
		for (int i=0; i<size(); i++) {
			obj.put(Integer.toString(i), get(i));
		}
		obj.toBson(stream);
	}

	@Override
	public byte getBsonPrefix() {
		return 0x04;
	}

	public String toJson() {
		return toJson(JsonPrintSettings.DEFAULT);
	}
	
	public String toJson(JsonPrintSettings settings) {
		return PrintUtil.getString(this, settings);
	}
	
	@Override
	public String toString() {
		return toJson();
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
			PrintUtil.indent(output, nextSettings.getStartIndent());
			
			value.toJson(output, nextSettings);
			
			if(count != this.size() - 1)
				output.write(",");
			output.write("\n");
			
			count++;
		}
		
		PrintUtil.indent(output, settings.getStartIndent());
		output.write("]");
	}
}
