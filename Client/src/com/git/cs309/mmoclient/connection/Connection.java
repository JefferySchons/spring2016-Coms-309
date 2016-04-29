package com.git.cs309.mmoclient.connection;

import java.io.IOException;
import java.net.Socket;

import com.git.cs309.mmoclient.packets.PacketHandler;
import com.git.cs309.mmoserver.connection.AbstractConnection;
import com.git.cs309.mmoserver.packets.Packet;

public class Connection extends AbstractConnection {

	/**
	 * Encapsulate a {@link java.net.Socket} into a Connection container object.
	 * @param socket the socket to be encapsulated.
	 * @throws IOException if any IOException occurs with the socket while getting it's attributes.
	 */
	public Connection(Socket socket) throws IOException {
		super(socket);
	}

	/**
	 * Iteration start block is a method called at the begging of each loop (the same loop that grabs packets).
	 * It should be overridden if anything needs to occur before the start of the loop.
	 */
	@Override
	public void iterationStartBlock() {
		// Nothing needed
	}
	/**
	 * How many packets can this connection recieve per loop before automatically dropping the connection.
	 * For the Client, it shouldn't worry with this, since this method was designed mainly for server 
	 * implementations.
	 */
	@Override
	public int maxPacketsPerIteration() {
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Executes once the loop exits.
	 * Should be overridden if any final steps need to be taken right before a connection is dropped.
	 */
	@Override
	public void postRun() {
		//Nothing needed
	}

	/**
	 * Must be overridden with packet handling code.
	 */
	@Override
	public void handlePacket(Packet arg0) {
		PacketHandler.getInstance().handlePacket(arg0);
	}
}
