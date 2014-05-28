package tech.magnitude.abson;

import java.io.IOException;
import java.io.OutputStream;

public interface Absonifyable {
	
	String toJson();
	void toBson(OutputStream stream) throws IOException;
	public abstract byte getBsonPrefix();
}
