package tech.magnitude.abson;

import java.util.HashMap;
import java.util.Map;

import tech.magnitude.abson.elements.AbsonObject;

public class AbsonFactory {
	private Map<Class<? extends AbsonDecomposable>, AbsonComposer<? extends AbsonDecomposable>> composers;
	private Map<String, Class<? extends AbsonDecomposable>> classNames;
	
	public AbsonFactory() {
		composers = new HashMap<>();
		classNames = new HashMap<>();
	}
	
	public <T extends AbsonDecomposable> void registerComposer(String key, Class<T> _class, AbsonComposer<T> composer) {
		classNames.put(key, _class);
		registerComposer(_class, composer);
	}
	
	public <T extends AbsonDecomposable> void registerComposer(Class<T> _class, AbsonComposer<T> composer) {
		classNames.put(_class.getName(), _class);
		composers.put(_class, composer);
	}
	
	public <T extends AbsonDecomposable> T compose(Class<T> _class, AbsonObject object) {
		return getComposer(_class).compose(object);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbsonDecomposable> T compose(String className, AbsonObject object) {
		return (T)compose(classNames.get(className), object);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends AbsonDecomposable> AbsonComposer<T> getComposer(Class<T> _class) {
		return (AbsonComposer<T>)composers.get(_class);
	}
	
	protected AbsonComposer<? extends AbsonDecomposable> getComposer(String className) {
		return getComposer(classNames.get(className));
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends AbsonDecomposable> AbsonComposer<T> getComposer(Class<T> _class, String className) {
		return (AbsonComposer<T>)composers.get(className);
	}
}
