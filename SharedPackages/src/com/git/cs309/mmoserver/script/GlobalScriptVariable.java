package com.git.cs309.mmoserver.script;

import javax.script.ScriptEngine;

public abstract class GlobalScriptVariable {
	protected GlobalScriptVariable(ScriptEngine scriptEngine) {
		scriptEngine.put(getVariableName(), this);
	}
	
	public abstract String getVariableName();
}
