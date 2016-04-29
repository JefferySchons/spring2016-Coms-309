package com.git.cs309.mmoserver.lang;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AnnotationWrapper {
	protected final HashMap<Class<? extends Annotation>, List<Method>> methodsByAnnotation = new HashMap<>();
	
	public final List<Method> getAnnotatedMethod(final Class<? extends Annotation> annotation) {
		if (methodsByAnnotation.containsKey(annotation)) {
			return methodsByAnnotation.get(annotation);
		}
		List<Method> methods = new ArrayList<>();
		for (Method method : this.getClass().getMethods()) {
			if (method.isAnnotationPresent(annotation)) {
				methods.add(method);
			}
		}
		methodsByAnnotation.put(annotation, methods);
		return methods;
	}
}
