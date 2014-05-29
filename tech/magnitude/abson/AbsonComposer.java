package tech.magnitude.abson;

import tech.magnitude.abson.elements.AbsonObject;

public interface AbsonComposer<T extends AbsonDecomposable> {
	T compose(AbsonObject abson);
}
