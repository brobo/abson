package tech.magnitude.abson;

import tech.magnitude.abson.elements.AbsonObject;

/**
 * Represents an object which can be decomposed into
 * an AbsonObject.
 * @author blacksmithgu
 * @author brobo
 */
public interface AbsonDecomposable {
	/**
	 * Decomposes the object into an AbsonObject, such that
	 * the object can later be re-composed.
	 * @return The decomposition of the object.
	 */
	AbsonObject decompose();
}
