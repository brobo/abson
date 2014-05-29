package tech.magnitude.abson;

import java.util.HashMap;
import java.util.Map;

import tech.magnitude.abson.elements.AbsonObject;

public class AbsonFactory {
	
	private static final AbsonFactory singleton = new AbsonFactory();
	
	private static AbsonFactory getSingleton() {
		return singleton;
	}
	
	private Map<Class<? extends AbsonDecomposable>, AbsonComposer<? extends AbsonDecomposable>> composers;
	private Map<String, Class<? extends AbsonDecomposable>> classNames;
	
	private AbsonFactory() {
		composers = new HashMap<>();
		classNames = new HashMap<>();
	}
	
	public static <T extends AbsonDecomposable> void registerComposer(Class<T> _class, AbsonComposer<T> composer) {
		AbsonFactory factory = getSingleton();
		factory.classNames.put(_class.getName(), _class);
		factory.composers.put(_class, composer);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends AbsonDecomposable> T compose(Class<T> _class, AbsonObject object) {
		AbsonFactory factory = getSingleton();
		return (T)factory.composers.get(_class).compose(object);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends AbsonDecomposable> T compose(String className, AbsonObject object) {
		AbsonFactory factory = getSingleton();
		return (T)compose(factory.classNames.get(className), object);
	}
}
