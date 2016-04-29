package com.git.cs309.mmoserver;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

import javax.script.ScriptException;

import com.git.cs309.mmoserver.lang.module.ModuleManager;
import com.git.cs309.mmoserver.script.JavaScriptEngine;

/*
 * TODO Section:
 * Implement party system
 * Fix Maps so that more than one Entity can exist on same tile
 * Add more packet outputs for various things
 * Add more packets to output MORE various things
 * Add and send map packet, which tells client player is in new map
 */

/**
 * 
 * @author Group 21
 *
 *         Main is the main class and entry point of the MMOServer. I also
 *         handles the "tick" mechanics.
 *
 *         <p>
 *         A tick in this server framework is simply a notification to all the
 *         threads running TickProcess objects telling them to wake up from
 *         their wait call. All tick reliants invoke <code>wait()</code> on the
 *         TICK_NOTIFIER object, which they retrieve from this class. When a
 *         "tick" is sent out, <code>notifyAll()</code> is called on
 *         TICK_NOTIFIER, which in turn allows threads to exit wait and begin
 *         execution of their tick procedures. Once a tick begins, the main
 *         thread (the one used during entry into this class, which also handles
 *         ticking) waits until all TickProcess objects have finished their tick
 *         procedures. The main thread then waits out any remaining time, then
 *         notifies all the threads waiting on TICK_NOTIFIER to start a new tick
 *         cycle, infinitely repeating.
 *         </p>
 * 
 *         <p>
 *         Another neat feature is that whenever a TickProcess thread fails
 *         because of an uncaught exception, the whole server pauses (after the
 *         current tick) so that debugging may commence. From there, the thread
 *         can actually be restarted from a button in the GUI.
 *         </p>
 */
public final class Main {

	//Turns out that using the default system loader will just re-reference already loaded classes. Would need to create and use a different classloader
	//Will do, if I can find time to do something ridiculous like that. Keeping them like this for time being (not singletons, that is)

	/**
	 * Main method, duh.
	 * 
	 * @param args
	 * @throws UnknownHostException
	 * @throws ScriptException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws UnknownHostException, FileNotFoundException, ScriptException {
		ModuleManager.setProjectBase("com.git.cs309.mmoserver.");
		ModuleManager.getModule(Server.class);
		JavaScriptEngine.setVariable("out", System.out);
		JavaScriptEngine.runScript("./data/scripts/serverstart.js");
	}
}
