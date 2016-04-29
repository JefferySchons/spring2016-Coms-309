package com.git.cs309.mmoserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.git.cs309.mmoserver.connection.ConnectionAcceptor;
import com.git.cs309.mmoserver.connection.ConnectionManager;
import com.git.cs309.mmoserver.cycle.CycleProcessManager;
import com.git.cs309.mmoserver.entity.characters.CharacterManager;
import com.git.cs309.mmoserver.entity.characters.npc.NPCFactory;
import com.git.cs309.mmoserver.entity.characters.npc.dropsystem.DropSystem;
import com.git.cs309.mmoserver.entity.characters.user.ModerationHandler;
import com.git.cs309.mmoserver.entity.characters.user.UserManager;
import com.git.cs309.mmoserver.entity.objects.GameObjectFactory;
import com.git.cs309.mmoserver.io.Logger;
import com.git.cs309.mmoserver.items.ItemFactory;
import com.git.cs309.mmoserver.lang.module.Module;
import com.git.cs309.mmoserver.lang.module.ModuleManager;
import com.git.cs309.mmoserver.map.MapFactory;
import com.git.cs309.mmoserver.map.MapManager;
import com.git.cs309.mmoserver.script.JavaScriptEngine;
import com.git.cs309.mmoserver.util.TickProcess;

public class Server extends Module {

	// Is server running.
	private volatile boolean running = true;

	private boolean debug = false;

	// Object that all TickProcess objects wait on for tick notification.
	private final Object TICK_NOTIFIER = new Object(); // To notify

	// List of TickProcess objects currently running. They add themselves to
	// this list when instantiated.
	private final List<TickProcess> TICK_RELIANT_LIST = new ArrayList<>();

	// threads of
	// new tick.
	// Current server ticks count.
	private volatile long tickCount = 0; // Tick count.
	
	/**
	 * Can be used to register tick reliants so that server will know to wait
	 * until theyre finished. Automatically invoked from TickProcess contructor.
	 * 
	 * @param TickProcess
	 *            new object to register in list.
	 */
	public void addTickProcess(final TickProcess TickProcess) {
		synchronized (TICK_RELIANT_LIST) { // Synchronize block to obtain
											// TICK_RELIANT_LIST lock
			TICK_RELIANT_LIST.add(TickProcess);
		}
	}

	/**
	 * Getter method for TICK_NOTIFIER.
	 * 
	 * @return the TICK_NOTIFIER object.
	 */
	public Object getTickNotifier() {
		return TICK_NOTIFIER;
	}

	/**
	 * Getter for running state.
	 * 
	 * @return running state
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Getter method for tickCount.
	 * 
	 * @return current tickCount
	 */
	public long getTickCount() {
		return tickCount;
	}

	public boolean isDebug() {
		return debug;
	}

	public Server() {
		if (!debug) {
			System.setOut(Logger.getOutPrintStream());
			System.setErr(Logger.getErrPrintStream());
			JavaScriptEngine.setVariable("out", System.out);
		}
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				saveEverything();
				System.out.println("Saved everything before going down.");
			}
		});
		ConnectionAcceptor.startAcceptor(43594);
	}
	
	/**
	 * Requests program termination.
	 */
	public  void requestExit() {
		running = false;
	}
	
	/**
	 * Initializes handler and managers, to ensure they're ready to handle
	 * activity.
	 */
	private static void loadAndStartClasses() {
		ModerationHandler.loadModerations();
		try {
			NPCFactory.getInstance().loadDefinitions();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			GameObjectFactory.getInstance().loadDefinitions();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			ItemFactory.getInstance().loadDefinitions();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			DropSystem.getInstance().loadDrops();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		MapFactory.getInstance();
		ModuleManager.getModule(MapManager.class).loadMaps();
		ModuleManager.getModule(ConnectionManager.class);
		ModuleManager.getModule(CycleProcessManager.class);
		ModuleManager.getModule(CharacterManager.class);
	}
	
	public void runServer() {
		running = true;
		loadAndStartClasses(); // Call initialize block, which will initialize things
		// that should be initialized before starting server.
		try {
			JavaScriptEngine.invokeFunction("dumpVariables", new Object[] {});
		} catch (NoSuchMethodException | ScriptException e1) {
			e1.printStackTrace();
		}
		System.out.println("Starting server...");
		int ticks = 0;
		long tickTimes = 0L;
		while (running) {
			long start = System.currentTimeMillis();
			synchronized (TICK_NOTIFIER) { // Obtain intrinsic lock and notify
											// all waiting threads. This starts
											// the tick.
				TICK_NOTIFIER.notifyAll();
			}
			boolean allFinished;
			do { // This block keeps looping until all tick reliant threads are
					// finished with their tick.
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// Don't really care too much if it gets interrupted.
				}
				allFinished = true;
				for (TickProcess t : TICK_RELIANT_LIST) {
					if (t.isStopped()) { // Uncaught exception or SOMETHING
						t.start(); // Automatically restart.
						break;
					}
					if (!t.tickFinished()) {
						allFinished = false;
						break;
					}
				}
			} while (!allFinished);
			long timeLeft = Config.MILLISECONDS_PER_TICK - (System.currentTimeMillis() - start); // Calculate
																									// remaining
																									// time.
			tickTimes += (System.currentTimeMillis() - start);
			ticks++;
			tickCount++;
			if (ticks == Config.TICKS_PER_MINUTE * Config.STATUS_PRINT_RATE) { // For visual map, Config.TICKS_PER_WALK / 2
				System.out.println(" ");
				System.out.println("Average tick consumption over " + Config.STATUS_PRINT_RATE + " minutes: "
						+ String.format("%.3f", ((tickTimes / (float) (Config.MILLISECONDS_PER_TICK * ticks))) * 100.0f)
						+ "%.");
				for (TickProcess process : TICK_RELIANT_LIST) {
					process.printStatus();
				}
				System.out.println(" ");
				//MapHandler.getInstance().printMaps();
				ticks = 0;
				tickTimes = 0L;
			}
			if (timeLeft < 0) {
				System.err.println("Warning: Server is lagging behind desired tick time " + (-timeLeft) + "ms.");
			}
			if (timeLeft < 2) {
				timeLeft = 2; // Must wait at least a little bit, so that
								// threads can catch up and wait.
			}
			try {
				Thread.sleep(timeLeft);
			} catch (InterruptedException e) {
				// Don't really care too much if it gets interrupted.
			}
		}
		System.out.println("Server going down...");
		saveEverything();
		System.out.println("Saved all users before going down.");
		System.out.println("");
		System.out.println("");
	}

	private static void saveEverything() {
		UserManager.saveAllUsers();
		ModerationHandler.saveModerations();
	}

	@Override
	public String getVariableName() {
		return "Server";
	}

}
