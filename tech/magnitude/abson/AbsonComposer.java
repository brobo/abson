package tech.magnitude.abson;

import tech.magnitude.abson.elements.AbsonObject;

/**
 * Provides an interface which composes a specific type T from
 * an AbsonObject which was presumably created from that objects'
 * decompose function.
 * @author blacksmithgu
 * @author brobo
 *
 * @param <T> The class which this composer composes.
 */
public interface AbsonComposer<T extends AbsonDecomposable> {
	/**
	 * Composes an object of type T, given the AbsonObject which 
	 * was presumably created from the same classes' decompose
	 * function.
	 * @param abson The AbsonObject which contains the data to compose.
	 * @return The composed object.
	 */
	T compose(AbsonObject abson);
}
