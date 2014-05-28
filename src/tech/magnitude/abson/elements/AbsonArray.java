package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import tech.magnitude.abson.Absonifyable;

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
	
	public void addInteger(int val) {
		add(new Abson32Integer(val));
	}
	
	public void addLong(long val) {
		add(new Abson64Integer(val));
	}
	
	public void addString(String val) {
		add(new AbsonString(val));
	}
	
	public void addDouble(double val) {
		add(new AbsonFloatingPoint(val));
	}
	
	public void addDate(Date date) {
		add(new AbsonUTCDatetime(date));
	}
	
	public void addBoolean(boolean value) {
		add(new AbsonBoolean(value));
	}
	
	public void addArray(AbsonArray array) {
		add(array);
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		AbsonObject obj = new AbsonObject();
		for (int i=0; i<size(); i++) {
			obj.put("" + i, get(i));
		}
		obj.toBson(stream);
	}

	@Override
	public byte getBsonPrefix() {
		return 0x04;
	}

	@Override
	public String toJson() {
		String res = "[";
		for (Absonifyable value : this) {
			res += value.toJson() + ",";
		}
		if (res.length() > 1) {
			res = res.substring(0, res.length()-1);
		}
		res += "]";
		return res;
	}
}
