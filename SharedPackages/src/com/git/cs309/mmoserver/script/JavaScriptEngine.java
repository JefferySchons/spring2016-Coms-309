package com.git.cs309.mmoserver.script;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public final class JavaScriptEngine {
	private static final ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
	
	public static final Object runScript(final String scriptPath) throws FileNotFoundException, ScriptException {
		return scriptEngine.eval(new FileReader(scriptPath));
	}
	
	public static final Object runStringScript(final String script) throws ScriptException {
		return scriptEngine.eval(script);
	}
	
	public static final Object runScript(final String scriptPath, final Bindings bindings) throws FileNotFoundException, ScriptException {
		return scriptEngine.eval(new FileReader(scriptPath), bindings);
	}
	
	public static final Object runStringScript(final String script, final Bindings bindings) throws ScriptException {
		return scriptEngine.eval(script, bindings);
	}
	
	public static final Object invokeFunction(final String function, final Object...args) throws NoSuchMethodException, ScriptException {
		Invocable invocable = (Invocable) scriptEngine;
		return invocable.invokeFunction(function, args);
	}
	
	public static final void setVariable(final String variable, final Object object) {
		scriptEngine.put(variable, object);
	}
	
	public static final ScriptEngine getScriptEngine() {
		return scriptEngine;
	}
}
