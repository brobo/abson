package tech.magnitude.abson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * A utility class which defines a set of functions any Abson object should have (minimally)
 * in order to be properly formatted.
 * @author blacksmithgu
 */
public interface Absonifyable {
	/**
	 * Writes the JSON representation of this object out to the specified writer with the specified settings.
	 * @param writer The writer to write the JSON representation to.
	 * @param settings The settings to use when writing the JSON.
	 * @throws IOException Thrown if an IO error occurs while using the provided writer.
	 */
	void toJson(Writer writer, JsonPrintSettings settings) throws IOException;
	
	/**
	 * Writes the BSON representation of this object out to the specified OutputStream.
	 * @param stream The stream to write the BSON representation to.
	 * @throws IOException Thrown if an IO error occurs while using the provided stream.
	 */
	void toBson(OutputStream stream) throws IOException;
	
	/**
	 * Obtains the BSON prefix which represents this type.
	 * @return The BSON prefix which represents this type.
	 */
	public byte getBsonPrefix();
	
	/**
	 * Obtains the value which this Absonifyable holds.
	 * @return The value which this Absonifyable holds.
	 */
	public Object getValue();
}
