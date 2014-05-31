package tech.magnitude.abson;

import java.util.HashMap;
import java.util.Map;

import tech.magnitude.abson.elements.AbsonObject;

/**
 * Represents a simple factory through which composers may be
 * associated with classes and optional shorthand class notations.
 * @author blacksmithgu
 * @author brobo
 */
public class AbsonFactory {
	private Map<Class<? extends AbsonDecomposable>, AbsonComposer<? extends AbsonDecomposable>> composers;
	private Map<String, Class<? extends AbsonDecomposable>> classNames;
	
	/**
	 * Creates a new AbsonFactory with no registered composers.
	 */
	public AbsonFactory() {
		composers = new HashMap<>();
		classNames = new HashMap<>();
	}
	
	/**
	 * Registers a composer with the given shorthand name, class which it constructs, and the actual composer.
	 * Note that the shorthand name is a convienence reference which has virtue if you are also sending the
	 * object's type dynamically over the network. If one is provided, as is implied by using this overload of
	 * the method, you can then compose by providing the string name rather than the class object.
	 * @param key The shorthand name to be associated with this composer.
	 * @param _class The class which this composer composes.
	 * @param composer The composer to be registered.
	 */
	public <T extends AbsonDecomposable> void registerComposer(String key, Class<T> _class, AbsonComposer<T> composer) {
		classNames.put(key, _class);
		registerComposer(_class, composer);
	}
	
	/**
	 * Registers a composer with the given class and composer. This method
	 * automatically uses the given class's full name (such as tech.magnitude.abson.AbsonFactory)
	 * for the shorthand name.
	 * @param _class
	 * @param composer
	 */
	public <T extends AbsonDecomposable> void registerComposer(Class<T> _class, AbsonComposer<T> composer) {
		classNames.put(_class.getName(), _class);
		composers.put(_class, composer);
	}
	
	/**
	 * Composes an instance of the specified class using the composer which is registered with this
	 * factory and the provided AbsonObject. If such a composer is not registered with this factory,
	 * a NullPointerException is thrown.
	 * @param _class The class for which to compose an instance.
	 * @param object The object which holds to data for composition.
	 * @return The composed object.
	 */
	public <T extends AbsonDecomposable> T compose(Class<T> _class, AbsonObject object) {
		return getComposer(_class).compose(object);
	}
	
	/**
	 * Composes an instance of the specified class using the composer which is registered with this factory;
	 * the class is obtained by a lookup using the short-hand name provided. This class generally only has use
	 * if a short-hand name is also provided when the composer is registered. If no composer is found with this
	 * short-hand name, a NullPointerException is thrown.
	 * 
	 * Note that while this method is generic, there is no actual way for it to infer the generic type, and thus
	 * requires casting.
	 * @param className
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbsonDecomposable> T compose(String className, AbsonObject object) {
		return (T)compose(classNames.get(className), object);
	}
	
	/**
	 * Obtains the composer associated with the specified class.
	 * @param _class The class for which to obtain the composer.
	 * @return The composer associated with the specified class, or null if no such composer exists.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends AbsonDecomposable> AbsonComposer<T> getComposer(Class<T> _class) {
		return (AbsonComposer<T>)composers.get(_class);
	}
	
	/**
	 * Obtains the composer associated with the specified short-hand class name. To ensure
	 * generic type safety, it is recommended to use getComposer(Class<T>, String) if
	 * you are aware of what type or super-type the composer will return.
	 * @param className The short-hand class name for which to obtain the composer.
	 * @return The composer associated with the specified class, or null if no such composer exists.
	 */
	protected AbsonComposer<? extends AbsonDecomposable> getComposer(String className) {
		return getComposer(classNames.get(className));
	}
	
	/**
	 * Obtains the composer associated with the specified short-hand class name and provided class.
	 * This is the typesafe version of getComposer(String), and should be used preferentially.
	 * @param _class The class which represents the type that the composer composes (or a super-type).
	 * @param className The class name which is the short-hand name associated with the composer.
	 * @return The composer associated with the specified short-hand name and class, or null if no such composer exists.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends AbsonDecomposable> AbsonComposer<T> getComposer(Class<T> _class, String className) {
		return (AbsonComposer<T>)composers.get(className);
	}
}
