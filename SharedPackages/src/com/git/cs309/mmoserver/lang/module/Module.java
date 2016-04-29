package com.git.cs309.mmoserver.lang.module;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.git.cs309.mmoserver.script.GlobalScriptVariable;
import com.git.cs309.mmoserver.script.JavaScriptEngine;

public abstract class Module extends GlobalScriptVariable {
	protected final HashMap<String, Method> methodMap = new HashMap<>();
	
	public Module() {
		super(JavaScriptEngine.getScriptEngine());
		for (Method method : this.getClass().getMethods()) {
			methodMap.put(method.getName(), method);
		}
	}
	
	@SuppressWarnings("unchecked")
	public final <T> T invoke(final String methodName, final Object...arguments) {
		Method method = methodMap.get(methodName);
		try {
			return (T) method.invoke(this, arguments);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return (T) new Object();
	}
	
	public final <T> T invoke(final Class<T> cls, final String methodName, final Object...arguments) {
		Method method = methodMap.get(methodName);
		try {
			return cls.cast(method.invoke(this, arguments));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return cls.cast(new Object());
	}
}
