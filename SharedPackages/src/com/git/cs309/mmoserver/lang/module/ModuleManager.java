package com.git.cs309.mmoserver.lang.module;

import java.util.HashMap;

public final class ModuleManager extends Module {
	private static final HashMap<String, Module> moduleMapByName = new HashMap<>();
	private static final HashMap<Class<? extends Module>, Module> moduleMapByClass = new HashMap<>();
	private static String projectBase = "";
	private static final ModuleManager INSTANCE = new ModuleManager();
	
	private ModuleManager() {
		moduleMapByName.put(getVariableName(), this);
		moduleMapByClass.put(ModuleManager.class, this);
	}
	
	public static final ModuleManager getInstance() {
		return INSTANCE;
	}
	
	public static final <T extends Module> T getModule(final String moduleName, final Class<T> cls) {
		if (moduleMapByName.containsKey(moduleName)) {
			return cls.cast(moduleMapByName.get(moduleName));
		}
		try {
			T module = cls.newInstance();
			moduleMapByName.put(moduleName, module);
			return cls.cast(module);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return cls.cast(new Object());
	}
	
	public static final <T extends Module> T getModule(final Class<T> cls) {
		if (moduleMapByClass.containsKey(cls)) {
			return cls.cast(moduleMapByClass.get(cls));
		}
		try {
			Module module = (Module) cls.newInstance();
			moduleMapByClass.put(cls, module);
			moduleMapByName.put(cls.getName().replace(projectBase, ""), module);
			return cls.cast(module);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return cls.cast(new Object());
	}
	
	public static final void setProjectBase(final String newProjectBase) {
		projectBase = newProjectBase;
	}

	@Override
	public String getVariableName() {
		return "ModuleManager";
	}
}
