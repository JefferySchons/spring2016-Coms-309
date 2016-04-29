package com.git.cs309.mmoserver.cycle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.git.cs309.mmoserver.util.TickProcess;

/**
 * 
 * @author Group 21
 * 
 *         Handles all CycleProcesses. A CycleProcess is simply a task that
 *         needs to be executed per tick, but doesn't need or can't implement
 *         TickProcess themselves. Good uses for CycleProcesses are: Events,
 *         Walking, Buffs, things like that. CycleProcesses can also terminate
 *         if a condition is met.
 */
public final class CycleProcessManager extends TickProcess {
	

	private final Set<CycleProcess> processes = new HashSet<>(); // Set of processes.

	//Private so that only this class can access constructor.
	public CycleProcessManager() {
		
	}

	/**
	 * Add a new process for execution.
	 * 
	 * @param process
	 *            new process
	 */
	public void addProcess(final CycleProcess process) {
		synchronized (processes) {
			processes.add(process);
		}
	}

	@Override
	public void ensureSafeClose() {
		//Not needed
	}

	@Override
	public void printStatus() {
		println("Total cycle processes: " + processes.size());
	}

	@Override
	protected synchronized void tickTask() {
		List<CycleProcess> removalList = new ArrayList<>(); // List of objects to remove after finished processing. Because of for-each loop, concurrent modification would occur if they were removed during loop.
		synchronized (processes) {
			for (CycleProcess process : processes) {
				process.process();
				if (process.finished()) { // Check if process is finished.
					process.end();
					removalList.add(process);
				}
			}
			processes.removeAll(removalList);
		}
	}

	@Override
	public String getVariableName() {
		return "CycleProcessManager";
	}
}
