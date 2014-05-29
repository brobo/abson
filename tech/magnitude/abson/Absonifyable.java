package tech.magnitude.abson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public interface Absonifyable {
	
	void toJson(Writer writer, JsonPrintSettings settings) throws IOException;
	
	void toBson(OutputStream stream) throws IOException;
	public abstract byte getBsonPrefix();
	
	public Object getValue();
}
